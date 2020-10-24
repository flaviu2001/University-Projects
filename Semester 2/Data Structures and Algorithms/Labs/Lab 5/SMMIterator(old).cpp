#include "SMMIterator.h"
#include "SortedMultiMap.h"
#include <algorithm>
#include <ctime>
#include <iostream>

void SMMIterator::sort(TElem *start, TElem *finish)
{
    if (finish-start <= 1)
        return;
    int pivot = rand()%(finish-start);
    int where = -1;
    for (auto it = start; it != finish; ++it)
        if (this->map.comp(it->first, start[pivot].first))
            ++where;
    swap(start[pivot], start[where]);
    int p = 0;
    for (auto it = start; it != finish; ++it)
        if (it != start+where && this->map.comp(it->first, start[where].first))
            swap(start[p++], *it);
    this->sort(start, start+where);
    this->sort(start+where+1, finish);
}

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
    srand(time(nullptr));
    this->sort(this->elems, this->elems+this->map.dict_size);
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
