#pragma once

#include "SortedMultiMap.h"


class ValueIterator {
    friend class SortedMultiMap;
private:
    //DO NOT CHANGE THIS PART
    const SortedMultiMap& map;
    ValueIterator(const SortedMultiMap& m, TKey key); //Theta(n) worst, theta(1) best, theta(1) average, O(n) total

    SortedMultiMap::Node* n;
    int pos;
public:
    void first();   //Theta(1)
    void next();    //Theta(1)
    [[nodiscard]] bool valid() const;      //Theta(1)
    [[nodiscard]] TValue getCurrent() const; //Theta(1)
};
