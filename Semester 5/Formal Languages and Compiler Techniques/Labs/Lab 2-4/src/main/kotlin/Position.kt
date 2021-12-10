data class Position(val positionType: PositionType, val pair: Pair<Int, Int>) {
    companion object {
        val NullPosition = Position(PositionType.NONE, Pair(-1, -1))
    }
}
