fun main() {
    val g = Grammar("src/main/resources/g1.in")
    println("Nonterminals: ${g.nonTerminals}")
    println("Terminals: ${g.terminals}")
    println("Starting symbol: ${g.startingSymbol}")
    println("Production set: ${g.productionSet}")
    println(if (g.checkCFG()) "It is a cfg" else "It is not a cfg")

    val lr = LR(g)
    val canonicalCollection = lr.canonicalCollection()
    println("States")
    for (i in canonicalCollection.states.indices)
        println("$i: ${canonicalCollection.states[i]}")
    println("State transitions")
    for (pair in canonicalCollection.adjacencyList)
        println("${pair.key} -> ${pair.value}")
}
