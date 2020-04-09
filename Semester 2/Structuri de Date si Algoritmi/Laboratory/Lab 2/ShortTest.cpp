#include "ShortTest.h"
#include <cassert>
#include "Map.h"
#include "MapIterator.h"
#include <iostream>
#include <iomanip>

void testAll() { //call each function to see if it is implemented
    double time_now = clock();
    Map m;
    assert(m.isEmpty());
    assert(m.size() == 0); //add elements
    assert(m.add(5,5)==NULL_TVALUE);
    assert(m.add(1,111)==NULL_TVALUE);
    assert(m.add(10,110)==NULL_TVALUE);
    assert(m.add(7,7)==NULL_TVALUE);
    assert(m.add(1,1)==111);
    assert(m.add(10,10)==110);
    assert(m.add(-3,-3)==NULL_TVALUE);
    assert(m.size() == 5);
    assert(m.search(10) == 10);
    assert(m.search(16) == NULL_TVALUE);
    assert(m.remove(1) == 1);
    assert(m.remove(6) == NULL_TVALUE);
    assert(m.size() == 4);


    TElem e;
    MapIterator id = m.iterator();
    id.first();
    int s1 = 0, s2 = 0;
    while (id.valid()) {
        e = id.getCurrent();
        s1 += e.first;
        s2 += e.second;
        id.next();
    }
    assert(s1 == 19);
    assert(s2 == 19);
    double time_now2 = clock();

    MapIterator mi = m.iterator();
    mi.first();
    TElem p;
    p = mi.getCurrent();
    mi.next();
    while (mi.valid()){
        mi.previous();
        assert(p == mi.getCurrent());
        mi.next();
        p = mi.getCurrent();
        mi.next();
    }

    std::cout << std::fixed << std::setprecision(6) << "Short test completed in: " << (time_now2-time_now)/CLOCKS_PER_SEC << " seconds.\n";
}


