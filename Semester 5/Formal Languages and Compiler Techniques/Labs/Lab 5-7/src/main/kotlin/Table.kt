class Table(val tableRow: MutableMap<Int, Row>) {
    override fun toString(): String {
        var string = ""
        for ((rowIndex, row) in tableRow.entries.toList().sortedBy { it.key }) {
            string += "$rowIndex: $row\n"
        }
        return string
    }
}
