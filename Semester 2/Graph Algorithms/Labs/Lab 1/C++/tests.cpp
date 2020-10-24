#include "tests.h"
#include "graph.h"
#include <cassert>

void test_add_rem_vertex(){
    Graph g;
    assert(g.count_vertices() == 0);
    g.add_vertex(2);
    g.add_vertex(4);
    assert(g.count_vertices() == 2);
    g.remove_vertex(4);
    assert(g.count_vertices() == 1);
    try{
        g.add_vertex(2);
        assert(false);
    }catch (VertexError &ve){
        //good
    }catch (...){
        assert(false);
    }
    try{
        g.remove_vertex(100);
        assert(false);
    }catch (VertexError &ve){
        //good
    }catch (...){
        assert(false);
    }
}

void test_add_rem_edge(){
    Graph g(10);
    g.add_edge(1, 2, 1);
    g.add_edge(1, 3, 2);
    g.add_edge(4, 2, 10);
    g.add_edge(2, 4, 9);
    assert(g.count_edges() == 4);
    g.remove_edge(1, 2);
    assert(g.count_edges() == 3);
    try{
        g.add_edge(1, 3);
        assert(false);
    }catch (EdgeError &ee){
        //good
    }catch (...){
        assert(false);
    }
    try{
        g.add_edge(11, 12);
        assert(false);
    }catch (EdgeError &ee){
        //good
    }catch (...){
        assert(false);
    }
    set_edge s1, s2;
    for (auto it = g.edges_begin(); it != g.edges_end(); ++it)
        s1.insert(*it);
    s2.insert(Edge(1, 3, 2));
    s2.insert(Edge(4, 2, 10));
    s2.insert(Edge(2, 4, 9));
    assert(s1 == s2);
}

void test_parse_set_of_vertices(){
    Graph g;
    g.add_vertex(4);
    g.add_vertex(1);
    g.add_vertex(9);
    set_int s1, s2;
    for (auto it = g.vertices_begin(); it != g.vertices_end(); ++it)
        s1.insert(*it);
    s2.insert(1);
    s2.insert(9);
    s2.insert(4);
    assert(s1 == s2);
    g.add_vertex(10);
    s1.clear();
    for (auto it = g.vertices_begin(); it != g.vertices_end(); ++it)
        s1.insert(*it);
    s2.insert(10);
    assert(s1 == s2);
}

void test_is_edge(){
    Graph g(4);
    g.add_edge(1, 2);
    g.add_edge(2, 3);
    assert(g.is_edge(1, 2));
    assert(!g.is_edge(2, 1));
}

void test_indegree_outdegree(){
    Graph g(6);
    g.add_edge(1, 2);
    g.add_edge(1, 3);
    g.add_edge(1, 5);
    g.add_edge(2, 1);
    g.add_edge(4, 1);
    assert(g.in_degree(1) == 2);
    assert(g.out_degree(1) == 3);
    assert(g.in_degree(4) == 0);
    assert(g.out_degree(4) == 1);
}

void test_outbound_edge(){
    Graph g(5);
    g.add_edge(1, 2);
    g.add_edge(1, 3);
    g.add_edge(1, 4);
    g.add_edge(0, 1);
    set_int s1, s2({2, 3, 4});
    for (auto x = g.neighbours_begin(1); x != g.neighbours_end(1); ++x)
        s1.insert(*x);
    assert(s1 == s2);
}

void test_inbound_edge(){
    Graph g(5);
    g.add_edge(1, 2);
    g.add_edge(1, 3);
    g.add_edge(1, 4);
    g.add_edge(0, 1);
    set_int s1, s2({0});
    for (auto x = g.transpose_begin(1); x != g.transpose_end(1); ++x)
        s1.insert(*x);
    assert(s1 == s2);
}

void test_get_set_edge_cost(){
    Graph g(4);
    g.add_edge(1, 2, 5);
    g.add_edge(1, 0, 3);
    assert(g.get_edge_cost(1, 2) == 5);
    g.set_edge_cost(1, 2, 10);
    assert(g.get_edge_cost(1, 2) == 10);
}

void test_all(){
    test_add_rem_vertex();
    test_add_rem_edge();
    test_parse_set_of_vertices();
    test_is_edge();
    test_indegree_outdegree();
    test_outbound_edge();
    test_inbound_edge();
    test_get_set_edge_cost();
}