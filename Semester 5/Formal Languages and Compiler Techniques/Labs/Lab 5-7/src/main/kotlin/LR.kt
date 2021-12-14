class LR(private val grammar: Grammar) {
    private fun getDotPrecededNonTerminal(item: Item): String? {
        val term = item.rhs.getOrNull(item.dotPosition)
        if (term !in grammar.nonTerminals) {
            return null
        }
        return term
    }

    private val workingGrammar = if (grammar.isEnriched) grammar else grammar.getEnrichedGrammar()
    private val orderedProductions = workingGrammar.productionSet.getOrderedProductions()

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
        val canonicalCollection = CanonicalCollection()
        canonicalCollection.addState(
            closure(
                Item(
                    workingGrammar.startingSymbol,
                    workingGrammar.productionSet.getProductionsOf(workingGrammar.startingSymbol)[0],
                    0
                )
            )
        )
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

    fun getParsingTable(): Table {
        val canonicalCollection = canonicalCollection()
        val table = Table(mutableMapOf())
        canonicalCollection.adjacencyList.forEach {
            val state = canonicalCollection.states[it.key.first]
            if (it.key.first !in table.tableRow) {
                table.tableRow[it.key.first] = Row(state.stateType, mutableMapOf(), null)
            }
            table.tableRow[it.key.first]!!.goto!![it.key.second] = it.value
        }
        for ((index, state) in canonicalCollection.states.withIndex()) {
            if (state.stateType == StateType.REDUCE) {
                table.tableRow[index] = Row(
                    state.stateType,
                    null,
                    orderedProductions.indexOf(Pair(state.items.first().lhs, state.items.first().rhs))
                )
            }
            if (state.stateType == StateType.ACCEPT) {
                table.tableRow[index] = Row(
                    state.stateType,
                    null,
                    null
                )
            }
        }
        return table
    }

    fun parse(word: List<String>): List<ParsingTreeRow> {
        val workingStack = mutableListOf<Pair<String, Int>>()
        val remainingStack = word.toMutableList()
        val productionStack = mutableListOf<Int>()
        val parsingTable = getParsingTable()
        workingStack.add(Pair("$", 0))

        val parsingTree = mutableListOf<ParsingTreeRow>()
        val treeStack = mutableListOf<Pair<String, Int>>()
        var currentIndex = 0
        while (remainingStack.isNotEmpty() || workingStack.isNotEmpty()) {
            val tableValue = parsingTable.tableRow[workingStack.last().second]
                ?: throw Exception("Invalid state ${workingStack.last().second} in the working stack")
            when (tableValue.action) {
                StateType.SHIFT -> {
                    val token = remainingStack.firstOrNull() ?: throw Exception("Action is shift but nothing else is left in the remaining stack")
                    val goto = tableValue.goto!!
                    val value = goto[token] ?: throw Exception("Invalid symbol \"$token\" for goto of state ${workingStack.last().second}")
                    workingStack.add(
                        Pair(
                            token,
                            value
                        )
                    )
                    remainingStack.removeFirst()
                    treeStack.add(Pair(token, currentIndex++))
                }
                StateType.ACCEPT -> {
                    val lastElement = treeStack.removeLast()
                    parsingTree.add(ParsingTreeRow(lastElement.second, lastElement.first, -1, -1))
                    return parsingTree
                }
                StateType.REDUCE -> {
                    val productionToReduceTo = orderedProductions[tableValue.reductionIndex!!]
                    val parentIndex = currentIndex++
                    var lastIndex = -1
                    for (j in 0 until productionToReduceTo.second.size) {
                        workingStack.removeLast()
                        val lastElement = treeStack.removeLast()
                        parsingTree.add(
                            ParsingTreeRow(
                                lastElement.second,
                                lastElement.first,
                                parentIndex,
                                lastIndex
                            )
                        )
                        lastIndex = lastElement.second
                    }
                    treeStack.add(Pair(productionToReduceTo.first, parentIndex))
                    val previous = workingStack.last()
                    workingStack.add(
                        Pair(
                            productionToReduceTo.first,
                            parsingTable.tableRow[previous.second]!!.goto!![productionToReduceTo.first]!!
                        )
                    )
                    productionStack.add(0, tableValue.reductionIndex)

                }
                else -> throw Exception(tableValue.action.toString())
            }
        }
        throw Exception("How did you even get here?")
    }
}
