import kotlin.random.Random

class Graph(val nodesCount: Int, edges: List<Pair<Int, Int>>) {
    companion object {
        fun getRandomGraph(nodesCount: Int, edgesCount: Int): Graph {
            val edges = mutableListOf<Pair<Int, Int>>()
            for (i in 0 until edgesCount) {
                var from = Random.nextInt(nodesCount)
                var to = Random.nextInt(nodesCount)
                while (edges.contains(Pair(from, to)) || from == to) {
                    from = Random.nextInt(nodesCount)
                    to = Random.nextInt(nodesCount)
                }
                edges.add(Pair(from, to))
            }
            return Graph(nodesCount, edges)
        }
    }

    private val adjacencyList: Map<Int, Set<Int>>

    init {
        val newAdjacencyList = mutableMapOf<Int, MutableSet<Int>>()
        for (edge in edges) {
            if (edge.first !in newAdjacencyList)
                newAdjacencyList[edge.first] = mutableSetOf(edge.second)
            else
                newAdjacencyList[edge.first]!!.add(edge.second)
        }
        adjacencyList = newAdjacencyList
    }

    fun getNeighbours(node: Int) = adjacencyList[node]?: mutableSetOf()
}
