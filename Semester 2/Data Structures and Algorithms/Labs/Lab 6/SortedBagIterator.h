#pragma once
#include "SortedBag.h"

class SortedBag;

class SortedBagIterator
{
    friend class SortedBag;

private:
    const SortedBag& bag;
    explicit SortedBagIterator(const SortedBag& b);

    SortedBag::treap *node, *first_node;
    int cnt;

public:
    TComp getCurrent(); //Theta(1)
    bool valid();   //Theta(1)
    void next();    //total O(n) but average Theta(1) because worst is O(n) but amortized O(1)
    void first();   //Theta(1)
};

