class SymbolTable {
    private val identifierHashTable = HashTable<String>()
    private val intConstantsHashTable = HashTable<Int>()
    private val stringConstantsHashTable = HashTable<String>()

    fun addIdentifier(name: String): Pair<Int, Int> = identifierHashTable.add(name)

    fun addIntConstant(constant: Int): Pair<Int, Int> = intConstantsHashTable.add(constant)

    fun addStringConstant(string: String): Pair<Int, Int> = stringConstantsHashTable.add(string)

    fun hasIdentifier(name: String): Pair<Int, Int>? = identifierHashTable.get(name)

    fun hasIntConstant(constant: Int): Pair<Int, Int>? = intConstantsHashTable.get(constant)

    fun hasStringConstant(string: String): Pair<Int, Int>? = stringConstantsHashTable.get(string)

    fun getIdentifier(posInBucket: Int, posInList: Int) = identifierHashTable.findByPair(posInBucket, posInList)

    fun getIntConstant(posInBucket: Int, posInList: Int) = intConstantsHashTable.findByPair(posInBucket, posInList)

    fun getStringConstant(posInBucket: Int, posInList: Int) = stringConstantsHashTable.findByPair(posInBucket, posInList)
}
