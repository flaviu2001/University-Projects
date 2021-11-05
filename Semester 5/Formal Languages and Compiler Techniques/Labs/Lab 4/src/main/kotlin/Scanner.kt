import automaton.FiniteAutomaton

class Scanner(private val program: String, private val tokens: List<String>) {
    val symbolTable = SymbolTable()
    val pif = mutableListOf<Pair<Int, Pair<Int, Int>>>()
    private var index = 0
    private var currentLine = 1

    private fun skipWhitespace() {
        while (index < program.length && program[index].isWhitespace()) {
            if (program[index] == '\n')
                ++currentLine
            ++index
        }
    }

    private fun skipComment() {
        if (program.startsWith("//", index)) {
            while (index < program.length && program[index] != '\n')
                ++index
            return
        }
    }

    private fun treatStringConstant(): Boolean {
        val regex = Regex("^\"([a-zA-z0-9_ ]*)\"").find(program.substring(index))
        if (regex == null) {
            if (Regex("^\"[^\"]\"").containsMatchIn(program.substring(index)))
                throw ScannerException("Lexical error: Invalid characters inside string", currentLine)
            if (Regex("^\"").containsMatchIn(program.substring(index)))
                throw ScannerException("Lexical error: Unclosed quotes", currentLine)
            return false
        }
        val stringConstant = regex.groups[1]!!.value
        index += stringConstant.length + 2
        val position = symbolTable.addStringConstant(stringConstant)
        pif.add(Pair(position.positionType.code, position.pair))
        return true
    }

    private fun treatIntConstant(): Boolean {
//        val regex = Regex("^([+-]?[1-9][0-9]*|0)").find(program.substring(index)) ?: return false
//        val intConstant = regex.groups[1]!!.value
//        val regex = Regex("^([+-]?[0-9]*)").find(program.substring(index)) ?: return false
//        val intConstant = regex.groups[1]!!.value
//        if (!FiniteAutomaton("src/main/resources/int_constant.in").checkAccepted(FiniteAutomaton.stringToListOfChars(intConstant)))
//            return false
        val fa = FiniteAutomaton("src/main/resources/int_constant.in")
        val intConstant = fa.getNextAccepted(program.substring(index)) ?: return false
        if (intConstant[0] in listOf('+', '-') && pif.size > 0 && pif.last().first in listOf( // makes it so 1+2 is the tokens 1, + and 2 and not 1 and +2
                PositionType.IDENTIFIER.code,
                PositionType.INT_CONSTANT.code,
                PositionType.STRING_CONSTANT.code
            ))
            return false
        index += intConstant.length
        val parsedIntConstant = intConstant.toInt()
        val position = symbolTable.addIntConstant(parsedIntConstant)
        pif.add(Pair(position.positionType.code, position.pair))
        return true
    }

    private fun treatFromTokenList(): Boolean {
        for ((tokenIndex, token) in tokens.withIndex())
            if (program.startsWith(token, index)) {
                pif.add(Pair(tokenIndex, Position.NullPosition.pair))
                index += token.length
                return true
            }
        return false
    }

    private fun treatIdentifier(): Boolean {
//        val regex = Regex("^([a-zA-Z_][a-zA-Z0-9_]*)").find(program.substring(index)) ?: return false
//        val identifier = regex.groups[1]!!.value
//        val regex = Regex("^([a-zA-Z0-9_]*)").find(program.substring(index)) ?: return false
//        val identifier = regex.groups[1]!!.value
//        if (!FiniteAutomaton("src/main/resources/identifier.in").checkAccepted(FiniteAutomaton.stringToListOfChars(identifier)))
//            return false
        val fa = FiniteAutomaton("src/main/resources/identifier.in")
        val identifier = fa.getNextAccepted(program.substring(index)) ?: return false
        index += identifier.length
        val position = symbolTable.addIdentifier(identifier)
        pif.add(Pair(position.positionType.code, position.pair))
        return true
    }

    private fun nextToken() {
        skipWhitespace()
        skipComment()
        if (index == program.length)
            return
        for (function in listOf(
            Scanner::treatStringConstant,
            Scanner::treatIntConstant,
            Scanner::treatFromTokenList,
            Scanner::treatIdentifier))
            if (function(this))
                return
        throw ScannerException("Lexical error: Cannot classify token", currentLine)
    }

    fun scan() {
        while (index in program.indices)
            nextToken()
    }
}