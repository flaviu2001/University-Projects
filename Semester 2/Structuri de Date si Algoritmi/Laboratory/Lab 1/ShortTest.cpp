#include "ShortTest.h"
#include "SortedSet.h"
#include "SortedSetIterator.h"
#include <assert.h>

bool r2(TComp e1, TComp e2) {
    if (e1 <= e2) {
        return true;
    }
    else {
        return false;
    }
}


void testAll() {
    int vverif[5];
    int iverif;
    TComp e;

    SortedSet s1(r2);
    assert(s1.add(5) == true);
    assert(s1.add(1) == true);
    assert(s1.add(10) == true);
    SortedSetIterator it1 = s1.iterator();
    it1.first();
    e = it1.getCurrent();
    iverif = 0;
    vverif[iverif++] = e;
    it1.next();
    while (it1.valid()) {
        assert(e < it1.getCurrent());
        e = it1.getCurrent();
        vverif[iverif++] = e;
        it1.next();
    }
    assert((vverif[0] == 1) && (vverif[1] == 5) && (vverif[2] == 10));


    SortedSet s(r2);
    assert(s.isEmpty() == true);
    assert(s.size() == 0);
    assert(s.add(5) == true);
    assert(s.add(1) == true);
    assert(s.add(10) == true);
    assert(s.add(7) == true);
    assert(s.add(1) == false);
    assert(s.add(10) == false);
    assert(s.add(-3) == true);
    assert(s.size() == 5);
    assert(s.search(10) == true);
    assert(s.search(16) == false);
    assert(s.remove(1) == true);
    assert(s.remove(6) == false);
    assert(s.size() == 4);

    SortedSetIterator it = s.iterator();
    iverif = 0;
    it.first();
    e = it.getCurrent();
    vverif[iverif++] = e;
    it.next();
    while (it.valid()) {
        assert(r2(e, it.getCurrent()));
        e = it.getCurrent();
        vverif[iverif++] = e;
        it.next();
    }
    assert((vverif[0] == -3) && (vverif[1] == 5) && (vverif[2] == 7) && (vverif[3] == 10));

}

