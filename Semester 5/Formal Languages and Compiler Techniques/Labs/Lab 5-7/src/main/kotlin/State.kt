data class State(val items: Set<Item>) {
    val stateType = if (items.size == 1 && items.first().rhs.size == items.first().dotPosition && items.first().lhs == Grammar.enrichedGrammarStartingSymbol)
        StateType.ACCEPT
    else if (items.size == 1 && items.first().rhs.size == items.first().dotPosition)
        StateType.REDUCE
    else if (items.isNotEmpty() && items.all { it.rhs.size > it.dotPosition })
        StateType.SHIFT
    else if (items.size > 1 && items.all { it.rhs.size == it.dotPosition })
        StateType.REDUCE_REDUCE_CONFLICT
    else StateType.SHIFT_REDUCE_CONFLICT

    fun getSymbolsSucceedingTheDot(): List<String> {
        val symbols = mutableSetOf<String>()
        for (item in items)
            if (item.dotPosition in item.rhs.indices)
                symbols.add(item.rhs[item.dotPosition])
        return symbols.toList()
    }

    override fun toString(): String {
        return "$stateType - $items"
    }
}