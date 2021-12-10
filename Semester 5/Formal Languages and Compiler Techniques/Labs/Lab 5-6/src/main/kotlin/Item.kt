data class Item(val lhs: String, val rhs: List<String>, val dotPosition: Int) {
    override fun toString(): String {
        val rhs1 = rhs.slice(0 until dotPosition).joinToString("")
        val rhs2 = rhs.slice(dotPosition until rhs.size).joinToString("")
        return "$lhs -> $rhs1.$rhs2"
    }
}
