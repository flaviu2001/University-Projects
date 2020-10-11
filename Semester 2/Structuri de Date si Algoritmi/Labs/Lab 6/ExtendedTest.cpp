#include "ShortTest.h"
#include "SortedBag.h"
#include "SortedBagIterator.h"
#include <cassert>
#include <iostream>
#include <exception>

using namespace std;

bool relation2(TComp r1, TComp r2) {
    return r1 <= r2;
}

bool relation3(TComp r1, TComp r2) {
    return r1 >= r2;
}


void testCreate() {
    cout << "Test create" << endl;
    SortedBag sb(relation2);
    assert(sb.isEmpty());
    assert(sb.size() == 0);

    for (int i = -10; i < 30; i++) {
        assert(!sb.remove(i));
    }
    for (int i = -10; i < 30; i++) {
        assert(!sb.search(i));
    }
    for (int i = -10; i < 30; i++) {
        assert(sb.nrOccurrences(i) == 0);
    }
    SortedBagIterator it = sb.iterator();
    assert(!it.valid());
    try {
        it.getCurrent();
        assert(false);
    }
    catch (exception&) {
        static_assert(true);
    }
    try {
        it.next();
        assert(false);
    }
    catch (exception&) {
        static_assert(true);
    }
}

void testIterator(SortedBag& sb, Relation rel) {
    SortedBagIterator it = sb.iterator();
    TComp e1;
    TComp e2;
    if (it.valid()) {
        e1 = it.getCurrent();
        it.next();
    }
    while (it.valid()) {
        e2 = e1;
        e1 = it.getCurrent();
        it.next();
        assert(sb.search(e1));
        assert(sb.search(e2));
        assert(sb.nrOccurrences(e1) > 0);
        assert(sb.nrOccurrences(e2) > 0);
        assert(rel(e2, e1));
    }
    try {
        it.getCurrent();
        assert(false);
    }
    catch (exception&) {
        static_assert(true);
    }
    try {
        it.next();
        assert(false);
    }
    catch (exception&) {
        static_assert(true);
    }
    it.first();
    assert(it.valid());
    it.first();
    int count = 0;
    while (it.valid()) {
        count++;
        it.next();
    }
    assert(count == sb.size());
}

void testAdd(Relation r) {
    cout << "Test add" << endl;
    SortedBag sb(r);
    for (int i = 0; i < 100; i++) {

        sb.add(i);
    }
    assert(sb.size() == 100);
    assert(!sb.isEmpty());
    testIterator(sb, r);

    for (int i = 200; i >= -200; i--) {
        sb.add(i);
    }
    assert(sb.size() == 501);
    testIterator(sb, r);

    for (int i = -300; i < 300; i++) {
        bool exista = sb.search(i);
        int nrA = sb.nrOccurrences(i);

        if (i < -200 || i > 200) {
            assert(!exista);
            assert(nrA == 0);
        }
        else if (i >= -200 && i < 0) {
            assert(exista);
            assert(nrA == 1);
        }
        else if (i >= 0 && i < 100) {
            assert(exista);
            assert(nrA == 2);
        }
        else if (i >= 100 && i <= 200) {
            assert(exista);
            assert(nrA == 1);
        }
    }

    for (int i = 0; i < 100; i++) {
        sb.add(0);
    }
    assert(sb.nrOccurrences(0) == 102);
    testIterator(sb, r);

    SortedBag sb2(r);
    for (int i = 0; i < 300; i = i + 2) {
        sb2.add(i);
        sb2.add(2 * i);
        sb2.add(-2 * i);
    }
    assert(sb2.size() == 450);
    testIterator(sb2, r);
}

void testRemove(Relation r) {
    cout << "Test remove" << endl;
    SortedBag sb(r);
    for (int i = -100; i < 100; i++) {
        assert(!sb.remove(i));
        assert(!sb.search(i));
        assert(sb.nrOccurrences(i) == 0);
    }
    assert(sb.isEmpty());

    for (int i = -100; i < 100; i++) {
        sb.add(i);
    }
    assert(sb.size() == 200);
    for (int i = -100; i < 100; i = i + 2) {
        assert(sb.remove(i));
    }
    assert(sb.size() == 100);
    for (int i = -100; i < 100; i++) {
        if (i % 2 == 0) {
            assert(sb.nrOccurrences(i) == 0);
            assert(!sb.search(i));
            assert(!sb.remove(i));
        }
        else {
            assert(sb.nrOccurrences(i) == 1);
            assert(sb.search(i));
        }
        sb.add(i);
        sb.add(i);
        sb.add(i);
    }
    assert(sb.size() == 700);
    for (int i = -200; i < 200; i++) {
        if (i < -100 || i >= 100) {
            assert(!sb.search(i));
            assert(sb.nrOccurrences(i) == 0);
            assert(!sb.remove(i));
        }
        else if (i % 2 == 0) {
            assert(sb.search(i));
            assert(sb.nrOccurrences(i) == 3);
            assert(sb.remove(i));
            assert(sb.remove(i));
            assert(sb.remove(i));
            assert(!sb.remove(i));
        }
        else {
            assert(sb.search(i));
            assert(sb.nrOccurrences(i) == 4);
            assert(sb.remove(i));
            assert(sb.remove(i));
            assert(sb.remove(i));
            assert(sb.remove(i));
            assert(!sb.remove(i));
        }
    }
    SortedBag sb2(r);
    for (int i = 300; i >= -500; i--) {
        sb2.add(i);
        sb2.add(i * 2);
        sb2.add(-2 * i);
    }
    for (int i = -100; i < 100; i++) {
        for (int j = 0; j < 100; j++) {
            sb2.remove(i);
        }
    }
    for (int i = -100; i < 100; i++) {
        assert(sb2.nrOccurrences(i) == 0);
        assert(!sb2.search(i));
    }
}

void testQuantity(Relation r) {
    cout << "Test quantity" << endl;
    SortedBag sb(r);
    for (int j = 0; j < 10; j++) {
        sb.add(0);
        for (int i = 1; i < 300; i++) {
            sb.add(i);
            sb.add(-i);
        }
        sb.add(-3000);
    }
    int count = 6000;
    assert(sb.size() == 6000);
    for (int i = 0; i < 5000; i++) {
        TComp nr = rand() % 8000 - 4000;
        if (sb.remove(nr)) {
            count--;
        }
        assert(sb.size() == count);
    }
    assert(sb.size() == count);
}

void testIterator(Relation rel) {
    cout << "Test iterator" << endl;
    SortedBag sb(rel);
    for (int i = 0; i < 500; i++) {
        sb.add(i);
        sb.add(-2 * i);
        sb.add(2 * i);
        sb.add(i);
        sb.add(-2 * i);
        sb.add(2 * i);
    }

    assert(sb.size() == 3000);

    SortedBagIterator sbi = sb.iterator();
    int count = 0;
    while (sbi.valid()) {
        count++;
        sbi.next();
    }
    assert(count == sb.size());
    sbi.first();
    TElem e = sbi.getCurrent();
    sbi.next();
    count = 1;
    while (sbi.valid()) {
        TElem ee = sbi.getCurrent();
        assert(rel(e, ee));
        TElem ee2 = sbi.getCurrent();
        assert(ee == ee2);
        TElem ee3 = sbi.getCurrent();
        assert(ee == ee3);
        e = ee;
        sbi.next();
        count++;
    }
    assert(count == sb.size());
}



void testAllExtended() {
    testCreate();
    testAdd(relation2);
    testAdd(relation3);
    testRemove(relation2);
    testRemove(relation3);
    testIterator(relation2);
    testIterator(relation3);
    testQuantity(relation2);
    testQuantity(relation3);
}
