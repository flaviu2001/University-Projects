#include "SMMIterator.h"
#include "SortedMultiMap.h"

SMMIterator::SMMIterator(const SortedMultiMap& d) : map(d){
    pos = map.elems.head;
}

void SMMIterator::first(){
    pos = map.elems.head;
}

void SMMIterator::next(){
    if (!this->valid())
        throw std::exception();
    pos = map.elems.list[pos].next;
}

bool SMMIterator::valid() const{
    return pos != -1;
}

TElem SMMIterator::getCurrent() const{
    if (!this->valid())
        throw std::exception();
    return map.elems.list[pos].info;
}


