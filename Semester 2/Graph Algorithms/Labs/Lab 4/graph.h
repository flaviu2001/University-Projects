#ifndef GRAPH1_GRAPH_H
#define GRAPH1_GRAPH_H

#include <unordered_map>
#include <unordered_set>
#include <vector>
#include <utility>
#include <random>
#include <chrono>
#include <fstream>

struct Edge{
    int vertex1, vertex2, cost;
    Edge(int vertex1, int vertex2, int cost);
    bool operator==(Edge other)const;
};

namespace std {
    template <>
    struct hash<Edge>{
        std::size_t operator()(const Edge& k) const{
            return (std::hash<int>()(k.vertex1) ^ std::hash<int>()(k.vertex2));
        }
    };
}

typedef std::pair<int, int> pair_int;
typedef std::unordered_set<int> set_int;
typedef std::unordered_map< int, set_int > map_int;
typedef std::unordered_set< Edge > set_edge;

class VertexError : public std::exception {
private:
    const char* msg;
public:
    explicit VertexError(const char* c);
    [[nodiscard]] const char * what () const noexcept override;
};

class EdgeError : public std::exception {
private:
    const char* msg;
public:
    explicit EdgeError(const char* c);
    [[nodiscard]] const char * what () const noexcept override;
};

class Graph {
private:
    set_int vertices;
    map_int edges, transpose;
    set_edge cost;

public:
    /// Constructs an empty graph
    Graph();

    /// Constructs a graph with specified number of vertices
    explicit Graph(int vertices_no);

    /// Constructs a graph with specified number of vertices and edges, edges generated randomly
    Graph(int vertices_no, int edges_no);

    /// Returns an iterator towards the beginning of vertices
    set_int::const_iterator vertices_begin();

    /// Returns an iterator towards the end of vertices
    set_int::const_iterator vertices_end();

    /// Returns an iterator towards the beginning of neighbours of a vertex
    set_int::const_iterator neighbours_begin(int vertex);

    /// Returns an iterator towards the end of neighbours of a vertex
    set_int::const_iterator neighbours_end(int vertex);

    /// Returns an iterator towards the beginning of inbound neighbours of a vertex
    set_int::const_iterator transpose_begin(int vertex);

    /// Returns an iterator towards the end of inbound neighbours of a vertex
    set_int::const_iterator transpose_end(int vertex);

    /// Returns an iterator towards the beginning of the edges
    set_edge::const_iterator edges_begin();

    /// Returns an iterator towards the end of the edges
    set_edge::const_iterator edges_end();

    /// Returns true if the edge exists
    bool is_edge(int vertex1, int vertex2);

    /// Returns true if the edge represented by the edge exists
    bool is_edge(pair_int edge);

    /// Returns true if the vertex exists
    bool is_vertex(int vertex);

    /// Returns the number of vertices in the graph
    int count_vertices();

    /// Returns the number of edges in the graph
    int count_edges();

    /// Returns the number of edges with the endpoint vertex
    int in_degree(int vertex);

    /// Returns the number of edges with the start point vertex
    int out_degree(int vertex);

    /// Returns the cost of an edge if it exists.
    int get_edge_cost(int vertex1, int vertex2);

    /// Sets the cost of an edge in the graph if it exists.
    void set_edge_cost(int vertex1, int vertex2, int new_cost);

    /// Adds a vertex to the graph.
    void add_vertex(int vertex);

    /// Removes a vertex from the graph.
    void remove_vertex(int vertex);

    /// Adds an edge to the graph.
    void add_edge(int vertex1, int vertex2, int edge_cost = 0);

    /// Removes an edge from the graph.
    void remove_edge(int vertex1, int vertex2);
};

/// Loads the graph from the file
Graph read_file(const char* file_path);

/// Writes the graph to the file
void write_file(const char* file_path, Graph &graph);

/// Returns a random graph with given number of vertices and edges
Graph random_graph(int vertices_no, int edges_no);

#endif //GRAPH1_GRAPH_H
