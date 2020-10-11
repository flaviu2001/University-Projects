#include <cassert>

#include "SortedMultiMap.h"
#include "SMMIterator.h"
#include "ValueIterator.h"
#include <vector>
#include <algorithm>

using namespace std;

bool relation1(TKey cheie1, TKey cheie2) {
    return cheie1 <= cheie2;
}

void testValueIterator(){
    SortedMultiMap smm = SortedMultiMap(relation1);
    smm.add(1, 4);
    smm.add(5, 6);
    smm.add(1, 6);
    smm.add(5, 2);
    smm.add(1, 0);
    smm.add(1, 9);
    auto it = smm.iterator(1);
    vector<int> ans;
    while (it.valid()){
        ans.push_back(it.getCurrent());
        it.next();
    }
    sort(ans.begin(), ans.end());
    assert(ans == vector<int>({0, 4, 6, 9}));
    try{
        it.next();
        assert(false);
    }catch (std::exception &e){

    }catch (...){
        assert(false);
    }
    try{
        auto x = it.getCurrent();
        assert(false);
    }catch (std::exception &e){

    }catch (...){
        assert(false);
    }
    auto it2 = smm.iterator(5);
    it2.next();
    it2.first();
    ans.clear();
    for (int i = 0; i < 2; ++i) {
        ans.push_back(it2.getCurrent());
        it2.next();
    }
    sort(ans.begin(), ans.end());
    assert(ans == vector<int>({2, 6}));
}

void testAll(){
    testValueIterator();
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