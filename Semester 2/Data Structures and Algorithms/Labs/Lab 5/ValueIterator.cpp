//
// Created by jack on 5/12/20.
//

#include "ValueIterator.h"

ValueIterator::ValueIterator(const SortedMultiMap &m, TKey key) : map{m} {
    this->pos = 0;
    this->n = this->map.dict[this->map.hash_function(key)];
    while (this->n != nullptr){
        if (this->n->key == key)
            break;
        this->n = this->n->next;
    }
}

void ValueIterator::first() {
    this->pos = 0;
}

void ValueIterator::next() {
    if (!this->valid())
        throw std::exception();
    ++this->pos;
}

bool ValueIterator::valid() const {
    return this->n != nullptr && this->pos < this->n->size;
}

TValue ValueIterator::getCurrent() const {
    if (!this->valid())
        throw std::exception();
    return this->n->values[this->pos];
}
