class CanonicalCollection {
    private val mutableStates = mutableListOf<State>()
    val states: List<State> = mutableStates
    private val mutableAdjacencyList = mutableMapOf<Pair<Int, String>, Int>()
    val adjacencyList: Map<Pair<Int, String>, Int> = mutableAdjacencyList

    fun addState(state: State) {
        mutableStates.add(state)
    }

    fun connectStates(indexFirstState: Int, symbol: String, indexSecondState: Int) {
        mutableAdjacencyList[Pair(indexFirstState, symbol)] = indexSecondState
    }

}