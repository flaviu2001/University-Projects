from graph import *


class UI:
    def __init__(self, type_of_graph):
        self.type_of_graph = type_of_graph
        self.graph = None

    def empty_graph(self):
        self.graph = self.type_of_graph()
        print("Done!")

    def n_graph(self):
        n = input("How many vertices do you need: ")
        try:
            self.graph = self.type_of_graph(int(n))
            print("Done!")
        except Exception as e:
            print(e)

    def m_graph(self):
        n = input("How many vertices do you need: ")
        m = input("How many edges do you need: ")
        try:
            self.graph = self.type_of_graph(int(n), int(m))
            print("Done!")
        except Exception as e:
            print(e)

    def add_vertex(self):
        n = input("Type the vertex you wish to add: ")
        try:
            self.graph.add_vertex(int(n))
        except Exception as e:
            print(e)

    def add_edge(self):
        v1 = input("Type the first vertex of the edge: ")
        v2 = input("Type the second vertex of the edge: ")
        c = input("Type the cost of the edge: ")
        try:
            self.graph.add_edge(int(v1), int(v2), int(c))
        except Exception as e:
            print(e)

    def rem_vertex(self):
        n = input("Type the vertex you wish to remove: ")
        try:
            self.graph.remove_vertex(int(n))
        except Exception as e:
            print(e)

    def rem_edge(self):
        v1 = input("Type the first vertex of the edge: ")
        v2 = input("Type the second vertex of the edge: ")
        try:
            self.graph.remove_edge(int(v1), int(v2))
        except Exception as e:
            print(e)

    def change_edge(self):
        v1 = input("Type the first vertex of the edge: ")
        v2 = input("Type the second vertex of the edge: ")
        c = input("Type the cost of the edge: ")
        try:
            self.graph.set_edge_cost(int(v1), int(v2), int(c))
        except Exception as e:
            print(e)

    def print_edge(self):
        v1 = input("Type the first vertex of the edge: ")
        v2 = input("Type the second vertex of the edge: ")
        try:
            print("The cost of the given edge is {0}.".format(self.graph.get_edge_cost(int(v1), int(v2))))
        except Exception as e:
            print(e)

    def in_degree(self):
        n = input("Type the vertex for which you wish to find the in degree: ")
        try:
            print(self.graph.in_degree(int(n)))
        except Exception as e:
            print(e)

    def out_degree(self):
        n = input("Type the vertex for which you wish to find the out degree: ")
        try:
            print(self.graph.out_degree(int(n)))
        except Exception as e:
            print(e)

    def cnt_vertices(self):
        print("There are a total of {0} vertices.".format(self.graph.count_vertices()))

    def cnt_edges(self):
        print("There are a total of {0} edges.".format(self.graph.count_edges()))

    def is_vertex(self):
        n = input("Type the vertex you wish to check: ")
        try:
            if self.graph.is_vertex(int(n)):
                print("The given vertex belongs to the graph.")
            else:
                print("The given vertex does not belong to the graph.")
        except Exception as e:
            print(e)

    def is_edge(self):
        v1 = input("Type the first vertex of the edge: ")
        v2 = input("Type the second vertex of the edge: ")
        try:
            if self.graph.is_edge(int(v1), int(v2)):
                print("The edge does exist.")
            else:
                print("The edge doesn't exist.")
        except Exception as e:
            print(e)

    def print_vertex_list(self):
        for node in self.graph.vertices_iterator():
            print(node, end=" ")
        print()

    def print_neighbour_list(self):
        n = input("Type the vertex you wish to find neighbours for: ")
        try:
            anyone = False
            for node in self.graph.neighbours_iterator(int(n)):
                print(node, end=" ")
                anyone = True
            if not anyone:
                print("Vertex {0} has no neighbours.".format(n))
            else:
                print()
        except Exception as e:
            print(e)

    def print_transpose_list(self):
        n = input("Type the vertex you wish to find inbound neighbours for: ")
        try:
            anyone = False
            for node in self.graph.transpose_iterator(int(n)):
                print(node, end=" ")
                anyone = True
            if not anyone:
                print("Vertex {0} has no inbound neighbours.".format(n))
            else:
                print()
        except Exception as e:
            print(e)

    def print_edges(self):
        anyone = False
        for triple in self.graph.edges_iterator():
            print("Vertices {0}, {1} and cost {2}.".format(triple[0], triple[1], triple[2]))
            anyone = True
        if not anyone:
            print("No edges in the graph.")

    def read_file(self):
        path = input("Type the file from which you wish to read: ")
        try:
            self.graph = read_file(path)
        except Exception as e:
            print(e)

    def write_file(self):
        path = input("Type the file you wish to write to: ")
        try:
            write_file(path, self.graph)
        except Exception as e:
            print(e)

    def start(self):
        commands = {"1": self.empty_graph,
                    "2": self.n_graph,
                    "3": self.m_graph,
                    "4": self.add_vertex,
                    "5": self.add_edge,
                    "6": self.rem_vertex,
                    "7": self.rem_edge,
                    "8": self.change_edge,
                    "9": self.print_edge,
                    "10": self.in_degree,
                    "11": self.out_degree,
                    "12": self.cnt_vertices,
                    "13": self.cnt_edges,
                    "14": self.is_vertex,
                    "15": self.is_edge,
                    "16": self.print_vertex_list,
                    "17": self.print_neighbour_list,
                    "18": self.print_transpose_list,
                    "19": self.print_edges,
                    "20": self.read_file,
                    "21": self.write_file}
        while True:
            print("1. Generate an empty graph")
            print("2. Generate a graph with n vertices")
            print("3. Generate a graph with n vertices and m random edges")
            print("4. Add a vertex")
            print("5. Add an edge")
            print("6. Remove a vertex")
            print("7. Remove an edge")
            print("8. Change the cost of an edge")
            print("9. Print the cost of an edge")
            print("10. Print the in degree of a vertex")
            print("11. Print the out degree of a vertex")
            print("12. Print the number of vertices")
            print("13. Print the number of edges")
            print("14. Check whether a vertex belongs to the graph")
            print("15. Check whether an edge belongs to the graph")
            print("16. Print the list of vertices")
            print("17. Print the list of outbound neighbours of a vertex")
            print("18. Print the list of inbound neighbours of a vertex")
            print("19. Print the list of edges")
            print("20. Reads the graph from a file")
            print("21. Writes the graph to a file")
            print("0. Exit")
            cmd = input("> ")
            if cmd in commands:
                commands[cmd]()
            elif cmd == "0":
                break
            else:
                print("Invalid choice.")


UI(Graph).start()
