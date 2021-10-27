#pragma once

#include "SortedMultiMap.h"


class SMMIterator{
    friend class SortedMultiMap;
private:
    //DO NOT CHANGE THIS PART
    const SortedMultiMap& map;
    explicit SMMIterator(const SortedMultiMap& map);    //O(n*N), n is the number of different keys, N is the capacity of the hash table

    SortedMultiMap::Node *current, *head;
    int pos;

public:
    ~SMMIterator(); //Theta(n), n is the number of different keys pairs
    void first();   //Theta(1)
    void next();    //Theta(1)
    [[nodiscard]] bool valid() const;      //Theta(1)
    [[nodiscard]] TElem getCurrent() const; //Theta(1)
};