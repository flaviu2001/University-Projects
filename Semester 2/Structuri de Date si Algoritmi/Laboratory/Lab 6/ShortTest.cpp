#include "ShortTest.h"
#include "SortedBag.h"
#include "SortedBagIterator.h"
#include <assert.h>

bool relation1(TComp e1, TComp e2) {
    return e1 <= e2;
}

void testAll() {
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

