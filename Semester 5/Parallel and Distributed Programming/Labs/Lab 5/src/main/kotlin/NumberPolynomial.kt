@file:Suppress("UNCHECKED_CAST")

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class NumberPolynomial<T : Number>(coefficients: List<T>) {
    companion object {
        private fun <T : Number> prune(list: List<T>): List<T> {
            val result = list.toMutableList()
            while (result.size > 1 && result.last() == 0)
                result.removeLast()
            return result
        }
    }

    private val coefficients: List<T>
    private val degree: Int // This is not actually degree, it's degree + 1

    init {
        this.coefficients = prune(coefficients)
        this.degree = this.coefficients.size
    }

    private fun getZero(): T {
        return when (coefficients[0]) {
            is Long -> 0.toLong() as T
            is Float -> 0.toFloat() as T
            is Int -> 0 as T
            is Double -> 0.toDouble() as T
            is BigInt -> BigInt(0) as T
            else -> throw Exception()
        }
    }

    private fun getZeroPolynomial(): NumberPolynomial<T> {
        return NumberPolynomial(listOf(getZero()))
    }

    fun simpleProduct(y: NumberPolynomial<T>): NumberPolynomial<T> {
        val newPolynomial = List(degree + y.degree - 1) { getZero() }.toMutableList()
        for (i in 0 until degree)
            for (j in 0 until y.degree)
                newPolynomial[i + j] = (newPolynomial[i + j] + get(i) * y.get(j)) as T
        return NumberPolynomial(newPolynomial)
    }

    fun concurrentSimpleProduct(y: NumberPolynomial<T>, threadsNumber: Int): NumberPolynomial<T> {
        val newPolynomial = List(degree + y.degree - 1) { getZero() }.toMutableList()
        val iterations = degree * y.degree
        val stepSize = ceil(iterations.toDouble() / threadsNumber.toDouble()).toInt()
        val executor = Executors.newFixedThreadPool(threadsNumber)
        val range = (0 until iterations).step(stepSize)
        val locks = List(newPolynomial.size) { ReentrantLock() }
        range.forEach { i ->
            executor.execute {
                for (index in i until min(i + stepSize, iterations)) {
                    val firstCoord = index / y.degree
                    val secondCoord = index % y.degree
                    locks[firstCoord + secondCoord].lock()
                    newPolynomial[firstCoord + secondCoord] = (newPolynomial[firstCoord + secondCoord] + get(firstCoord) * y.get(secondCoord)) as T
                    locks[firstCoord + secondCoord].unlock()
                }
            }
        }
        executor.shutdown()
        executor.awaitTermination(60, TimeUnit.DAYS)
        return NumberPolynomial(newPolynomial)
    }

    @Suppress("DuplicatedCode")
    fun karatsuba(y: NumberPolynomial<T>): NumberPolynomial<T> {
        if (degree <= 1 && y.degree <= 1)
            return NumberPolynomial(listOf((get(0) * y.get(0)) as T))
        val cutoff = max(degree, y.degree) / 2
        val xH = higherHalf(cutoff)
        val xL = lowerHalf(cutoff)
        val yH = y.higherHalf(cutoff)
        val yL = y.lowerHalf(cutoff)
        val a = xH.karatsuba(yH)
        val d = xL.karatsuba(yL)
        val e = xH.plus(xL).karatsuba(yH.plus(yL)).minus(a).minus(d)
        return a.shift(cutoff * 2).plus(e.shift(cutoff)).plus(d)
    }

    @Suppress("DuplicatedCode")
    fun recursiveConcurrentKaratsuba(y: NumberPolynomial<T>, executor: ExecutorService): NumberPolynomial<T> {
        if (degree <= 1 && y.degree <= 1)
            return NumberPolynomial(listOf((get(0) * y.get(0)) as T))
        val cutoff = max(degree, y.degree) / 2
        val xH = higherHalf(cutoff)
        val xL = lowerHalf(cutoff)
        val yH = y.higherHalf(cutoff)
        val yL = y.lowerHalf(cutoff)
        val aFuture = executor.submit<NumberPolynomial<T>> {
            xH.karatsuba(yH)
        }
        val dFuture = executor.submit<NumberPolynomial<T>> {
            xL.karatsuba(yL)
        }
        val eFuture = executor.submit<NumberPolynomial<T>> {
            xH.plus(xL).karatsuba(yH.plus(yL))
        }
        val a = aFuture.get()
        val d = dFuture.get()
        val e = eFuture.get().minus(a).minus(d)
        return a.shift(cutoff * 2).plus(e.shift(cutoff)).plus(d)
    }

    fun concurrentKaratsuba(y: NumberPolynomial<T>, threadsNumber: Int): NumberPolynomial<T> {
        val executor = Executors.newFixedThreadPool(threadsNumber)
        val answer = recursiveConcurrentKaratsuba(y, executor)
        executor.shutdown()
        executor.awaitTermination(60, TimeUnit.DAYS)
        return answer
    }

    private fun lowerHalf(degreeCutoff: Int): NumberPolynomial<T> {
        if (degreeCutoff > 0)
            return NumberPolynomial(coefficients.slice(0 until Integer.min(degreeCutoff, degree)))
        return getZeroPolynomial()
    }

    private fun higherHalf(degreeCutoff: Int): NumberPolynomial<T> {
        if (degreeCutoff < coefficients.size)
            return NumberPolynomial(coefficients.slice(degreeCutoff until coefficients.size))
        return getZeroPolynomial()
    }

    private fun shift(shiftDegree: Int): NumberPolynomial<T> {
        return NumberPolynomial(List(shiftDegree) {
            getZero()
        } + coefficients)
    }

    fun plus(polynomial: NumberPolynomial<T>): NumberPolynomial<T> {
        val newPolynomial = List(max(degree, polynomial.degree)) { getZero() }.toMutableList()
        for (i in newPolynomial.indices)
            newPolynomial[i] = (get(i) + polynomial.get(i)) as T
        return NumberPolynomial(newPolynomial)
    }

    fun minus(polynomial: NumberPolynomial<T>): NumberPolynomial<T> {
        val newPolynomial = List(max(degree, polynomial.degree)) { getZero() }.toMutableList()
        for (i in newPolynomial.indices)
            newPolynomial[i] = (get(i) - polynomial.get(i)) as T
        return NumberPolynomial(newPolynomial)
    }

    fun get(index: Int): T {
        if (index < coefficients.size)
            return coefficients[index]
        return getZero()
    }

    override fun toString(): String {
        return coefficients.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is NumberPolynomial<*>)
            return false
        if (coefficients.size != other.coefficients.size)
            return false
        for (i in coefficients.indices) {
            if (coefficients[i] != other.coefficients[i] && other.coefficients[i] != coefficients[i])
                return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = coefficients.hashCode()
        result = 31 * result + degree
        return result
    }
}

operator fun Number.plus(other: Number): Number {
    return when(this) {
        is Long -> this + (other as Long)
        is Float -> this + (other as Float)
        is Int -> this + (other as Int)
        is Double -> this + (other as Double)
        is BigInt -> this.plus(other as BigInt)
        else -> throw Exception()
    }
}

operator fun Number.minus(other: Number): Number {
    return when(this) {
        is Long -> this - (other as Long)
        is Float -> this - (other as Float)
        is Int -> this - (other as Int)
        is Double -> this - (other as Double)
        is BigInt -> this.minus(other as BigInt)
        else -> throw Exception()
    }
}

operator fun Number.times(other: Number): Number {
    return when(this) {
        is Long -> this * (other as Long)
        is Float -> this * (other as Float)
        is Int -> this * (other as Int)
        is Double -> this * (other as Double)
        is BigInt -> this.times(other as BigInt)
        else -> throw Exception()
    }
}
