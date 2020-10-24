#include "ShortTest.h"
#include "SortedBag.h"
#include "SortedBagIterator.h"
#include <cassert>
#include <vector>
#include <iostream>

using namespace std;

bool relation1(TComp e1, TComp e2) {
    return e1 <= e2;
}

void test_toSet() {
    cout << "Test toSet\n";
    SortedBag sb(relation1);
    sb.add(5);
    sb.add(6);
    sb.add(0);
    sb.add(5);
    sb.add(10);
    sb.add(8);
    sb.add(0);
    assert(sb.toSet() == 2);
    vector<TComp> v;
    SortedBagIterator it = sb.iterator();
    assert(it.valid());
    while (it.valid()) {
        v.push_back(it.getCurrent());
        it.next();
    }
    assert(v == vector<int>({0, 5, 6, 8, 10}));
}

void testAll() {
    test_toSet();
    SortedBag sb(relation1);
    sb.add(5);
    sb.add(6);
    sb.add(0);
    sb.add(5);
    sb.add(10);
    sb.add(8);

    assert(sb.size() == 6);
    assert(sb.nrOccurrences(5) == 2);

    assert(sb.remove(5));
    assert(sb.size() == 5);

    assert(sb.search(6));
    assert(!sb.isEmpty());

    SortedBagIterator it = sb.iterator();
    assert(it.valid());
    while (it.valid()) {
        it.getCurrent();
        it.next();
    }
    assert(!it.valid());
    it.first();
    assert(it.valid());
}

