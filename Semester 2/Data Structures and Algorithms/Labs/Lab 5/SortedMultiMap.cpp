#include "SMMIterator.h"
#include "ValueIterator.h"
#include "SortedMultiMap.h"
#include <iostream>
#include <vector>
#include <exception>
using namespace std;

SortedMultiMap::SortedMultiMap(Relation r) {
    this->dict_size = 0;
    this->load_factor = 0.7;
    this->capacity = 1;
    this->dict = new Node*[this->capacity];
    for (int i = 0; i < this->capacity; ++i)
        this->dict[i] = nullptr;
    this->comp = r;
}

void SortedMultiMap::add(TKey c, TValue v) {
    int where = this->hash_function(c);
    Node* now = this->dict[where];
    Node* prev = nullptr;
    while (now != nullptr){
        if (now->key == c){
            if (now->size == now->capacity)
                SortedMultiMap::resize(now);
            now->values[now->size] = v;
            ++now->size;
            ++this->dict_size;
            break;
        }
        if (comp(now->key, c) && now->key != c)
            prev = now;
        now = now->next;
    }
    if (now == nullptr){
        if (double(this->dict_size+1)/this->capacity >= this->load_factor) {
            this->resize();
            where = this->hash_function(c); //recalculates the hash for key because capacity was changed
            now = this->dict[where];
            prev = nullptr;
            while (now != nullptr){
                if (comp(now->key, c) && now->key != c){
                    prev = now;
                    now = now->next;
                }else break;
            }
        }
        Node *new_node = new Node;
        new_node->values[0] = v;
        new_node->size = 1;
        new_node->key = c;
        if (prev == nullptr) {
            new_node->next = this->dict[where];
            this->dict[where] = new_node;
        }else{
            new_node->next = prev->next;
            prev->next = new_node;
        }
        ++this->dict_size;
    }
}

vector<TValue> SortedMultiMap::search(TKey c) const {
    int where = this->hash_function(c);
    Node* now = this->dict[where];
    vector<TValue> values;
    while (now != nullptr){
        if (now->key == c){
            for (int i = 0; i < now->size; ++i)
                values.push_back(now->values[i]);
            break;
        }
        now = now->next;
    }
    return values;
}

bool SortedMultiMap::remove(TKey c, TValue v) {
    int where = this->hash_function(c);
    Node* prev = nullptr;
    Node* now = this->dict[where];
    while (now != nullptr){
        if (now->key == c){
            bool removed = false;
            for (int i = 0; i < now->size; ++i)
                if (now->values[i] == v){
                    for (int j = i; j+1 < now->size; ++j)
                        now->values[j] = now->values[j+1];
                    --now->size;
                    --this->dict_size;
                    removed = true;
                    break;
                }
            if (now->size == 0){
                delete[] now->values;
                if (prev == nullptr){
                    this->dict[where] = now->next;
                    delete now;
                }else{
                    Node *next = now->next;
                    prev->next = next;
                    delete now;
                }
            }
            return removed;
        }
        prev = now;
        now = now->next;
    }
    return false;
}


int SortedMultiMap::size() const {
    return this->dict_size;
}

bool SortedMultiMap::isEmpty() const {
    return this->dict_size == 0;
}

SMMIterator SortedMultiMap::iterator() const {
    return SMMIterator(*this);
}

ValueIterator SortedMultiMap::iterator(TKey k) const {
    return ValueIterator(*this, k);
}


SortedMultiMap::~SortedMultiMap() {
    for (int i = 0; i < this->capacity; ++i){
        Node *now = this->dict[i];
        while (now != nullptr){
            delete[] now->values;
            Node *next = now->next;
            delete now;
            now = next;
        }
    }
    delete[] this->dict;
}

int SortedMultiMap::hash_function(int key) const{
    if (key < 0)
        key = this->capacity+key%this->capacity;
    return key%this->capacity;
}

void SortedMultiMap::resize() {
    this->capacity *= 2;
    Node** aux = new Node*[this->capacity];
    for (int i = 0; i < this->capacity; ++i)
        aux[i] = nullptr;
    for (int i = 0; i < this->capacity/2; ++i){
        Node* now = this->dict[i];
        while (now != nullptr){
            int where = this->hash_function(now->key);
            Node* next = now->next;
            now->next = aux[where];
            aux[where] = now;
            now = next;
        }
    }
    delete[] this->dict;
    this->dict = aux;
}

void SortedMultiMap::resize(SortedMultiMap::Node *node) {
    node->capacity *= 2;
    auto* aux = new TValue[node->capacity];
    for (int i = 0; i < node->size; ++i)
        aux[i] = node->values[i];
    delete[] node->values;
    node->values = aux;
}

SortedMultiMap::Node::Node(){
    this->next = nullptr;
    this->key = 0;
    this->size = 0;
    this->capacity = 1;
    this->values = new TValue[this->capacity];
}
