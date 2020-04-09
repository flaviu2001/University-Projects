from graph import Graph
from queue import Queue


def init_a_graph():
    g = Graph(5)
    g.add_edge(0, 1)
    g.add_edge(1, 0)
    g.add_edge(1, 1)
    g.add_edge(1, 2)
    g.add_edge(4, 0)
    g.add_edge(4, 2)
    return g


def get_root(g):
    for x in g.vertices_iterator():
        if len(list(g.transpose_iterator(x))) == 0:
            return x


def bfs_tree(graph, root):
    q = Queue()
    q.put(root)
    met = set()
    met.add(root)
    tree = Graph(graph.count_vertices())
    while not q.empty():
        node = q.get()
        for child in graph.neighbours_iterator(node):
            if child not in met:
                met.add(child)
                tree.add_edge(node, child)
                q.put(child)
    return tree


def print_tree(tree, root, spaces = 0):
    print("{0}{1}".format("    "*spaces, root))
    for node in tree.neighbours_iterator(root):
        print_tree(tree, node, spaces+1)


def road(tree, source, target):
    v = []
    while target != source:
        v.append(target)
        if len(list(tree.transpose_iterator(target))) == 0:
            return [-1]
        target = list(tree.transpose_iterator(target))[0]
    else:
        v.append(target)
    return list(reversed(v))


print_tree(bfs_tree(init_a_graph(), 4), 4)
print(road(bfs_tree(init_a_graph(), 4), 4, 2))
