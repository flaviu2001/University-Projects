#include "SortedBagIterator.h"
#include "SortedBag.h"
#include <exception>

using namespace std;

SortedBagIterator::SortedBagIterator(const SortedBag& b) : bag(b) {
    this->first_node = this->bag.root;
    if (this->first_node != nullptr)
        while (this->first_node->left != nullptr)
            this->first_node = this->first_node->left;
    this->node = this->first_node;
    this->cnt = 0;
}

TComp SortedBagIterator::getCurrent() {
    if (!this->valid())
        throw std::exception();
    return this->node->key;
}

bool SortedBagIterator::valid() {
    return this->node != nullptr;
}

void SortedBagIterator::next() {
    if (!this->valid())
        throw std::exception();
    ++this->cnt;
    if (this->cnt >= this->node->frequency){
        this->cnt = 0;
        if (this->node->right != nullptr){
            this->node = this->node->right;
            while (this->node->left != nullptr)
                this->node = this->node->left;
            return;
        }
        while (this->node->father != nullptr && this->node->father->right == this->node)
            this->node = this->node->father;
        this->node = this->node->father;
    }
}

void SortedBagIterator::first() {
    this->node = this->first_node;
    this->cnt = 0;
}

