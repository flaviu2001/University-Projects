#include "ExtendedTest.h"
#include "Map.h"
#include "MapIterator.h"
#include <cassert>
#include <iostream>
#include <iomanip>

using namespace std;

void testIteratorSteps(Map& m) {
    MapIterator mi = m.iterator();
    int count = 0;
    while (mi.valid()) {
        count++;
        mi.next();
    }
    assert(count == m.size());
}

void testCreate() {
    double time_now = clock();
    Map m;
    assert(m.size() == 0);
    assert(m.isEmpty());

    for (int i = -10; i < 10; i++) {
        assert(m.search(i) == NULL_TVALUE);
    }
    for (int i = -10; i < 10; i++) {
        assert(m.search(i) == NULL_TVALUE);
    }

    MapIterator it = m.iterator();
    assert(!it.valid());
    try {
        it.next();
        assert(false);
    }
    catch (exception&) {
        //assert(true);
    }
    try {
        it.getCurrent();
        //assert(false);
    }
    catch (exception&) {
        //assert(true);
    }
    double time_now2 = clock();
    std::cout << std::fixed << std::setprecision(6) << "Test create completed in: " << (time_now2-time_now)/CLOCKS_PER_SEC << " seconds.\n";
}


void testAdd() {
    double time_now = clock();
    Map m;
    for (int i = 0; i < 10; i++) {
        assert(m.add(i, i) == NULL_TVALUE);
    }
    assert(!m.isEmpty());
    assert(m.size() == 10);
    for (int i = -10; i < 0; i++) {
        assert(m.add(i, i) == NULL_TVALUE);
    }
    for (int i = 0; i < 10; i++) {
        assert(m.add(i, i) == i);
    }
    for (int i = 10; i < 20; i++) {
        assert(m.add(i, i) == NULL_TVALUE);
    }
    assert(!m.isEmpty());
    assert(m.size() == 30);
    for (int i = -100; i < 100; i++) {
        m.add(i, i);
    }
    assert(!m.isEmpty());
    assert(m.size() == 200);
    for (int i = -200; i < 200; i++) {
        if (i < -100) {
            assert(m.search(i) == NULL_TVALUE);
        }
        else if (i < 0) {
            assert(m.search(i) == i);
        }
        else if (i < 100) {
            assert(m.search(i) == i);
        }
        else {
            assert(m.search(i) == NULL_TVALUE);
        }
    }
    for (int i = 10000; i > -10000; i--) {
        m.add(i, i);
    }
    testIteratorSteps(m);
    assert(m.size() == 20000);
    double time_now2 = clock();
    std::cout << std::fixed << std::setprecision(6) << "Test add completed in: " << (time_now2-time_now)/CLOCKS_PER_SEC << " seconds.\n";
}


void testRemove() {
    double time_now = clock();
    Map m;

    for (int i = -100; i < 100; i++) {
        assert(m.remove(i) == NULL_TVALUE);
    }
    assert(m.size() == 0);
    for (int i = -100; i < 100; i = i + 2) {
        assert(m.add(i, i) == NULL_TVALUE);
    }
    for (int i = -100; i < 100; i++) {
        if (i % 2 == 0) {
            assert(m.remove(i) == i);
        }
        else {
            assert(m.remove(i) == NULL_TVALUE);
        }
    }
    assert(m.size() == 0);

    for (int i = -100; i <= 100; i = i + 2) {
        assert(m.add(i, i) == NULL_TVALUE);
    }
    testIteratorSteps(m);
    for (int i = 100; i > -100; i--) {
        if (i % 2 == 0) {
            assert(m.remove(i) == i);
        }
        else {
            assert(m.remove(i) == NULL_TVALUE);
        }
        testIteratorSteps(m);
    }

    assert(m.size() == 1);

    m.remove(-100);
    assert(m.size() == 0);

    for (int i = -100; i < 100; i++) {
        assert(m.add(i, 0) == NULL_TVALUE);
        assert(m.add(i, 1) == 0);
        assert(m.add(i, 2) == 1);
        assert(m.add(i, 3) == 2);
        assert(m.add(i, 4) == 3);
    }
    assert(m.size() == 200);
    for (int i = -200; i < 200; i++) {
        if (i < -100 || i >= 100) {
            assert(m.remove(i) == NULL_TVALUE);
        }
        else {
            assert(m.remove(i) == 4);
            assert(m.remove(i) == NULL_TVALUE);
        }
    }
    assert(m.size() == 0);
    double time_now2 = clock();
    std::cout << std::fixed << std::setprecision(6) << "Test remove completed in: " << (time_now2-time_now)/CLOCKS_PER_SEC << " seconds.\n";
}



void testIterator() {
    double time_now = clock();
    Map m;
    MapIterator it = m.iterator();
    assert(!it.valid());

    for (int i = 0; i < 100; i++) {
        m.add(33, 33);
    }
    MapIterator it2 = m.iterator();
    assert(it2.valid());
    TElem el = it2.getCurrent();
    assert(el.first == 33);
    assert(el.second == 33);
    it2.next();
    assert(!it2.valid());
    try {
        it2.next();
        assert(false);
    }
    catch (exception&) {
        //assert(true);
    }
    try {
        it2.getCurrent();
        //assert(false);
    }
    catch (exception&) {
        //assert(true);
    }

    it2.first();
    assert(it2.valid());

    Map m2;
    for (int i = -100; i < 100; i++) {
        assert(m2.add(i, i) == NULL_TVALUE);
        assert(m2.add(i, i) == i);
        assert(m2.add(i, i) == i);
    }
    MapIterator it3 = m2.iterator();
    assert(it3.valid());
    for (int i = 0; i < 200; i++) {
        it3.next();
    }
    assert(!it3.valid());
    it3.first();
    assert(it3.valid());

    Map m3;
    for (int i = 0; i < 200; i = i + 4) {
        m3.add(i, i);
    }

    MapIterator it4 = m3.iterator();
    assert(it4.valid());
    int count = 0;
    while (it4.valid()) {
        TElem e = it4.getCurrent();
        assert(e.first % 4 == 0);
        it4.next();
        count++;
    }
    assert(count == 50);
    double time_now2 = clock();
    std::cout << std::fixed << std::setprecision(6) << "Test iterator completed in: " << (time_now2-time_now)/CLOCKS_PER_SEC << " seconds.\n";
}



void testQuantity() {
    double time_now = clock();
    Map m;
    for (int j = 0; j < 30000; j = j + 1) {
        assert(m.add(j, j) == NULL_TVALUE);
    }
    for (int i = 10; i >= 1; i--) {
        for (int j = 0; j < 30000; j = j + i) {
            assert(m.add(j, j) == j);
        }
    }
    assert(m.size() == 30000);
    MapIterator it = m.iterator();
    assert(it.valid());
    for (int i = 0; i < m.size(); i++) {
        it.next();
    }
    it.first();
    while (it.valid()) {
        TElem e = it.getCurrent();
        assert(m.search(e.first) == e.first);
        it.next();
    }
    assert(!it.valid());

    for (int j = 30000 - 1; j >= 0; j--) {
        assert(m.remove(j) == j);
    }
    for (int i = 0; i < 10; i++) {
        for (int j = 40000; j >= -4000; j--) {
            assert(m.remove(j) == NULL_TVALUE);
        }
    }
    assert(m.size() == 0);
    double time_now2 = clock();
    std::cout << std::fixed << std::setprecision(6) << "Test quantity completed in: " << (time_now2-time_now)/CLOCKS_PER_SEC << " seconds.\n";
}

void testAllExtended() {
    testCreate();
    testAdd();
    testRemove();
    testIterator();
    testQuantity();
}




