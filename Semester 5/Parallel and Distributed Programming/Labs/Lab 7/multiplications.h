#ifndef LAB_7_MULTIPLICATIONS_H
#define LAB_7_MULTIPLICATIONS_H

#include <vector>

#include "utils.h"
#include "globals.h"
#include "mpi_comms.h"
#include "poly_ops.h"

using namespace std;

template<typename T>
vector<T> naive(const vector<T>& poly1, const vector<T>& poly2)
{
    vector<T> product;
    if (poly1.size() <= 1 && poly2.size() <= 1) {
        T value = get_coefficient(poly1, 0) * get_coefficient(poly2, 0);
        if (value != T(0))
            product.push_back(value);
        return product;
    }
    int cutoff = (int)max(poly1.size(), poly2.size())/2;
    vector<T> xH = higher_half(poly1, cutoff);
    vector<T> xL = lower_half(poly1, cutoff);
    vector<T> yH = higher_half(poly2, cutoff);
    vector<T> yL = lower_half(poly2, cutoff);
    vector<T> a = naive(xH, yH);
    vector<T> b = naive(xH, yL);
    vector<T> c = naive(xL, yH);
    vector<T> d = naive(xL, yL);
    vector<T> ans = poly_sum(shift(a, cutoff*2), poly_sum(shift(poly_sum(b, c), cutoff), d));
    reduce(ans);
    return ans;
}

template<typename T>
vector<T> karatsuba(const vector<T>& poly1, const vector<T>& poly2)
{
    vector<T> product;
    if (poly1.size() <= 1 && poly2.size() <= 1) {
        T value = get_coefficient(poly1, 0) * get_coefficient(poly2, 0);
        if (value != T(0))
            product.push_back(value);
        return product;
    }
    int cutoff = (int)max(poly1.size(), poly2.size())/2;
    vector<T> xH = higher_half(poly1, cutoff);
    vector<T> xL = lower_half(poly1, cutoff);
    vector<T> yH = higher_half(poly2, cutoff);
    vector<T> yL = lower_half(poly2, cutoff);
    vector<T> a = karatsuba(xH, yH);
    vector<T> d = karatsuba(xL, yL);
    vector<T> e = karatsuba(poly_sum(xH, xL), poly_sum(yH, yL));
    e = poly_diff(poly_diff(e, a), d);
    vector<T> ans = poly_sum(shift(a, cutoff*2), poly_sum(shift(e, cutoff), d));
    reduce(ans);
    return ans;
}

template<typename T>
vector<T> mpi_karatsuba(const vector<T>& x, const vector<T>& y)
{
    if (debug) {
        println("<mpi_karatsuba>");
        print_poly(x);
        print_poly(y);
        println("<mpi_karatsuba/>");
    }
    vector<T> product;
    if (x.size() <= 1 && y.size() <= 1) {
        T value = get_coefficient(x, 0) * get_coefficient(y, 0);
        if (value != T(0))
            product.push_back(value);
        return product;
    }
    int cutoff = (int)max(x.size(), y.size())/2;
    vector<T> xH = higher_half(x, cutoff);
    vector<T> xL = lower_half(x, cutoff);
    vector<T> yH = higher_half(y, cutoff);
    vector<T> yL = lower_half(y, cutoff);
    vector<T> a;
    if (id*3+1 < procs)
        call_child_send(xH, yH, id*3+1);
    else a = karatsuba(xH, yH);
    vector<T> d;
    if (id*3+2 < procs)
        call_child_send(xL, yL, id*3+2);
    else d = karatsuba(xL, yL);
    vector<T> e;
    if (id*3+3 < procs)
        call_child_send(poly_sum(xH, xL), poly_sum(yH, yL), id*3+3);
    else e = karatsuba(poly_sum(xH, xL), poly_sum(yH, yL));
    if (id*3+1 < procs)
        a = call_child_recv<T>(id*3+1);
    if (id*3+2 < procs)
        d = call_child_recv<T>(id*3+2);
    if (id*3+3 < procs)
        e = call_child_recv<T>(id*3+3);
    e = poly_diff(poly_diff(e, a), d);
    vector<T> ans = poly_sum(shift(a, cutoff*2), poly_sum(shift(e, cutoff), d));
    reduce(ans);
    if (debug)
        print_poly(ans);
    return ans;
}

template<typename T>
vector<T> mpi_naive(const vector<T>& x, const vector<T>& y)
{
    if (debug) {
        println("<mpi_naive>");
        print_poly(x);
        print_poly(y);
        println("<mpi_naive/>");
    }
    vector<T> product;
    if (x.size() <= 1 && y.size() <= 1) {
        T value = get_coefficient(x, 0) * get_coefficient(y, 0);
        if (value != T(0))
            product.push_back(value);
        return product;
    }
    int cutoff = (int)max(x.size(), y.size())/2;
    vector<T> xH = higher_half(x, cutoff);
    vector<T> xL = lower_half(x, cutoff);
    vector<T> yH = higher_half(y, cutoff);
    vector<T> yL = lower_half(y, cutoff);
    vector<T> a;
    vector<T> b;
    vector<T> c;
    vector<T> d;
    if (id*4+1 < procs)
        call_child_send(xH, yH, id*4+1);
    else a = naive(xH, yH);
    if (id*4+2 < procs)
        call_child_send(xH, yL, id*4+2);
    else b = naive(xH, yL);
    if (id*4+3 < procs)
        call_child_send(xL, yH, id*4+3);
    else c = naive(xL, yH);
    if (id*4+4 < procs)
        call_child_send(xL, yL, id*4+4);
    else d = naive(xL, yL);
    if (id*4+1 < procs)
        a = call_child_recv<T>(id * 4 + 1);
    if (id*4+2 < procs)
        b = call_child_recv<T>(id * 4 + 2);
    if (id*4+3 < procs)
        c = call_child_recv<T>(id * 4 + 3);
    if (id*4+4 < procs)
        d = call_child_recv<T>(id * 4 + 4);
    vector<T> ans = poly_sum(shift(a, cutoff*2), poly_sum(shift(poly_sum(b, c), cutoff), d));
    reduce(ans);
    if (debug)
        print_poly(ans);
    return ans;
}

#endif //LAB_7_MULTIPLICATIONS_H
