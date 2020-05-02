#include "SMMIterator.h"
#include "SortedMultiMap.h"
#include <algorithm>

SMMIterator::SMMIterator(const SortedMultiMap& d) : map(d){
    this->elems = new TElem[this->map.dict_size];
    int p = 0;
    for (int i = 0; i < this->map.capacity; ++i){
        SortedMultiMap::Node *now = this->map.dict[i];
        while (now != nullptr){
            for (int j = 0; j < now->size; ++j)
                this->elems[p++] = make_pair(now->key, now->values[j]);
            now = now->next;
        }
    }
    sort(this->elems, this->elems+this->map.dict_size, [this](const TElem &p1, const TElem &p2){
        return this->map.comp(p1.first, p2.first) && p1.first != p2.first;
    });
    this->pos = 0;
}

void SMMIterator::first(){
    this->pos = 0;
}

void SMMIterator::next(){
    if (this->pos >= this->map.dict_size)
        throw std::exception();
    ++this->pos;
}

bool SMMIterator::valid() const{
    return this->pos < this->map.dict_size;
}

TElem SMMIterator::getCurrent() const{
    if (this->pos >= this->map.dict_size)
        throw std::exception();
    return this->elems[this->pos];
}

SMMIterator::~SMMIterator() {
    delete[] this->elems;
}


