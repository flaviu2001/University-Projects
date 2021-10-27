#include <bits/stdc++.h>
#include "graph.h"

using namespace std;

vector<int> toposort(Graph &g)
{
    vector<int> in_degree(g.count_vertices(), 0);
    for (auto edge = g.edges_begin(); edge != g.edges_end(); ++edge)
        ++in_degree[edge->vertex2];
    queue<int> q;
    for (int i = 0; i < g.count_vertices(); ++i)
        if (in_degree[i] == 0)
            q.push(i);
    vector<int> topo;
    while (!q.empty()){
        int nod = q.front();
        q.pop();
        topo.push_back(nod);
        for (auto x = g.neighbours_begin(nod); x != g.neighbours_end(nod); ++x){
            --in_degree[*x];
            if (in_degree[*x] == 0)
                q.push(*x);
        }
    }
    return topo;
}

void scheduling_problem()
{
    ifstream fin ("data.in");
    int n;
    fin >> n;
    Graph g(n);
    vector<int> duration;
    for (int i = 0; i < n; ++i){
        int prerequisites, time;
        fin >> prerequisites >> time;
        duration.push_back(time);
        for (int j = 0; j < prerequisites; ++j){
            int x;
            fin >> x;
            g.add_edge(x, i);
        }
    }
    auto order = toposort(g);
    if (order.size() != g.count_vertices()){
        cout << "Not a DAG!" << "\n";
        return;
    }
    vector<int> earliest(g.count_vertices()), latest(g.count_vertices());
    int total_time = 0;
    for (auto x : order){
        earliest[x] = 0;
        for (auto in = g.transpose_begin(x); in != g.transpose_end(x); ++in)
            earliest[x] = max(earliest[x], earliest[*in]+duration[*in]);
    }
    for (auto x : earliest)
        total_time = max(total_time, earliest[x]+duration[x]);
    for (auto it = order.rbegin(); it != order.rend(); ++it){
        latest[*it] = total_time-duration[*it];
        for (auto x = g.neighbours_begin(*it); x != g.neighbours_end(*it); ++x)
            latest[*it] = min(latest[*it], latest[*x]-duration[*it]);
    }
    for (int i = 0; i < g.count_vertices(); ++i)
        cout << "For activity " << i << " the earliest starting time is " << earliest[i] << " and the latest is " << latest[i] << ".\n";
    cout << "The total time to finish the project is " << total_time << ".\n";
    cout << "The critical activities are ";
    for (int i = 0; i < g.count_vertices(); ++i)
        if (earliest[i] == latest[i])
            cout << i << " ";
    cout << "\n";
    fin.close();
}

void bonus_2()
{
    ifstream fin ("data2.in");
    int n, m;
    fin >> n >> m;
    Graph g(n);
    for (int i = 0; i < m; ++i){
        int a, b;
        fin >> a >> b;
        g.add_edge(a, b);
    }
    vector<int> order = toposort(g);
    if (order.size() != g.count_vertices()){
        cout << "Not a DAG!";
        return;
    }
    int where1 = 0, where2 = 0;
    int source, target;
    fin >> source >> target;
    for (int i = 0; i < g.count_vertices(); ++i) {
        if (source == order[i])
            where1 = i;
        if (target == order[i])
            where2 = i;
    }
    vector<int> dp(g.count_vertices(), 0);
    dp[target] = 1;
    for (int i = where2-1; i >= where1; --i){
        int nod = order[i];
        for (auto x = g.neighbours_begin(nod); x != g.neighbours_end(nod); ++x)
            dp[nod] += dp[*x];
    }
    cout << "There are " << dp[source] << " paths.\n";
    fin.close();
}


void bonus_3()
{
    ifstream fin ("data3.in");
    int n, m;
    fin >> n >> m;
    Graph g(n);
    for (int i = 0; i < m; ++i){
        int a, b, c;
        fin >> a >> b >> c;
        g.add_edge(a, b, c);
    }
    vector<int> order = toposort(g);
    if (order.size() != g.count_vertices()){
        cout << "Not a DAG!";
        return;
    }
    int where1 = 0, where2 = 0;
    int source, target;
    fin >> source >> target;
    for (int i = 0; i < g.count_vertices(); ++i) {
        if (source == order[i])
            where1 = i;
        if (target == order[i])
            where2 = i;
    }
    vector<pair<int, int>> dp(g.count_vertices());
    dp[target] = make_pair(0, 1);
    for (int i = where2-1; i >= where1; --i){
        int nod = order[i];
        dp[nod] = make_pair(-1, 1);
        for (auto x = g.neighbours_begin(nod); x != g.neighbours_end(nod); ++x)
            if (dp[*x].first != -1){
                if (dp[nod].first == -1 || dp[nod].first > dp[*x].first+g.get_edge_cost(nod, *x))
                    dp[nod] = make_pair(dp[*x].first+g.get_edge_cost(nod, *x), dp[*x].second);
                else if (dp[*x].first+g.get_edge_cost(nod, *x) == dp[nod].first)
                    dp[nod].second += dp[*x].second;
            }
    }
    cout << "The optimum path has cost " << dp[source].first << " and there is a total of " << dp[source].second << " paths.\n";
    fin.close();
}

int main() {
    scheduling_problem();
    bonus_2();
    bonus_3();
    return 0;
}
