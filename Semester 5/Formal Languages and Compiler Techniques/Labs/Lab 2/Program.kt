fun main() {
    val st = SymbolTable()
    st.addIdentifier("a")
    st.addIntConstant(2)
    st.addStringConstant("b")
    println(st.hasIdentifier("a"))
    println(st.hasIdentifier("b"))
    println(st.hasStringConstant("a"))
    println(st.hasStringConstant("b"))
    println(st.hasIntConstant(1))
    println(st.hasIntConstant(2))
}