#include "SortedSet.h"
#include <assert.h>
#include "SortedSetIterator.h"
#include <exception>
#include "ExtendedTest.h"
#include <iostream>

using namespace std;

bool rGreater(TComp e1, TComp e2) {
    if (e1 > e2) {
        return true;
    }
    else {
        return false;
    }
}

bool rLessEqual(TComp e1, TComp e2) {
    if (e1 <= e2) {
        return true;
    }
    else {
        return false;
    }
}

void testIteratorSteps(SortedSet& sset, Relation r) {
    SortedSetIterator si = sset.iterator();
    TElem elem = NULL_TELEM;
    int count = 0;
    if (si.valid()) {
        elem = si.getCurrent();
        count++;
        si.next();
    }
    while (si.valid()) {
        TElem elem2 = si.getCurrent();
        assert(r(elem, elem2));
        elem = elem2;
        count++;
        si.next();
    }
    assert(count == sset.size());

}

void testCreate(Relation r) {
    cout << "Test create" << endl;
    SortedSet s(r);
    assert(s.size() == 0);
    assert(s.isEmpty() == true);

    for (int i = -10; i < 10; i++) {
        assert(s.search(i) == false);
    }
    for (int i = -10; i < 10; i++) {
        assert(s.remove(i) == false);
    }

    SortedSetIterator it = s.iterator();
    assert(it.valid() == false);
    try {
        it.next();
        assert(false);
    }
    catch (exception&) {
        assert (true);
    }
    try {
        it.getCurrent();
        assert(false);
    }
    catch (exception&) {
        assert(true);
    }
}


void testAdd() {
    cout << "Test add" << endl;
    int vverif[10];
    int iverif;

    SortedSet s2(rGreater);
    for (int i = 0; i <= 3; i++) {
        s2.add(i);
    }
    for (int i = 5; i > 3; i--) {
        s2.add(i);
    }
    SortedSetIterator it2 = s2.iterator();
    iverif = 0;
    while (it2.valid()) {
        vverif[iverif++] = it2.getCurrent();
        it2.next();
    }
    assert((vverif[0] == 5) && (vverif[1] == 4) && (vverif[2] == 3) && (vverif[3] == 2) && (vverif[4] == 1) && (vverif[5] == 0));
    assert(s2.isEmpty() == false);
    assert(s2.size() == 6);

    SortedSet s(rLessEqual);
    for (int i = 0; i <= 3; i++) {
        s.add(i);
    }
    for (int i = 5; i > 3; i--) {
        s.add(i);
    }
    SortedSetIterator it = s.iterator();
    iverif = 0;
    while (it.valid()) {
        vverif[iverif++] = it.getCurrent();
        it.next();
    }
    assert((vverif[0] == 0) && (vverif[1] == 1) && (vverif[2] == 2) && (vverif[3] == 3) && (vverif[4] == 4) && (vverif[5] == 5));
    assert(s.isEmpty() == false);
    assert(s.size() == 6);

    for (int i = -10; i < 20; i++) {
        s.add(i);
    }
    assert(s.isEmpty() == false);
    assert(s.size() == 30);
    for (int i = 100; i > -100; i--) {
        s.add(i);
    }
    testIteratorSteps(s, rLessEqual);
    assert(s.isEmpty() == false);
    assert(s.size() == 200);
    for (int i = -200; i < 200; i++) {
        if (i <= -100) {
            assert(s.search(i) == false);
        }
        else if (i < 0) {
            assert(s.search(i) == true);
        }
        else if (i <= 100) {
            assert(s.search(i) == true);
        }
        else {
            assert(s.search(i) == false);
        }
    }
    for (int i = 10000; i > -10000; i--) {
        s.add(i);
    }
    testIteratorSteps(s, rLessEqual);
    assert(s.size() == 20000);
}


void testRemove(Relation r) {
    cout << "Test remove" << endl;
    SortedSet s(r);
    for (int i = -100; i < 100; i++) {
        assert(s.remove(i) == false);
    }
    assert(s.size() == 0);

    for (int i = -100; i < 100; i = i + 2) {
        s.add(i);
    }
    for (int i = -100; i < 100; i++) {
        if (i % 2 == 0) {
            assert(s.remove(i) == true);
        }
        else {
            assert(s.remove(i) == false);
        }
        testIteratorSteps(s, r);
    }
    assert(s.size() == 0);

    for (int i = -100; i <= 100; i = i + 2) {
        s.add(i);
    }

    for (int i = 100; i > -100; i--) {
        if (i % 2 == 0) {
            assert(s.remove(i) == true);
        }
        else {
            assert(s.remove(i) == false);
        }
        testIteratorSteps(s, r);
    }
    assert(s.size() == 1);

    s.remove(-100);
    assert(s.size() == 0);

    for (int i = -100; i < 100; i++) {
        s.add(i);
        s.add(i);
        s.add(i);
        s.add(i);
        s.add(i);
    }

    assert(s.size() == 200);
    for (int i = -200; i < 200; i++) {
        if (i < -100 || i >= 100) {
            assert(s.remove(i) == false);
        }
        else {
            assert(s.remove(i) == true);
            assert(s.remove(i) == false);
        }
        testIteratorSteps(s, r);
    }
    assert(s.size() == 0);

}


void testIterator(Relation r) {
    cout << "Test iterator" << endl;
    SortedSet s(r);
    SortedSetIterator it = s.iterator();
    assert(it.valid() == false);

    for (int i = 0; i < 100; i++) {
        s.add(33);
    }
    assert(s.size() == 1);

    SortedSetIterator it2 = s.iterator();
    assert(it2.valid() == true);
    TElem elem = it2.getCurrent();
    assert(elem == 33);
    it2.next();
    assert(it2.valid() == false);
    try {
        it2.next();
        assert(false);
    }
    catch (exception&) {
        assert(true);
    }
    try {
        it2.getCurrent();
        assert(false);
    }
    catch (exception&) {
        assert(true);
    }

    it2.first();
    assert(it2.valid() == true);

    SortedSet s2(r);
    for (int i = -100; i < 100; i++) {
        s2.add(i);
        s2.add(i);
        s2.add(i);
    }

    SortedSetIterator it3 = s2.iterator();
    assert(it3.valid() == true);
    for (int i = 0; i < 200; i++) {
        it3.next();
    }
    assert(it3.valid() == false);
    it3.first();
    assert(it3.valid() == true);

    SortedSet s3(r);
    for (int i = 0; i < 200; i = i + 4) {
        s3.add(i);
    }

    SortedSetIterator it4 = s3.iterator();
    assert(it4.valid() == true);
    int count = 0;
    while (it4.valid()) {
        TElem e = it4.getCurrent();
        assert(e % 4 == 0);
        it4.next();
        count++;
    }
    assert(count == 50);
}



void testQuantity(Relation r) {
    cout << "Test quantity" << endl;
    SortedSet s(r);
    for (int i = 10; i >= 1; i--) {
        for (int j = -3000; j < 3000; j = j + i) {
            s.add(j);
        }
    }
    assert(s.size() == 6000);
    SortedSetIterator it = s.iterator();
    assert(it.valid() == true);
    for (int i = 0; i < s.size(); i++) {
        it.next();
    }
    it.first();
    while (it.valid()) {
        TElem e = it.getCurrent();
        assert(s.search(e) == true);
        it.next();
    }
    assert(it.valid() == false);
    for (int i = 0; i < 3; i++) {
        for (int j = 40000; j >= -40000; j--) {
            s.remove(j);
        }
    }
    assert(s.size() == 0);
}


void testAllExtended() {

    testCreate(rLessEqual);
    testAdd();
    testRemove(rLessEqual);
    testRemove(rGreater);
    testIterator(rLessEqual);
    testIterator(rGreater);
    testQuantity(rLessEqual);
    testQuantity(rGreater);

}

