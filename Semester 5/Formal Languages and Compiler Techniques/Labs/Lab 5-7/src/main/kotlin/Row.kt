data class Row(val action: StateType, val goto: MutableMap<String, Int>?, val reductionIndex: Int?) {
    override fun toString(): String {
        return when (action) {
            StateType.REDUCE -> "REDUCE $reductionIndex"
            StateType.ACCEPT -> "ACCEPT"
            StateType.SHIFT -> "SHIFT $goto"
            else -> throw Exception("No other states allowed")
        }
    }
}
