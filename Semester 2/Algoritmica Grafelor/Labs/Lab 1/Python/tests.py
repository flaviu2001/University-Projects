import unittest
from graph import *


class Tests(unittest.TestCase):
    def test_add_rem_vertex(self):
        g = Graph()
        self.assertEqual(g.count_vertices(), 0)
        g.add_vertex(2)
        g.add_vertex(4)
        self.assertEqual(g.count_vertices(), 2)
        g.remove_vertex(4)
        self.assertEqual(g.count_vertices(), 1)
        with self.assertRaises(VertexError):
            g.add_vertex(2)
        with self.assertRaises(VertexError):
            g.remove_vertex(100)

    def test_add_rem_edge(self):
        g = Graph(10)
        g.add_edge(1, 2, 1)
        g.add_edge(1, 3, 2)
        g.add_edge(4, 2, 10)
        g.add_edge(2, 4, 9)
        self.assertEqual(g.count_edges(), 4)
        g.remove_edge(1, 2)
        self.assertEqual(g.count_edges(), 3)
        with self.assertRaises(EdgeError):
            g.add_edge(1, 3)
        with self.assertRaises(EdgeError):
            g.add_edge(11, 12)
        self.assertEqual(set(g.edges_iterator()), {(1, 3, 2), (4, 2, 10), (2, 4, 9)})

    def test_parse_set_of_vertices(self):
        g = Graph()
        g.add_vertex(4)
        g.add_vertex(1)
        g.add_vertex(9)
        v = set(g.vertices_iterator())
        self.assertEqual(v, {1, 4, 9})
        g.add_vertex(10)
        v = set(g.vertices_iterator())
        self.assertEqual(v, {1, 4, 9, 10})

    def test_is_edge(self):
        g = Graph(4)
        g.add_edge(1, 2)
        g.add_edge(2, 3)
        self.assertTrue(g.is_edge(1, 2))
        self.assertFalse(g.is_edge(2, 1))

    def test_indegree_outdegree(self):
        g = Graph(6)
        g.add_edge(1, 2)
        g.add_edge(1, 3)
        g.add_edge(1, 5)
        g.add_edge(2, 1)
        g.add_edge(4, 1)
        self.assertEqual(g.in_degree(1), 2)
        self.assertEqual(g.out_degree(1), 3)
        self.assertEqual(g.in_degree(4), 0)
        self.assertEqual(g.out_degree(4), 1)

    def test_parse_outbound_edge(self):
        g = Graph(5)
        g.add_edge(1, 2)
        g.add_edge(1, 3)
        g.add_edge(1, 4)
        g.add_edge(0, 1)
        self.assertEqual(set(g.neighbours_iterator(1)), {2, 3, 4})

    def test_parse_inbound_edge(self):
        g = Graph(5)
        g.add_edge(1, 2)
        g.add_edge(1, 3)
        g.add_edge(1, 4)
        g.add_edge(0, 1)
        self.assertEqual(set(g.transpose_iterator(1)), {0})

    def test_get_set_edge_cost(self):
        g = Graph(4)
        g.add_edge(1, 2, 5)
        g.add_edge(1, 0, 3)
        self.assertEqual(g.get_edge_cost(1, 2), 5)
        g.set_edge_cost(1, 2, 10)
        self.assertEqual(g.get_edge_cost(1, 2), 10)

    def test_copy_graph(self):
        g = Graph(4, 7)
        a = g.copy()
        a.remove_vertex(1)
        self.assertEqual(set(g.vertices_iterator()), {0, 1, 2, 3})
