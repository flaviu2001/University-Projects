#include "SMMIterator.h"
#include "SortedMultiMap.h"
#include <iostream>
#include <utility>
#include <vector>
#include <exception>
using namespace std;

SortedMultiMap::SortedMultiMap(Relation r) {
    this->comp = r;
    this->length = 0;
}

void SortedMultiMap::add(TKey c, TValue v) {
    DLLA &elements = this->elems;
    if (elements.size == elements.capacity && elements.first_empty == -1){
        elements.capacity *= 2;
        auto *aux = new DLLANode [elements.capacity];
        for (int i = 0; i < elements.size; ++i)
            aux[i] = elements[i];
        delete[] elements.list;
        elements.list = aux;
    }
    if (elements.first_empty == -1){
        elements.first_empty = elements.size;
        elements[elements.first_empty] = DLLANode(NULL_TELEM, -1, -1);
        ++elements.size;
    }
    if (this->length == 0){
        int copy_empty = elements.first_empty;
        elements.first_empty = elements[elements.first_empty].next;
        elements[copy_empty] = DLLANode(make_pair(c, v), -1, -1);
        elements.tail = elements.head = copy_empty;
        ++this->length;
        return;
    }
    int copy_empty = elements.first_empty;
    elements.first_empty = elements[elements.first_empty].next;
    if (comp(c, elements[elements.head].info.first)){  //place it in first position
        elements[copy_empty] = DLLANode(make_pair(c, v), -1, elements.head);
        elements[elements.head].prev = copy_empty;
        elements.head = copy_empty;
        ++this->length;
        return;
    }
    if (comp(elements[elements.tail].info.first, c)){  //place it in last position
        elements[copy_empty] = DLLANode(make_pair(c, v), elements.tail, -1);
        elements[elements.tail].next = copy_empty;
        elements.tail = copy_empty;
        ++this->length;
        return;
    }
    int after = elements.head;
    for (int node = elements.head; true; node = elements[node].next){
        if (comp(elements[node].info.first, c) && elements[node].info.first != c)
            after = node;
        else break;
    }
    elements[copy_empty] = DLLANode(make_pair(c, v), after, elements[after].next);
    elements[elements[after].next].prev = copy_empty;
    elements[after].next = copy_empty;
    ++this->length;
}

vector<TValue> SortedMultiMap::search(TKey c) const {
    vector<TValue> values;
    for (int node = this->elems.head; node != -1; node = this->elems.list[node].next)
        if (this->elems.list[node].info.first == c)
            values.push_back(this->elems.list[node].info.second);
        else if (!comp(this->elems.list[node].info.first, c))
            break;
    return values;
}

bool SortedMultiMap::remove(TKey c, TValue v) {
    DLLA &elements = this->elems;
    if (this->length == 0)
        return false;
    if (this->length == 1){
        if (elements[elements.head].info != make_pair(c, v))
            return false;
        elements[elements.head].next = elements.first_empty;
        elements.first_empty = elements.head;
        elements.head = elements.tail = -1;
        --this->length;
        return true;
    }
    if (elements[elements.head].info == make_pair(c, v)){
        int copy_new_head = elements[elements.head].next;
        elements[elements.head].next = elements.first_empty;
        elements.first_empty = elements.head;
        elements.head = copy_new_head;
        elements[elements.head].prev = -1;
        --this->length;
        return true;
    }
    if (elements[elements.tail].info == make_pair(c, v)){
        int copy_new_tail = elements[elements.tail].prev;
        elements[elements.tail].next = elements.first_empty;
        elements.first_empty = elements.tail;
        elements.tail = copy_new_tail;
        elements[elements.tail].next = -1;
        --this->length;
        return true;
    }
    int pos = -1;
    for (int node = elements.head; node != -1; node = elements[node].next){
        if (comp(c, elements[node].info.first) && c != elements[node].info.first)
            break;
        if (make_pair(c, v) == elements[node].info){
            pos = node;
            break;
        }
    }
    if (pos == -1)
        return false;
    elements[elements[pos].prev].next = elements[pos].next;
    elements[elements[pos].next].prev = elements[pos].prev;
    elements[pos].next = elements.first_empty;
    elements.first_empty = pos;
    --this->length;
    return true;
}


int SortedMultiMap::size() const {
    return this->length;
}

bool SortedMultiMap::isEmpty() const {
    return this->length == 0;
}

vector<TKey> SortedMultiMap::keySet() const {
    vector<TKey> keys;
    for (int node = this->elems.head; node != -1; node = this->elems.list[node].next){
        if (!keys.empty() && keys.back() == this->elems.list[node].info.first)
            continue;
        keys.push_back(this->elems.list[node].info.first);
    }
    return keys;
}

SMMIterator SortedMultiMap::iterator() const {
    return SMMIterator{*this};
}

SortedMultiMap::~SortedMultiMap() {
    delete[] elems.list;
}

SortedMultiMap::DLLA::DLLA(){
    this->capacity = 1;
    this->size = 1;
    this->list = new DLLANode[this->capacity];
    this->list[0] = DLLANode(NULL_TELEM, -1, -1);
    this->first_empty = 0;
    this->head = -1;
    this->tail = -1;
}

SortedMultiMap::DLLANode &SortedMultiMap::DLLA::operator[](int pos) {
    return this->list[pos];
}

SortedMultiMap::DLLANode::DLLANode(TElem info, int prev, int next): info{std::move(info)}, prev{prev}, next{next} {}

SortedMultiMap::DLLANode::DLLANode(): info{NULL_TELEM}, prev{0}, next{0}{}
