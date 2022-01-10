package pisicutele;

import mpi.*;

public class Main {

    public static void main(String[] args) {
        MPI.Init(args);

        int id = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        Graph graph = new Graph(5);
        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(2,3);
        graph.addEdge(3,4);
        graph.addEdge(4,0);
        graph.addEdge(2,0);
        graph.addEdge(0,4);
        graph.addEdge(4,3);
        graph.addEdge(3,1);
        graph.addEdge(1,4);

        Colors colors = new Colors(3);
        colors.setColorName(0, "red");
        colors.setColorName(1, "green");
        colors.setColorName(2, "blue");

        if (id == 0) {
            System.out.println("MAIN");

            try {
                System.out.println(GraphColoring.graphColoringMain(size, graph, colors));
            } catch (Exception gce) {
                System.out.println(gce.getMessage());
            }
        }
        else {
            System.out.println("Process " + id);

            int codesNo = colors.getCntColors();
            GraphColoring.graphColoringWorker(id, size, graph, codesNo);
        }

        MPI.Finalize();
    }
}
