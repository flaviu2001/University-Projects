#pragma once

#include "SortedMultiMap.h"


class SMMIterator{
    friend class SortedMultiMap;
private:
    //DO NOT CHANGE THIS PART
    const SortedMultiMap& map;
    explicit SMMIterator(const SortedMultiMap& map);    //O(n^2 + N), n is the number of different key-value pairs, N is the capacity of the hash table
                                                        //Theta(nlogn+N) average case

    TElem *elems;
    int pos;

    void sort(TElem *start, TElem *finish);

public:
    ~SMMIterator(); //Theta(n), n is the number of different key-value pairs
    void first();   //Theta(1)
    void next();    //Theta(1)
    [[nodiscard]] bool valid() const;      //Theta(1)
    [[nodiscard]] TElem getCurrent() const; //Theta(1)
};