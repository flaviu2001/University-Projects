#ifndef LAB_7_POLY_OPS_H
#define LAB_7_POLY_OPS_H

#include <vector>
#include <random>
#include <cmath>

using namespace std;

template<typename T>
bool different(const vector<T>& vector1, const vector<T>& vector2) {
    if (vector1.size() != vector2.size())
        return true;
    for (int i = 0; i < vector1.size(); ++i)
        if (vector1[i] != vector2[i])
            return true;
    return false;
}

template<>
bool different<float>(const vector<float>& vector1, const vector<float>& vector2) {
    if (vector1.size() != vector2.size())
        return true;
    for (int i = 0; i < vector1.size(); ++i)
        if (abs(vector1[i] - vector2[i]) > 0.0001)
            return true;
    return false;
}

template<typename T>
T get_coefficient(const vector<T>& poly, int index)
{
    if (index >= poly.size())
        return T(0);
    return poly[index];
}

template<typename T>
vector<T> lower_half(const vector<T> &poly, int cutoff)
{
    vector<T> new_poly;
    for (int i = 0; i < min((int)poly.size(), cutoff); ++i)
        new_poly.push_back(poly[i]);
    return new_poly;
}

template<typename T>
vector<T> higher_half(const vector<T> &poly, int cutoff)
{
    vector<T> new_poly;
    for (int i = cutoff; i < (int)poly.size(); ++i)
        new_poly.push_back(poly[i]);
    return new_poly;
}

template<typename T>
void reduce(vector<T> &poly)
{
    while (!poly.empty() && poly.back() == T(0))
        poly.pop_back();
}

template<typename T>
vector<T> poly_sum(const vector<T>& poly1, const vector<T>& poly2)
{
    vector<T> ans;
    for (int i = 0; i < max(poly1.size(), poly2.size()); ++i)
        ans.push_back(get_coefficient(poly1, i) + get_coefficient(poly2, i));
    return ans;
}

template<typename T>
vector<T> poly_diff(const vector<T>& poly1, const vector<T>& poly2)
{
    vector<T> ans;
    for (int i = 0; i < max(poly1.size(), poly2.size()); ++i)
        ans.push_back(get_coefficient(poly1, i) - get_coefficient(poly2, i));
    reduce(ans);
    return ans;
}

template<typename T>
vector<T> shift(const vector<T> &poly, int offset)
{
    vector<T> ans;
    ans.reserve(offset+poly.size());
    for (int i = 0; i < offset; ++i)
        ans.push_back(T(0));
    for (T x : poly)
        ans.push_back(x);
    return ans;
}

template<typename T>
vector<T> generate_poly(int n)
{
    vector<T> poly;
    poly.reserve(n);
    for (int i = 0; i < n; ++i)
        poly.push_back(T(rand()%10)); // NOLINT(cert-msc50-cpp)
    return poly;
}

template<>
vector<float> generate_poly<float>(int n)
{
    vector<float> poly;
    poly.reserve(n);
    for (int i = 0; i < n; ++i)
        poly.push_back(float(rand()%10)/10.0f); // NOLINT(cert-msc50-cpp)
    return poly;
}

#endif //LAB_7_POLY_OPS_H
