#ifndef LAB_7_UTILS_H
#define LAB_7_UTILS_H

#include <vector>
#include <string>
#include <sstream>
#include <iomanip>

using namespace std;

void println(const string& s);

template<typename T>
void print_poly(const vector<T>& poly)
{
    stringstream stream;
    for (T x : poly)
        stream << x << " ";
    println(stream.str());
}

int get_parent_id();

void init();

#endif //LAB_7_UTILS_H
