import java.io.File

class Grammar {
    companion object {
        const val enrichedGrammarStartingSymbol = "S0"
    }

    val nonTerminals: List<String>
    val terminals: List<String>
    val startingSymbol: String
    val productionSet: ProductionSet
    val isEnriched: Boolean

    private constructor(nonTerminals: List<String>, terminals: List<String>, startingSymbol: String, productionSet: ProductionSet) {
        this.nonTerminals = nonTerminals
        this.terminals = terminals
        this.startingSymbol = startingSymbol
        this.productionSet = productionSet
        this.isEnriched = true
    }

    constructor(filename: String) {
        val lines = File(filename).readLines()
        nonTerminals = lines[0].split(Regex(" +"))
        terminals = lines[1].split(Regex(" +"))
        startingSymbol = lines[2].trim()
        productionSet = ProductionSet()
        for (i in 3 until lines.size) {
            val lhs = lines[i].substringBefore("->").trim().split(" ")
            val rhs = lines[i].substringAfter("->").trim().split(" ")
                .filter { it != "epsilon" }
            productionSet.addProduction(lhs, rhs)
        }
        this.isEnriched = false
    }


    fun checkCFG(): Boolean {
        return productionSet.getAllProductions().all {
            it.key.size == 1
        }
    }

    fun getEnrichedGrammar(): Grammar {
        if (isEnriched)
            throw Exception("Grammar already enriched!")
        val newGrammar = Grammar(nonTerminals.toMutableList(), terminals.toList(), enrichedGrammarStartingSymbol, productionSet.copy())
        (newGrammar.nonTerminals as MutableList).add(enrichedGrammarStartingSymbol)
        newGrammar.productionSet.addProduction(listOf(enrichedGrammarStartingSymbol), listOf(startingSymbol))
        return newGrammar
    }
}
