class SymbolTable(val size: Int = 107) {
    val identifierHashTable = HashTable<String>(size)
    val intConstantsHashTable = HashTable<Int>(size)
    val stringConstantsHashTable = HashTable<String>(size)

    fun addIdentifier(name: String): Position = Position(PositionType.IDENTIFIER, identifierHashTable.add(name))

    fun addIntConstant(constant: Int): Position = Position(PositionType.INT_CONSTANT, intConstantsHashTable.add(constant))

    fun addStringConstant(string: String): Position = Position(PositionType.STRING_CONSTANT, stringConstantsHashTable.add(string))

    fun hasPosition(name: String): Pair<Int, Int>? = identifierHashTable.get(name)

    fun hasIntConstant(constant: Int): Pair<Int, Int>? = intConstantsHashTable.get(constant)

    fun hasStringConstant(string: String): Pair<Int, Int>? = stringConstantsHashTable.get(string)

    fun getIdentifier(posInBucket: Int, posInList: Int) = identifierHashTable.findByPair(posInBucket, posInList)

    fun getIntConstant(posInBucket: Int, posInList: Int) = intConstantsHashTable.findByPair(posInBucket, posInList)

    fun getStringConstant(posInBucket: Int, posInList: Int) = stringConstantsHashTable.findByPair(posInBucket, posInList)
}
