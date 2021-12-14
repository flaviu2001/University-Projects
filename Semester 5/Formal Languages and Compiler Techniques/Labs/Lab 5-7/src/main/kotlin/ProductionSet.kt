class ProductionSet {
    private val productions: MutableMap<List<String>, MutableList<List<String>>>

    constructor() {
        productions = mutableMapOf()
    }

    private constructor(productions: MutableMap<List<String>, MutableList<List<String>>>) {
        this.productions = productions
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun getProductionsOf(lhs: List<String>): List<List<String>> {
        return productions[lhs] ?: emptyList()
    }

    fun getProductionsOf(lhs: String) = getProductionsOf(listOf(lhs))

    fun getAllProductions() = productions

    fun addProduction(lhs: List<String>, rhs: List<String>) {
        if (!productions.containsKey(lhs))
            productions[lhs] = mutableListOf()
        productions[lhs]!!.add(rhs)
    }

    fun copy(): ProductionSet {
        return ProductionSet(productions.toMutableMap())
    }

    override fun toString(): String {
        return productions.toString()
    }

    fun getOrderedProductions(): List<Pair<String, List<String>>> {
        val productionList = mutableListOf<Pair<String, List<String>>>()
        productions.forEach {
            it.value.forEach { it2 ->
                productionList.add(Pair(it.key[0], it2))
            }
        }
        return productionList
    }
}