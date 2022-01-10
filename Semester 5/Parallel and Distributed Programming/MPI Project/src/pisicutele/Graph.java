package pisicutele;

import java.util.*;

public class Graph {
    private final int cntNodes;
    private final List<Set<Integer>> adjacencyList;

    public Graph(int cntNodes) {
        this.cntNodes = cntNodes;

        adjacencyList = new ArrayList<>();
        for (int node = 0; node < cntNodes; node++) {
            adjacencyList.add(new HashSet<>());
        }
    }

    public void addEdge(int fromVertex, int toVertex) {
        adjacencyList.get(fromVertex).add(toVertex);
        adjacencyList.get(toVertex).add(fromVertex);
    }

    public boolean isEdge(int fromVertex, int toVertex) {
        return adjacencyList.get(fromVertex).contains(toVertex);
    }

    public int getCntNodes() {
        return cntNodes;
    }
}
