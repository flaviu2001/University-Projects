#include "SortedBag.h"
#include "SortedBagIterator.h"
#include <random>

SortedBag::SortedBag(Relation r) {
    this->root = nullptr;
    this->comp = r;
    this->number_of_elements = 0;
}

void SortedBag::rotleft(SortedBag::treap *&n) {
    treap *t = n->left;

    if (t->right != nullptr)
        t->right->father = n;
    t->father = n->father;
    n->father = t;

    n->left = t->right;
    t->right = n;    n->father = t;

    n->left = t->right;
    t->right = n;
    n = t;
}

void SortedBag::rotright(SortedBag::treap *&n) {
    treap *t = n->right;

    if (t->left != nullptr)
        t->left->father = n;
    t->father = n->father;
    n->father = t;

    n->right = t->left;
    t->left = n;
    n = t;
}

void SortedBag::balance(SortedBag::treap *&n) {
    if (n->left != nullptr && n->left->priority > n->priority)
        this->rotleft(n);
    else if (n->right != nullptr && n->right->priority > n->priority)
        this->rotright(n);
}

void SortedBag::add(SortedBag::treap *&n, TComp e, SortedBag::treap* father) {
    if (n == nullptr){
        n = new treap(e, int(rand())%1000000+1, nullptr, nullptr, father);
        return;
    }
    if (e == n->key){
        ++n->frequency;
        return;
    }
    if (this->comp(e, n->key))
        this->add(n->left, e, n);
    else this->add(n->right, e, n);
    this->balance(n);
}

void SortedBag::add(TComp e) {
    add(this->root, e, nullptr);
    ++this->number_of_elements;
}

bool SortedBag::remove(SortedBag::treap *&n, TComp e) {
    if (n == nullptr)
        return false;
    if (this->comp(e, n->key) && e != n->key)
        return this->remove(n->left, e);
    if (this->comp(n->key, e) && e != n->key)
        return remove(n->right, e);
    if (n->frequency > 1){
        --n->frequency;
        return true;
    }
    if (n->left == nullptr && n->right == nullptr){
        delete n;
        n = nullptr;
        return true;
    }
    int l = 0, r = 0;
    if (n->left != nullptr)
        l = n->left->priority;
    if (n->right != nullptr)
        r = n->right->priority;
    if (l > r)
        rotleft(n);
    else rotright(n);
    return this->remove(n, e);
}

bool SortedBag::remove(TComp e) {
    bool ret = this->remove(this->root, e);
    if (ret)
        --this->number_of_elements;
    return ret;
}

SortedBag::treap* SortedBag::get_node(SortedBag::treap *n, TComp e) const {
    if (n == nullptr)
        return nullptr;
    if (e == n->key)
        return n;
    if (this->comp(e, n->key))
        return this->get_node(n->left, e);
    return this->get_node(n->right, e);
}

bool SortedBag::search(TComp elem) const {
    return this->get_node(this->root, elem) != nullptr;
}


int SortedBag::nrOccurrences(TComp elem) const {
    auto t = this->get_node(this->root, elem);
    if (t == nullptr)
        return 0;
    return t->frequency;
}

int SortedBag::size() const {
    return this->number_of_elements;
}

bool SortedBag::isEmpty() const {
    return this->number_of_elements == 0;
}

int SortedBag::toSetRec(SortedBag::treap *n) {
    if (n == nullptr)
        return 0;
    int deleted = n->frequency-1;
    n->frequency = 1;
    this->number_of_elements -= deleted;
    return deleted + toSetRec(n->left) + toSetRec(n->right);
}

int SortedBag::toSet() {
    return toSetRec(root);
}

SortedBagIterator SortedBag::iterator() const {
    return SortedBagIterator(*this);
}

void SortedBag::destroy(SortedBag::treap *&n) {
    if (n == nullptr)
        return;
    destroy(n->left);
    destroy(n->right);
    delete n;
    n = nullptr;
}

SortedBag::~SortedBag() {
    this->destroy(this->root);
}
