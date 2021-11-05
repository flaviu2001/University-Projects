import java.io.File

fun getBackCode(scanner: Scanner, tokens: List<String>) {
    for (pair in scanner.pif)
        if (pair.first >= 0)
            print("${tokens[pair.first]} ")
        else if (pair.first == -1)
            print("${scanner.symbolTable.getIdentifier(pair.second.first, pair.second.second)} ")
        else if (pair.first == -2)
            print("${scanner.symbolTable.getIntConstant(pair.second.first, pair.second.second)} ")
        else if (pair.first == -3)
            print("\"${scanner.symbolTable.getStringConstant(pair.second.first, pair.second.second)}\" ")
    println()
}

fun main() {
    val program = File("src/main/resources/p1.in").readText()
    val tokens = File("src/main/resources/token.in").readLines()
    val scanner = Scanner(program, tokens)
    try {
        scanner.scan()
    } catch (exception: ScannerException) {
        println("${exception.message}\nline: ${exception.code}")
        return
    }
    getBackCode(scanner, tokens)
    File("src/main/resources/PIF.out").printWriter().use { writer ->
        for (pair in scanner.pif)
            writer.println("${pair.first} (${pair.second.first}, ${pair.second.second})")
    }
    File("src/main/resources/ST.out").printWriter().use { writer ->
        writer.println("${scanner.symbolTable.size}\n")
        writer.println("IDENTIFIERS")
        for (i in 0 until scanner.symbolTable.size)
            for (j in 0 until scanner.symbolTable.identifierHashTable.buckets[i].size)
                writer.println("$i, $j: ${scanner.symbolTable.identifierHashTable.buckets[i][j]}")
        writer.println()
        writer.println("INT CONSTANTS")
        for (i in 0 until scanner.symbolTable.size)
            for (j in 0 until scanner.symbolTable.intConstantsHashTable.buckets[i].size)
                writer.println("$i, $j: ${scanner.symbolTable.intConstantsHashTable.buckets[i][j]}")
        writer.println()
        writer.println("STRING CONSTANTS")
        for (i in 0 until scanner.symbolTable.size)
            for (j in 0 until scanner.symbolTable.stringConstantsHashTable.buckets[i].size)
                writer.println("$i, $j: ${scanner.symbolTable.stringConstantsHashTable.buckets[i][j]}")
        writer.println()
    }
}

