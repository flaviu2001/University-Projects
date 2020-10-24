#include <bits/stdc++.h>
#include "graph.h"

using namespace std;

const int inf = 2147483647;

void backtracking(Graph &g, int node, vector<int> &stack, vector<bool> &met, vector<int> &sol)
{
    if (g.count_vertices() == stack.size()){
        if (g.is_edge(node, stack.front()))
            sol = stack;
        return;
    }
    vector<pair<int, int>> edges;
    for (auto x = g.neighbours_begin(node); x != g.neighbours_end(node); ++x)
        edges.emplace_back(g.get_edge_cost(node, *x), *x);
    sort(edges.begin(), edges.end());
    for (auto edge : edges)
        if (!met[edge.second]){
            met[edge.second] = true;
            stack.push_back(edge.second);
            backtracking(g, edge.second, stack, met, sol);
            met[edge.second] = false;
            stack.pop_back();
            if (!sol.empty())
                return;
        }
}

int main() {
    ifstream fin ("data.in");
    int n, m;
    fin >> n >> m;
    Graph g(n);
    for (int i = 0; i < m; ++i){
        int a, b, c;
        fin >> a >> b >> c;
        g.add_edge(a, b, c);
        g.add_edge(b, a, c);
    }
    vector<int> stack, sol;
    vector<bool> met(n, false);
    int begin = 0;
    stack.push_back(begin);
    met[begin] = true;
    backtracking(g, begin, stack, met, sol);
    int total_cost = 0;
    if (sol.empty()) {
        cout << "There is no hamiltonian cycle.\n";
        return 0;
    }
    cout << "The cycle is ";
    int prev = -1;
    for (auto x : sol) {
        cout << x << " ";
        if (prev != -1)
            total_cost += g.get_edge_cost(prev, x);
        prev = x;
    }
    total_cost += g.get_edge_cost(prev, sol.front());
    cout << ".\nThe cost of the cycle is " << total_cost << ".\n";
    return 0;
}
