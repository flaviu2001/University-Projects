#pragma once

#include "SortedMultiMap.h"


class SMMIterator{
    friend class SortedMultiMap;
private:
    //DO NOT CHANGE THIS PART
    const SortedMultiMap& map;
    explicit SMMIterator(const SortedMultiMap& map); //Theta(1)

    int pos;

public:
    void first();	//Theta(1)
    void next();	//Theta(1)
    bool valid() const;	//Theta(1)
    TElem getCurrent() const;	//Theta(1)
};

