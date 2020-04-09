#include "MapIterator.h"
#include <exception>

MapIterator::MapIterator(const Map& m): map{m}{
    now = map.root;
}

void MapIterator::first(){
    now = map.root;
}

void MapIterator::next(){
    if (now == nullptr)
        throw std::exception();
    now = now->next;
}

void MapIterator::previous() {
    if (now == nullptr)
        throw std::exception();
    if (map.root == now) {
        now = nullptr;
        return;
    }
    Map::LinkedList *node = map.root;
    while (node->next != now)
        node = node->next;
    now = node;
}

TElem MapIterator::getCurrent(){
    if (now == nullptr)
        throw std::exception();
    return now->value;
}

bool MapIterator::valid() const{
    return now != nullptr;
}