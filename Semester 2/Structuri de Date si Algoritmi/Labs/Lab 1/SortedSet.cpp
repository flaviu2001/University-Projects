#include "SortedSet.h"
#include "SortedSetIterator.h"
#include <iostream>

SortedSet::SortedSet(Relation r) {
    comp = r;
    arr_size = 0;
    capacity = 1;
    array = new TComp[1];
}


bool SortedSet::add(TComp elem) {
    if (search(elem))
        return false;
    if (arr_size == capacity){
        capacity *= 2;
        auto* temp = new TComp[capacity];
        for (int i = 0; i < arr_size; ++i)
            temp[i] = array[i];
        delete[] array;
        array = temp;
    }
    int pos = 0;
    for (int i = 0; i < arr_size; ++i)
        if (comp(array[i], elem)){
            pos = i+1;
        }else break;
    for (int i = arr_size; i > pos; --i)
        array[i] = array[i-1];
    array[pos] = elem;
    ++arr_size;
    return true;
}


bool SortedSet::remove(TComp elem) {
    if (!search(elem))
        return false;
    bool found = false;
    for (int i = 0; i < arr_size-1; ++i){
        if (array[i] == elem)
            found = true;
        if (found)
            array[i] = array[i+1];
    }
    --arr_size;
    return true;
}


bool SortedSet::search(TComp elem) const {
    int st = 0, dr = arr_size-1, mid;
    for (mid = (st+dr)/2; st <= dr; mid = (st+dr)/2)
        if (array[mid] == elem)
            return true;
        else if (comp(array[mid], elem))
            st = mid+1;
        else dr = mid-1;
    return false;
}


int SortedSet::size() const {
    return arr_size;
}



bool SortedSet::isEmpty() const {
    return arr_size == 0;
}

SortedSetIterator SortedSet::iterator() const {
    return SortedSetIterator(*this);
}


SortedSet::~SortedSet() {
    delete[] array;
}


