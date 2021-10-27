class HashTable<T>(private var size: Int = 107) {
    private var buckets: Array<MutableList<T>> = Array(size) { mutableListOf() }

    fun findByPair(posInBucket: Int, posInList: Int): T {
        if (posInBucket !in 0 until size)
            throw Exception("Invalid position given")
        if (posInList !in 0 until buckets[posInBucket].size)
            throw Exception("Invalid position given")
        return buckets[posInBucket][posInList]
    }

    fun get(element: T): Pair<Int, Int>? {
        val posInBucket = element.hashCode() % size
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
        val posInBucket = element.hashCode() % size
        val posInList = buckets[posInBucket].size
        buckets[posInBucket].add(element)
        return Pair(posInBucket, posInList)
    }
}