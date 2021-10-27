#include <exception>
#include <assert.h>
#include <algorithm>
#include <vector>
#include <iostream>
#include "SMMIterator.h"
#include "SortedMultiMap.h"
#include "ExtendedTest.h"

using namespace std;

bool asc(TKey c1, TKey c2) {
    if (c1 <= c2) {
        return true;
    } else {
        return false;
    }
}

bool desc(TKey c1, TKey c2) {
    if (c1 >= c2) {
        return true;
    } else {
        return false;
    }
}

bool rel3(TKey c1, TKey c2) {
    int s1 = 0;
    while (c1 > 0) {
        s1 += c1 % 10;
        c1 = c1 / 10;
    }

    int s2 = 0;
    while (c2 > 0) {
        s2 += c2 % 10;
        c2 = c2 / 10;
    }
    return s1 <= s2;
}

bool rel4(TKey c1, TKey c2) {
    return c1 % 1111 <= c2 % 1111;
}

bool rel5(TKey c1, TKey c2) {
    return c1 % 1111 >= c2 % 1111;
}

void testIteratorSteps(SortedMultiMap& m) {
    SMMIterator smmi = m.iterator();
    int count = m.size();
    for (int i = 0; i < count / 2; i++) {
        smmi.next();
    }
    smmi.first();
    int c = 0;

    while (smmi.valid()) {
        c++;
        smmi.next();
    }
    assert(c == m.size());
}

void testRelation(Relation r) {
    SortedMultiMap smm(r);
    int current = 3;
    while (smm.size() < 8000) {
        smm.add(current, current);
        smm.add(current, current * 2);
        current = (current + 13) % 99;
    }
    SMMIterator smit = smm.iterator();
    TKey first = smit.getCurrent().first;
    smit.next();
    while (smit.valid()) {
        TKey current = smit.getCurrent().first;
        assert(current == smit.getCurrent().second || current == smit.getCurrent().second / 2);
        assert(r(first, current));
        first = current;
        smit.next();
    }
    testIteratorSteps(smm);
}

void testRelations() {
    cout << "Test relations" << endl;
    testRelation(asc);
    testRelation(desc);
    testRelation(rel3);
    testRelation(rel4);
    testRelation(rel5);
}


void testCreate() {
    cout << "Test create" << endl;
    SortedMultiMap smm = SortedMultiMap(asc);
    assert(smm.size() == 0);
    assert(smm.isEmpty());

    for (int i = 0; i < 10; i++) {
        vector<TValue> v= smm.search(i);
        assert(v.size()==0);
    }

    for (int i = -10; i < 10; i++) {
        vector<TValue> v= smm.search(i);
        assert(v.size()==0);
    }
}

void testSearch(Relation r) {
    cout << "Test search" << endl;
    SortedMultiMap smm = SortedMultiMap(r);
    int kMin = 0;
    int kMax = 10;
    for (int i = kMin; i <= kMax; i++) {
        smm.add(i, i + 1);
        smm.add(i, i + 2);
    }
    int intervalDim = 10;
    testIteratorSteps(smm);
    for (int i = kMin; i <= kMax; i++) {
        vector<TValue> v= smm.search(i);
        assert(v.size()==2);
    }
    for (int i = kMin - intervalDim; i < kMin; i++) {
        vector<TValue> v= smm.search(i);
        assert(v.size()==0);
    }
    for (int i = kMax + 1; i < kMax + intervalDim; i++) {
        vector<TValue> v= smm.search(i);
        assert(v.size()==0);
    }
}

void testSearch() {
    testSearch(asc);
    testSearch(desc);
}

void populateSMMEmpty(SortedMultiMap& smm, int min, int max) {
    for (int i = min; i <= max; i++) {
        smm.add(i, i);
        if (i%2 ==0)
            smm.add(i, i+2);
    }
}

void testRemoveSearch(Relation r) {
    cout << "Test remove and search" << endl;
    SortedMultiMap smm = SortedMultiMap(r);
    int min = 10;
    int max = 20;
    populateSMMEmpty(smm, min, max);
    testIteratorSteps(smm);
    for (int c = min; c <= max; c++) {
        assert(smm.remove(c, c+1) == false);
        if (c%2==0)
            assert(smm.remove(c,c) == true);
        testIteratorSteps(smm);
    }

    for (int c = min; c <= max; c++) {
        if (c%2==1){
            assert(smm.remove(c,c+1) == false);
            assert(smm.remove(c,c) == true);
        }
        else{
            assert(smm.remove(c,c+2) == true);
        }
        testIteratorSteps(smm);
    }
    assert(smm.size() == 0);
}

void testRemove() {
    testRemoveSearch(asc);
    testRemoveSearch(desc);
}

vector<int> randomKeys(int kMin, int kMax) {
    vector<int> keys;
    for (int c = kMin; c <= kMax; c++) {
        keys.push_back(c);
    }
    int n = keys.size();
    for (int i = 0; i < n - 1; i++) {
        int j = i + rand() % (n - i);
        swap(keys[i], keys[j]);
    }
    return keys;
}


void testIterator(Relation r) {
    cout << "Test iterator" << endl;
    SortedMultiMap smm = SortedMultiMap(r);
    SMMIterator it = smm.iterator();
    assert(!it.valid());
    try {
        it.next();
        assert(false);
    }
    catch (exception& ex) {
        assert(true);
    }
    try {
        it.getCurrent();
        assert(false);
    }
    catch (exception& ex) {
        assert(true);
    }
    it.first();
    assert(!it.valid());
    int cMin = 100;
    int cMax = 300;
    vector<int> keys = randomKeys(cMin, cMax);
    int n = keys.size();
    for (int i = 0; i < n; i++) {
        smm.add(keys[i], 100);
        if (keys[i]%2==0)	{
            smm.add(keys[i], 200);
        }
    }
    testIteratorSteps(smm);
    SMMIterator itsmm = smm.iterator();
    assert(itsmm.valid());
    itsmm.first();
    assert(itsmm.valid());

    TKey kPrev = itsmm.getCurrent().first;

    itsmm.next();
    while (itsmm.valid()) {
        TKey k = itsmm.getCurrent().first;
        assert(r(kPrev, k));
        kPrev = k;
        itsmm.next();
    }
}

void testIterator() {
    testIterator(asc);
    testIterator(desc);
}

void testAllExtended() {
    testCreate();
    testSearch();
    testRemove();
    testIterator();
    testRelations();
}