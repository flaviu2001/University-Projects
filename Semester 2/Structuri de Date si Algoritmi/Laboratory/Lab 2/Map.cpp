#include "Map.h"
#include "MapIterator.h"

Map::Map() {
    this->root = nullptr;
    this->length = 0;
}

Map::~Map() {
    LinkedList *now;
    while (this->root != nullptr){
        now = this->root->next;
        delete this->root;
        this->root = now;
    }
}

TValue Map::add(TKey c, TValue v){
    LinkedList *node = this->root;
    while (node != nullptr && node->value.first != c){
        node = node->next;
    }
    if (node == nullptr){
        auto *now = new LinkedList;
        now->value = {c, v};
        now->next = this->root;
        this->root = now;
        ++this->length;
        return NULL_TVALUE;
    }
    TValue return_value = node->value.second;
    node->value = {c, v};
    return return_value;
}

TValue Map::search(TKey c) const{
    LinkedList *node = this->root;
    while (node != nullptr && node->value.first != c)
        node = node->next;
    if (node == nullptr)
        return NULL_TVALUE;
    return node->value.second;
}

TValue Map::remove(TKey c){
    if (this->root == nullptr)
        return NULL_TVALUE;
    LinkedList* prev, *now, *next;
    prev = nullptr;
    now = this->root;
    next = this->root->next;
    while (now != nullptr && now->value.first != c){
        if (prev != nullptr)
            prev = prev->next;
        else prev = now;
        now = now->next;
        if (next != nullptr)
            next = next->next;
    }
    if (now == nullptr)
        return NULL_TVALUE;
    if (prev == nullptr){
        int ret_value = this->root->value.second;
        delete this->root;
        this->root = next;
        --this->length;
        return ret_value;
    }
    int ret_value = now->value.second;
    delete now;
    prev->next = next;
    --this->length;
    return ret_value;
}


int Map::size() const {
    return this->length;
}

bool Map::isEmpty() const{
    return this->length == 0;
}

MapIterator Map::iterator() const {
    return MapIterator(*this);
}



