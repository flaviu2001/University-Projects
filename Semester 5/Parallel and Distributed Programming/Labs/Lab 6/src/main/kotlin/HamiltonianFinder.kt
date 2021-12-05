import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

class HamiltonianFinder(private val graph: Graph, private val startingNode: Int = 0) {
    private var visited = LinkedHashSet<Int>()
    private var resultPath = mutableListOf<Int>()
    private var lock = ReentrantLock()
    private var executor: ExecutorService? = null
    private val maxDepth: Int = 2

    private constructor(graph: Graph,
                        startingNode: Int,
                        visited: LinkedHashSet<Int>,
                        resultPath: MutableList<Int>,
                        lock: ReentrantLock,
                        executor: ExecutorService?) : this(graph, startingNode) {
        this.visited = visited
        this.resultPath = resultPath
        this.lock = lock
        this.executor = executor
    }

    private fun visitNodeConcurrently(node: Int, depth: Int = 0) {
        lock.lock()
        if (resultPath.isNotEmpty()) {
            lock.unlock()
            return
        }
        lock.unlock()
        if (depth < maxDepth) {
            visited.add(node)
            if (visited.size != graph.nodesCount) {
                val futures = mutableListOf<Future<*>>()
                graph.getNeighbours(node)
                    .filter { it !in visited }
                    .forEach {
                        futures.add(executor!!.submit {
                            this.copy().visitNodeConcurrently(it, depth+1)
                        })
                    }
                for (future in futures)
                    future.get()
            } else if (graph.getNeighbours(node).contains(startingNode)) {
                lock.lock()
                resultPath.addAll(visited)
                lock.unlock()
            }
        } else {
            this.copy().visitNode(node)
        }
    }

    fun getListConcurrently(threadsNumber: Int = 12): List<Int> {
        executor = Executors.newFixedThreadPool(threadsNumber)
        resultPath = mutableListOf()
        visitNodeConcurrently(startingNode)
        executor!!.shutdown()
        executor!!.awaitTermination(60, TimeUnit.DAYS)
        return resultPath
    }

    private fun visitNode(node: Int) {
        lock.lock()
        if (resultPath.isNotEmpty()) {
            lock.unlock()
            return
        }
        lock.unlock()
        visited.add(node)
        if (visited.size != graph.nodesCount)
            graph.getNeighbours(node)
                .filter { it !in visited }
                .forEach {
                    visitNode(it)
                }
        else if (graph.getNeighbours(node).contains(startingNode)) {
            lock.lock()
            resultPath.addAll(visited)
            lock.unlock()
        }
        visited.remove(node)
    }

    fun getList(): List<Int> {
        resultPath = mutableListOf()
        visitNode(startingNode)
        return resultPath
    }

    private fun copy(): HamiltonianFinder {
        val newVisited = LinkedHashSet<Int>()
        for (node in visited)
            newVisited.add(node)
        return HamiltonianFinder(graph, startingNode, newVisited, resultPath, lock, executor)
    }
}
