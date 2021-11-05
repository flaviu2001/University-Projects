package automaton

fun main() {
    val fa = FiniteAutomaton("src/main/resources/int_constant.in")
    println("1. Print states")
    println("2. Print alphabet")
    println("3. Print output states")
    println("4. Print in state")
    println("5. Print transitions")
    println("6. Check word with varying length letters")
    println("7. Check word with length 1 letters")
    println("8. Exit")
    while (true) {
        print("> ")
        when(readLine()!!.toInt()) {
            1 -> fa.printStates()
            2 -> fa.printAlphabet()
            3 -> fa.printOutStates()
            4 -> fa.printInState()
            5 -> fa.printTransitions()
            6 -> {
                print("Number of letters in the word: ")
                val n = readLine()!!.toInt()
                val listOfLetters = mutableListOf<String>()
                for (i in 1..n) {
                    print("> ")
                    val letter = readLine()
                    listOfLetters.add(letter!!)
                }
                println(fa.checkAccepted(listOfLetters))
            }
            7 -> println(fa.checkAccepted(FiniteAutomaton.stringToListOfChars(readLine()!!)))
            8 -> break
        }
    }
}
