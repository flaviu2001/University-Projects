import kotlin.math.abs

class HashTable<T>(private var size: Int = 107) {
    var buckets: Array<MutableList<T>> = Array(size) { mutableListOf() }

    private fun hash(element: T): Int {
        val value = element.hashCode() % size
        return abs(value)
    }

    fun findByPair(posInBucket: Int, posInList: Int): T {
        if (posInBucket !in 0 until size)
            throw Exception("Invalid position given")
        if (posInList !in 0 until buckets[posInBucket].size)
            throw Exception("Invalid position given")
        return buckets[posInBucket][posInList]
    }

    fun get(element: T): Pair<Int, Int>? {
        val posInBucket = hash(element)
        for ((posInList, elementInBucket) in buckets[posInBucket].withIndex()) {
            if (elementInBucket == element)
                return Pair(posInBucket, posInList)
        }
        return null
    }

    fun add(element: T): Pair<Int, Int> {
        val lookup = get(element)
        if (lookup != null)
            return lookup
        val posInBucket = hash(element)
        val posInList = buckets[posInBucket].size
        buckets[posInBucket].add(element)
        return Pair(posInBucket, posInList)
    }
}