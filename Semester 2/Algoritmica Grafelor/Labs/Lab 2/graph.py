from exceptions import *
from random import randrange
from copy import deepcopy


def read_file(file_path):
    file = open(file_path, "r")
    n, m = map(int, file.readline().split())
    g = UndirectedGraph(n)
    for _ in range(m):
        vertex1, vertex2, edge_cost = map(int, file.readline().split())
        g.add_edge(vertex1, vertex2, edge_cost)
    file.close()
    return g


def write_file(file_path, g):
    file = open(file_path, "w")
    file.write("{0} {1}\n".format(g.count_vertices(), g.count_edges()))
    for node in g.vertices_iterator():
        for neighbour in g.neighbours_iterator(node):
            file.write("{0} {1} {2}\n".format(node, neighbour, g.get_edge_cost(node, neighbour)))
    file.close()


def random_graph(vertices_no, edges_no):
    g = UndirectedGraph()
    for i in range(vertices_no):
        g.add_vertex(i)
    for j in range(edges_no):
        vertex1 = randrange(vertices_no)
        vertex2 = randrange(vertices_no)
        while g.is_edge(vertex1, vertex2):
            vertex1 = randrange(vertices_no)
            vertex2 = randrange(vertices_no)
        g.add_edge(vertex1, vertex2, randrange(1000000))


class UndirectedGraph:
    def __init__(self, n=0, m=0):
        self._vertices = set()
        self._neighbours = dict()
        self._cost = dict()
        for i in range(n):
            self.add_vertex(i)
        for j in range(m):
            vertex1 = randrange(n)
            vertex2 = randrange(n)
            while self.is_edge(vertex1, vertex2):
                vertex1 = randrange(n)
                vertex2 = randrange(n)
            self.add_edge(vertex1, vertex2, randrange(1000000))

    def vertices_iterator(self):
        """
        Returns an iterator to the set of vertices.
        """
        for vertex in self._vertices:
            yield vertex

    def neighbours_iterator(self, vertex):
        """
        Returns an iterator to the set of (outbound) neighbours of a vertex.
        """
        if not self.is_vertex(vertex):
            raise VertexError("Invalid vertex.")
        for neighbour in self._neighbours[vertex]:
            yield neighbour

    def edges_iterator(self):
        """
        Returns an iterator to the set of edges.
        """
        for key, value in self._cost.items():
            yield key[0], key[1], value

    def is_vertex(self, vertex):
        """
        Returns True if vertex belongs to the graph.
        """
        return vertex in self._vertices

    def is_edge(self, vertex1, vertex2):
        """
        Returns True if the edge from vertex1 to vertex2 belongs to the graph.
        """
        if vertex1 > vertex2:
            vertex1, vertex2 = vertex2, vertex1
        return vertex1 in self._neighbours and vertex2 in self._neighbours[vertex1]

    def count_vertices(self):
        """
        Returns the number of vertices in the graph.
        """
        return len(self._vertices)

    def count_edges(self):
        """
        Returns the number of edges in the graph.
        """
        return len(self._cost)

    def degree(self, vertex):
        """
        Returns the number of edges with the start point vertex.
        """
        if vertex not in self._neighbours:
            raise VertexError("The specified vertex does not exist.")
        return len(self._neighbours[vertex])

    def get_edge_cost(self, vertex1, vertex2):
        """
        Returns the cost of an edge if it exists.
        """
        if vertex1 > vertex2:
            vertex1, vertex2 = vertex2, vertex1
        if (vertex1, vertex2) not in self._cost:
            raise EdgeError("The specified edge does not exist.")
        return self._cost[(vertex1, vertex2)]

    def set_edge_cost(self, vertex1, vertex2, new_cost):
        """
        Sets the cost of an edge in the graph if it exists.
        """
        if vertex1 > vertex2:
            vertex1, vertex2 = vertex2, vertex1
        if (vertex1, vertex2) not in self._cost:
            raise EdgeError("The specified edge does not exist.")
        self._cost[(vertex1, vertex2)] = new_cost

    def add_vertex(self, vertex):
        """
        Adds a vertex to the graph.
        """
        if self.is_vertex(vertex):
            raise VertexError("Cannot add a vertex which already exists.")
        self._vertices.add(vertex)
        self._neighbours[vertex] = set()

    def add_edge(self, vertex1, vertex2, edge_cost=0):
        """
        Adds an edge to the graph.
        """
        if vertex1 > vertex2:
            vertex1, vertex2 = vertex2, vertex1
        if self.is_edge(vertex1, vertex2):
            raise EdgeError("The specified edge already exists")
        if not self.is_vertex(vertex1) or not self.is_vertex(vertex2):
            raise EdgeError("The vertices on the edge do not exist.")
        self._neighbours[vertex1].add(vertex2)
        self._neighbours[vertex2].add(vertex1)
        self._cost[(vertex1, vertex2)] = edge_cost

    def remove_edge(self, vertex1, vertex2):
        """
        Removes an edge from the graph.
        """
        if vertex1 > vertex2:
            vertex1, vertex2 = vertex2, vertex1
        if not self.is_edge(vertex1, vertex2):
            raise EdgeError("The specified edge does not exist.")
        del self._cost[(vertex1, vertex2)]
        self._neighbours[vertex1].remove(vertex2)
        self._neighbours[vertex2].remove(vertex1)

    def remove_vertex(self, vertex):
        """
        Removes a vertex from the graph.
        """
        if not self.is_vertex(vertex):
            raise VertexError("Cannot remove a vertex which doesn't exist.")
        to_remove = []
        for node in self._neighbours[vertex]:
            to_remove.append(node)
        for node in to_remove:
            self.remove_edge(vertex, node)
        del self._neighbours[vertex]
        self._vertices.remove(vertex)

    def copy(self):
        """
        Returns a deepcopy of the graph.
        """
        return deepcopy(self)


class DirectedGraph:
    def __init__(self, n=0, m=0):
        self._vertices = set()
        self._neighbours = dict()
        self._transpose = dict()
        self._cost = dict()
        for i in range(n):
            self.add_vertex(i)
        for j in range(m):
            vertex1 = randrange(n)
            vertex2 = randrange(n)
            while self.is_edge(vertex1, vertex2):
                vertex1 = randrange(n)
                vertex2 = randrange(n)
            self.add_edge(vertex1, vertex2, randrange(1000000))

    def vertices_iterator(self):
        """
        Returns an iterator to the set of vertices.
        """
        for vertex in self._vertices:
            yield vertex

    def neighbours_iterator(self, vertex):
        """
        Returns an iterator to the set of (outbound) neighbours of a vertex.
        """
        if not self.is_vertex(vertex):
            raise VertexError("Invalid vertex.")
        for neighbour in self._neighbours[vertex]:
            yield neighbour

    def transpose_iterator(self, vertex):
        """
        Returns an iterator to the set of (inbound) neighbours of a vertex.
        """
        if not self.is_vertex(vertex):
            raise VertexError("Invalid vertex.")
        for neighbour in self._transpose[vertex]:
            yield neighbour

    def edges_iterator(self):
        """
        Returns an iterator to the set of edges.
        """
        for key, value in self._cost.items():
            yield key[0], key[1], value

    def is_vertex(self, vertex):
        """
        Returns True if vertex belongs to the graph.
        """
        return vertex in self._vertices

    def is_edge(self, vertex1, vertex2):
        """
        Returns True if the edge from vertex1 to vertex2 belongs to the graph.
        """
        return vertex1 in self._neighbours and vertex2 in self._neighbours[vertex1]

    def count_vertices(self):
        """
        Returns the number of vertices in the graph.
        """
        return len(self._vertices)

    def count_edges(self):
        """
        Returns the number of edges in the graph.
        """
        return len(self._cost)

    def in_degree(self, vertex):
        """
        Returns the number of edges with the endpoint vertex.
        """
        if vertex not in self._transpose:
            raise VertexError("The specified vertex does not exist.")
        return len(self._transpose[vertex])

    def out_degree(self, vertex):
        """
        Returns the number of edges with the start point vertex.
        """
        if vertex not in self._neighbours:
            raise VertexError("The specified vertex does not exist.")
        return len(self._neighbours[vertex])

    def get_edge_cost(self, vertex1, vertex2):
        """
        Returns the cost of an edge if it exists.
        """
        if (vertex1, vertex2) not in self._cost:
            raise EdgeError("The specified edge does not exist.")
        return self._cost[(vertex1, vertex2)]

    def set_edge_cost(self, vertex1, vertex2, new_cost):
        """
        Sets the cost of an edge in the graph if it exists.
        """
        if (vertex1, vertex2) not in self._cost:
            raise EdgeError("The specified edge does not exist.")
        self._cost[(vertex1, vertex2)] = new_cost

    def add_vertex(self, vertex):
        """
        Adds a vertex to the graph.
        """
        if self.is_vertex(vertex):
            raise VertexError("Cannot add a vertex which already exists.")
        self._vertices.add(vertex)
        self._neighbours[vertex] = set()
        self._transpose[vertex] = set()

    def add_edge(self, vertex1, vertex2, edge_cost=0):
        """
        Adds an edge to the graph.
        """
        if self.is_edge(vertex1, vertex2):
            raise EdgeError("The specified edge already exists")
        if not self.is_vertex(vertex1) or not self.is_vertex(vertex2):
            raise EdgeError("The vertices on the edge do not exist.")
        self._neighbours[vertex1].add(vertex2)
        self._transpose[vertex2].add(vertex1)
        self._cost[(vertex1, vertex2)] = edge_cost

    def remove_edge(self, vertex1, vertex2):
        """
        Removes an edge from the graph.
        """
        if not self.is_edge(vertex1, vertex2):
            raise EdgeError("The specified edge does not exist.")
        del self._cost[(vertex1, vertex2)]
        self._neighbours[vertex1].remove(vertex2)
        self._transpose[vertex2].remove(vertex1)

    def remove_vertex(self, vertex):
        """
        Removes a vertex from the graph.
        """
        if not self.is_vertex(vertex):
            raise VertexError("Cannot remove a vertex which doesn't exist.")
        to_remove = []
        for node in self._neighbours[vertex]:
            to_remove.append(node)
        for node in to_remove:
            self.remove_edge(vertex, node)
        to_remove = []
        for node in self._transpose[vertex]:
            to_remove.append(node)
        for node in to_remove:
            self.remove_edge(node, vertex)
        del self._neighbours[vertex]
        del self._transpose[vertex]
        self._vertices.remove(vertex)

    def copy(self):
        """
        Returns a deepcopy of the graph.
        """
        return deepcopy(self)
