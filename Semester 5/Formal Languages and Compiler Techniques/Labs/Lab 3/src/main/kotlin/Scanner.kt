class Scanner(private val program: String, private val tokens: List<String>) {
    val symbolTable = SymbolTable()
    val pif = mutableListOf<Pair<Int, Pair<Int, Int>>>()
    private var index = 0
    private var currentLine = 1

    private fun nextToken() {
        while (index < program.length && program[index].isWhitespace()) {
            if (program[index] == '\n')
                ++currentLine
            ++index
        }
        if (program.startsWith("//", index)) { //skip comments
            while (index < program.length && program[index] != '\n')
                ++index
            return
        }
        if (index == program.length)
            return
        if (program[index] == '"') {
            ++index
            var stringConstant = ""
            while (index < program.length && program[index] != '"') {
                stringConstant += program[index]
                ++index
            }
            if (index == program.length)
                throw ScannerException("Lexical error: Unclosed quotes", currentLine)
            ++index
            val position = symbolTable.addStringConstant(stringConstant)
            pif.add(Pair(position.positionType.code, position.pair))
            return
        }
        if (program[index].isDigit() || (program[index] == '-' && index+1 < program.length && program[index+1].isDigit())) {
            var number = 0
            var sign = 1
            if (program[index] == '-') {
                sign = -1
                ++index
            }
            while (index < program.length && program[index].isDigit()) {
                number = number*10 + program[index].toString().toInt()
                ++index
            }
            number *= sign
            val position = symbolTable.addIntConstant(number)
            pif.add(Pair(position.positionType.code, position.pair))
            return
        }
        for ((tokenIndex, token) in tokens.withIndex()) //what about the token + and the number +3? I think I won't allow +3
            if (program.startsWith(token, index)) {
                pif.add(Pair(tokenIndex, Position.NullPosition.pair))
                index += token.length
                return
            }
        if (program[index].isLetter() || program[index] == '_') {
            var identifier = ""
            while (index < program.length && (program[index].isLetterOrDigit() || program[index] == '_')) {
                identifier += program[index]
                ++index
            }
            val position = symbolTable.addIdentifier(identifier)
            pif.add(Pair(position.positionType.code, position.pair))
            return
        }
        throw ScannerException("Lexical error: Cannot classify token", currentLine)
    }

    fun scan() {
        while (index in program.indices)
            nextToken()
    }
}