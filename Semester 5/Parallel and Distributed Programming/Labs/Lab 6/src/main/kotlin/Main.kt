import java.io.File

fun readGraph(filename: String): Graph {
    val file = File(filename).bufferedReader()
    val line = file.readLine()
    val n = line.toInt()
    val edgeList = mutableListOf<Pair<Int, Int>>()
    file.readLines().forEach {
        val tokens = it.split(" ")
        val a = tokens[0].toInt()
        val b = tokens[1].toInt()
        edgeList.add(Pair(a, b))
    }
    return Graph(n, edgeList)
}

fun main() {
//    val graph = readGraph("src/main/resources/graph.in")
    val graph = Graph.getRandomGraph(30, 350)
    println(HamiltonianFinder(graph).getList())
    println(HamiltonianFinder(graph).getListConcurrently())
}