class LR(private val grammar: Grammar) {
    private fun getDotPrecededNonTerminal(item: Item): String? {
        val term = item.rhs.getOrNull(item.dotPosition)
        if (term !in grammar.nonTerminals) {
            return null
        }
        return term
    }

    private fun closure(item: Item): State {
        var oldClosure: MutableSet<Item>
        var currentClosure = mutableSetOf(item)
        do {
            oldClosure = currentClosure.toMutableSet()
            val newClosure = currentClosure.toMutableSet()
            for (it in currentClosure) {
                val nonTerminal = getDotPrecededNonTerminal(it) ?: continue
                for (production in grammar.productionSet.getProductionsOf(nonTerminal)) {
                    val currentItem = Item(nonTerminal, production, 0)
                    newClosure.add(currentItem)
                }
            }
            currentClosure = newClosure
        } while (oldClosure != currentClosure)
        return State(currentClosure)
    }

    private fun goTo(state: State, element: String): State {
        val result = mutableSetOf<Item>()
        for (item in state.items) {
            val nonTerminal = item.rhs.getOrNull(item.dotPosition)
            if (nonTerminal == element) {
                val nextItem = Item(item.lhs, item.rhs, item.dotPosition + 1)
                result.addAll(closure(nextItem).items)
            }
        }
        return State(result)
    }

    fun canonicalCollection(): CanonicalCollection {
        val workingGrammar = if(grammar.isEnriched) grammar else grammar.getEnrichedGrammar()
        val canonicalCollection = CanonicalCollection()
        canonicalCollection.addState(closure(Item(workingGrammar.startingSymbol, workingGrammar.productionSet.getProductionsOf(workingGrammar.startingSymbol)[0], 0)))
        var i = 0
        while (i < canonicalCollection.states.size) {
            for (symbol in canonicalCollection.states[i].getSymbolsSucceedingTheDot()) {
                val newState = goTo(canonicalCollection.states[i], symbol)
                var indexInStates = canonicalCollection.states.indexOf(newState)
                if (indexInStates == -1) {
                    canonicalCollection.addState(newState)
                    indexInStates = canonicalCollection.states.indices.last()
                }
                canonicalCollection.connectStates(i, symbol, indexInStates)
            }
            ++i
        }
        return canonicalCollection
    }
}
