import kotlin.math.max

class BigInt(value: Int): Number() {
    private var digits: List<Byte>
    private val base: Byte = 10

    init {
        digits = mutableListOf()
        var valueCopy = value
        while (valueCopy != 0) {
            (digits as MutableList).add((valueCopy % base).toByte())
            valueCopy /= base
        }
        if (digits.isEmpty())
            (digits as MutableList).add(0)
    }

    constructor(digits: List<Byte>) : this(0) {
        this.digits = digits.toMutableList()
        while (this.digits.isNotEmpty() && this.digits.last() == 0.toByte())
            (this.digits as MutableList).removeLast()
    }

    fun plus(other: BigInt): BigInt {
        val newDigits = mutableListOf<Byte>()
        var i = 0
        val n = max(digits.size, other.digits.size)
        var rem: Byte = 0
        while (i < n) {
            var current: Byte = rem
            if (i < digits.size)
                current = (current + digits[i]).toByte()
            if (i < other.digits.size)
                current = (current + other.digits[i]).toByte()
            rem = (current/base).toByte()
            current = (current % base).toByte()
            newDigits.add(current)
            ++i
        }
        if (rem != 0.toByte())
            newDigits.add(rem)
        return BigInt(newDigits)
    }

    fun minus(other: BigInt): BigInt {
        val newDigits = mutableListOf<Byte>()
        var i = 0
        val n = max(digits.size, other.digits.size)
        var rem: Byte = 0
        while (i < n) {
            var current: Byte = rem
            if (i < digits.size)
                current = (current + digits[i]).toByte()
            if (i < other.digits.size)
                current = (current - other.digits[i]).toByte()
            rem = if (current < 0) -1 else 0
            if (current < 0)
                current = (current + 10).toByte()
            newDigits.add(current)
            ++i
        }
        return BigInt(newDigits)
    }

    fun times(other: BigInt): BigInt {
        val newDigits = List(digits.size + other.digits.size+1){0}.toMutableList()
        for (i in digits.indices)
            for (j in other.digits.indices)
                newDigits[i+j] += digits[i] * other.digits[j]
        var i = 0
        while (i < newDigits.size-1) {
            newDigits[i+1] += newDigits[i]/base
            newDigits[i] = newDigits[i] % base
            ++i
        }
        return BigInt(newDigits.map { it.toByte() })
    }

    override fun toByte(): Byte {
        throw Exception("Unused method")
    }

    override fun toChar(): Char {
        throw Exception("Unused method")
    }

    override fun toDouble(): Double {
        throw Exception("Unused method")
    }

    override fun toFloat(): Float {
        throw Exception("Unused method")
    }

    override fun toInt(): Int {
        var ans = 0
        for (digit in digits.reversed())
            ans = ans * 10 + digit
        return ans
    }

    override fun toLong(): Long {
        throw Exception("Unused method")
    }

    override fun toShort(): Short {
        throw Exception("Unused method")
    }

    override fun toString(): String {
        return digits.reversed().joinToString("") { it.toString() }
    }

    override fun equals(other: Any?): Boolean {
        return when(other) {
            is BigInt -> digits == other.digits
            is Int -> toInt() == other
            else -> false
        }
    }
}
