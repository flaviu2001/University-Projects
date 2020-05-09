#include <cassert>

#include "SortedMultiMap.h"
#include "SMMIterator.h"
#include <vector>

using namespace std;

bool relation1(TKey cheie1, TKey cheie2) {
    return cheie1 <= cheie2;
}

void testAll(){
    SortedMultiMap smm = SortedMultiMap(relation1);
    assert(smm.size() == 0);
    assert(smm.isEmpty());
    smm.add(1,2);
    smm.add(1,3);
    assert(smm.size() == 2);
    assert(!smm.isEmpty());
    vector<TValue> v= smm.search(1);
    assert(v.size()==2);
    v= smm.search(3);
    assert(v.empty());
    SMMIterator it = smm.iterator();
    it.first();
    while (it.valid()){
        TElem e = it.getCurrent();
        it.next();
    }
    assert(smm.remove(1, 2));
    assert(smm.remove(1, 3));
    assert(!smm.remove(2, 1));
    assert(smm.isEmpty());
}

