import java.util.concurrent.TimeUnit

fun main() {
    val l1 = List(3000){1}
    val l2 = List(3000){1}
    val p1 = NumberPolynomial(l1)
    val p2 = NumberPolynomial(l2)
    val p1b = NumberPolynomial(l1.map {
        BigInt(it)
    })
    val p2b = NumberPolynomial(l2.map {
        BigInt(it)
    })
    val k = timeMethod("Simple Product") {
        p1.simpleProduct(p2)
    }
    val csp = timeMethod("Karatsuba") {
        p1.karatsuba(p2)
    }
    val ck = timeMethod("Concurrent Simple Product") {
        p1.concurrentSimpleProduct(p2, 12)
    }
    val sp = timeMethod("Concurrent Karatsuba") {
        p1.concurrentKaratsuba(p2, 12)
    }
    println()
    val spB = timeMethod("Simple Product Big Int") {
        p1b.simpleProduct(p2b)
    }
    val kB = timeMethod("Karatsuba Big Int") {
        p1b.karatsuba(p2b)
    }
    val cspB = timeMethod("Concurrent Simple Product Big Int") {
        p1b.concurrentSimpleProduct(p2b, 12)
    }
    val ckB = timeMethod("Concurrent Karatsuba Big Int") {
        p1b.concurrentKaratsuba(p2b, 12)
    }
    if (sp != k)
        println("Wrong Karatsuba")
    if (sp != csp)
        println("Wrong concurrent simple product")
    if (sp != ck)
        println("Wrong concurrent karatsuba")
    if (spB != kB)
        println("Wrong Karatsuba Big Int")
    if (spB != cspB)
        println("Wrong concurrent simple product Big Int")
    if (spB != ckB)
        println("Wrong concurrent karatsuba Big Int")
}

fun timeMethod(methodName: String, method: () -> NumberPolynomial<*>): NumberPolynomial<*> {
    val begin = System.nanoTime()
    val result = method()
    val end = System.nanoTime()
    println("$methodName took ${TimeUnit.MILLISECONDS.convert(end - begin, TimeUnit.NANOSECONDS) / 1000.0} seconds")
    return result
}
