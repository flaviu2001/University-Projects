#include "SMMIterator.h"
#include "SortedMultiMap.h"
#include <algorithm>

SMMIterator::SMMIterator(const SortedMultiMap& d) : map(d){
    this->current = nullptr;
    for (int i = 0; i < this->map.capacity; ++i){
        if (this->map.dict[i] == nullptr)
            continue;
        SortedMultiMap::Node *prev_curr = this->current; // to delete
        SortedMultiMap::Node *new_list = nullptr;
        SortedMultiMap::Node *new_list_head = nullptr;
        SortedMultiMap::Node *current_iterable = this->map.dict[i];
        while (this->current != nullptr && current_iterable != nullptr){
            auto *new_node = new SortedMultiMap::Node;
            delete[] new_node->values;
            if (this->map.comp(this->current->key, current_iterable->key)){
                new_node->key = this->current->key;
                new_node->values = this->current->values;
                new_node->size = this->current->size;
                new_node->capacity = this->current->capacity;
                this->current = this->current->next;
            }else{
                new_node->key = current_iterable->key;
                new_node->values = current_iterable->values;
                new_node->size = current_iterable->size;
                new_node->capacity = current_iterable->capacity;
                current_iterable = current_iterable->next;
            }
            if (new_list == nullptr) {
                new_list = new_node;
                new_list_head = new_node;
            }else {
                new_list->next = new_node;
                new_list = new_list->next;
            }
        }
        while (this->current != nullptr){
            auto *new_node = new SortedMultiMap::Node;
            delete[] new_node->values;
            new_node->key = this->current->key;
            new_node->values = this->current->values;
            new_node->size = this->current->size;
            new_node->capacity = this->current->capacity;
            this->current = this->current->next;
            if (new_list == nullptr) {
                new_list = new_node;
                new_list_head = new_node;
            }else {
                new_list->next = new_node;
                new_list = new_list->next;
            }
        }
        while (current_iterable != nullptr){
            auto *new_node = new SortedMultiMap::Node;
            delete[] new_node->values;
            new_node->key = current_iterable->key;
            new_node->values = current_iterable->values;
            new_node->size = current_iterable->size;
            new_node->capacity = current_iterable->capacity;
            current_iterable = current_iterable->next;
            if (new_list == nullptr) {
                new_list = new_node;
                new_list_head = new_node;
            }else {
                new_list->next = new_node;
                new_list = new_list->next;
            }
        }
        this->current = new_list_head;
        while (prev_curr != nullptr){
            auto* next_node = prev_curr->next;
            delete prev_curr;
            prev_curr = next_node;
        }
    }
    this->head = this->current;
    this->pos = 0;
}

void SMMIterator::first(){
    this->current = this->head;
    this->pos = 0;
}

void SMMIterator::next(){
    if (this->current == nullptr)
        throw std::exception();
    if (this->pos+1 == this->current->size){
        this->current = this->current->next;
        this->pos = 0;
    }else{
        ++this->pos;
    }
}

bool SMMIterator::valid() const{
    return this->current != nullptr;
}

TElem SMMIterator::getCurrent() const{
    if (this->current == nullptr)
        throw std::exception();
    return make_pair(this->current->key, this->current->values[this->pos]);
}

SMMIterator::~SMMIterator() {
    while (this->head != nullptr){
        auto* next_node = this->head->next;
        delete this->head;
        this->head = next_node;
    }
}