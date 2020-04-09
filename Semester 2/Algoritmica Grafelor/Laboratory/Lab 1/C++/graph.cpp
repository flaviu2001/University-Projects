#include "graph.h"

VertexError::VertexError(const char* c) {
    msg = c;
}

const char *VertexError::what() const noexcept {
    return msg;
}

EdgeError::EdgeError(const char *c) {
    msg = c;
}

const char *EdgeError::what() const noexcept {
    return msg;
}

Graph::Graph() = default;

Graph::Graph(int vertices_no) {
    if (vertices_no < 0)
        throw VertexError("Invalid number of vertices.");
    for (int i = 0; i < vertices_no; ++i)
        add_vertex(i);
}

Graph::Graph(int vertices_no, int edges_no) {
    if (edges_no < 0 || edges_no > vertices_no*(vertices_no+1)/2)
        throw EdgeError("Invalid number of edges.");
    if (vertices_no < 0)
        throw VertexError("Invalid number of vertices.");
    for (int i = 0; i < vertices_no; ++i)
        add_vertex(i);
    std::mt19937 rng(std::chrono::steady_clock::now().time_since_epoch().count());
    for (int i = 0; i < edges_no; ++i){
        int a, b, c;
        do{
            a = int(rng()%2147483647)%vertices_no;
            b = int(rng()%2147483647)%vertices_no;
            c = int(rng()%2147483647)%1000000;
        }while(is_edge(a, b));
        add_edge(a, b, c);
    }
}

set_int::const_iterator Graph::vertices_begin() {
    return vertices.begin();
}

set_int::const_iterator Graph::vertices_end() {
    return vertices.end();
}

set_int::const_iterator Graph::neighbours_begin(int vertex) {
    if (!is_vertex(vertex))
        throw VertexError("Invalid vertex.");
    return edges[vertex].begin();
}

set_int::const_iterator Graph::neighbours_end(int vertex) {
    if (!is_vertex(vertex))
        throw VertexError("Invalid vertex.");
    return edges[vertex].end();
}

set_int::const_iterator Graph::transpose_begin(int vertex) {
    if (!is_vertex(vertex))
        throw VertexError("Invalid vertex.");
    return transpose[vertex].begin();
}

set_int::const_iterator Graph::transpose_end(int vertex) {
    if (!is_vertex(vertex))
        throw VertexError("Invalid vertex.");
    return transpose[vertex].end();
}

bool Graph::is_edge(int vertex1, int vertex2) {
    return edges.find(vertex1) != edges.end() && edges[vertex1].find(vertex2) != edges[vertex1].end();
}

bool Graph::is_edge(pair_int edge) {
    return edges.find(edge.first) != edges.end() && edges[edge.first].find(edge.second) != edges[edge.first].end();
}

bool Graph::is_vertex(int vertex) {
    return vertices.find(vertex) != vertices.end();
}

int Graph::count_vertices(){
    return vertices.size();
}

int Graph::in_degree(int vertex) {
    if (transpose.find(vertex) != transpose.end())
        return transpose[vertex].size();
    throw VertexError("The specified vertex does not exist.");
}

int Graph::out_degree(int vertex) {
    if (edges.find(vertex) != edges.end())
        return edges[vertex].size();
    throw VertexError("The specified vertex does not exist.");
}

int Graph::get_edge_cost(int vertex1, int vertex2) {
    auto it = cost.find(Edge(vertex1, vertex2, 0));
    if (it == cost.end())
        throw EdgeError("The specified edge does not exist.");
    return it->cost;
}

void Graph::set_edge_cost(int vertex1, int vertex2, int new_cost) {
    Edge new_edge(vertex1, vertex2, new_cost);
    auto it = cost.find(new_edge);
    if (it == cost.end())
        throw EdgeError("The specified edge does not exist.");
    cost.erase(new_edge);
    cost.insert(new_edge);
}

void Graph::add_vertex(int vertex) {
    if (is_vertex(vertex))
        throw VertexError("Cannot add a vertex which already exists.");
    vertices.insert(vertex);
    edges[vertex] = set_int();
    transpose[vertex] = set_int();
}

void Graph::remove_vertex(int vertex) {
    if (!is_vertex(vertex))
        throw VertexError("Cannot remove a vertex which doesn't exist.");
    std::vector<int> to_remove;
    for (auto node : edges[vertex])
        to_remove.emplace_back(node);
    for (auto node : to_remove)
        remove_edge(vertex, node);
    to_remove.clear();
    for (auto node : transpose[vertex])
        to_remove.emplace_back(node);
    for (auto node : to_remove)
        remove_edge(node, vertex);
    edges.erase(vertex);
    transpose.erase(vertex);
    vertices.erase(vertices.find(vertex));
}

void Graph::add_edge(int vertex1, int vertex2, int edge_cost) {
    if (is_edge(std::make_pair(vertex1, vertex2)))
        throw EdgeError("The specified edge already exists");
    if (!is_vertex(vertex1) || !is_vertex(vertex2))
        throw EdgeError("The vertices on the edge do not exist.");
    edges[vertex1].insert(vertex2);
    transpose[vertex2].insert(vertex1);
    cost.insert(Edge(vertex1, vertex2, edge_cost));
}

void Graph::remove_edge(int vertex1, int vertex2) {
    if (!is_edge(std::make_pair(vertex1, vertex2)))
        throw EdgeError("The specified edge does not exist.");
    cost.erase(Edge(vertex1, vertex2, 0));
    edges[vertex1].erase(edges[vertex1].find(vertex2));
    transpose[vertex2].erase(transpose[vertex2].find(vertex1));
}

int Graph::count_edges() {
    return cost.size();
}

set_edge::const_iterator Graph::edges_begin() {
    return cost.begin();
}

set_edge::const_iterator Graph::edges_end() {
    return cost.end();
}

Graph read_file(const char* file_path) {
    std::ifstream fin(file_path);
    int n, m;
    fin >> n >> m;
    Graph g(n);
    for (int i = 1; i <= m; ++i){
        int a, b, c;
        fin >> a >> b >> c;
        g.add_edge(a, b, c);
    }
    fin.close();
    return g;
}

void write_file(const char* file_path, Graph &graph) {
    std::ofstream fout(file_path);
    fout << graph.count_vertices() << " " << graph.count_edges() << "\n";
    for (auto it = graph.vertices_begin(); it != graph.vertices_end(); ++it)
        for (auto node = graph.neighbours_begin(*it); node != graph.neighbours_end(*it); ++node)
            fout << *it << " " << *node << " " << graph.get_edge_cost(*it, *node) << "\n";
    fout.close();
}

Graph random_graph(int vertices_no, int edges_no)
{
    Graph g;
    if (edges_no < 0 || edges_no > vertices_no*(vertices_no+1)/2)
        throw EdgeError("Invalid number of edges.");
    if (vertices_no < 0)
        throw VertexError("Invalid number of vertices.");
    for (int i = 0; i < vertices_no; ++i)
        g.add_vertex(i);
    std::mt19937 rng(std::chrono::steady_clock::now().time_since_epoch().count());
    for (int i = 0; i < edges_no; ++i){
        int a, b, c;
        do{
            a = int(rng()%2147483647)%vertices_no;
            b = int(rng()%2147483647)%vertices_no;
            c = int(rng()%2147483647)%1000000;
        }while(g.is_edge(a, b));
        g.add_edge(a, b, c);
    }
    return g;
}

Edge::Edge(int _vertex1, int _vertex2, int _cost) {
    vertex1 = _vertex1;
    vertex2 = _vertex2;
    cost = _cost;
}

bool Edge::operator==(Edge other) const{
    return vertex1 == other.vertex1 && vertex2 == other.vertex2;
}
