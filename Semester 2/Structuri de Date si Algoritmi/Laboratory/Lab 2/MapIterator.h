#pragma once
#include "Map.h"
class MapIterator
{
    //DO NOT CHANGE THIS PART
    friend class Map;
private:
    const Map& map;
    Map::LinkedList *now;

    explicit MapIterator(const Map& m);
public:
    void first();   //Theta(1)
    void next();    //Theta(1)
    void previous();    //Theta(n) - Average, Theta(1) - Best, Theta(n) - Worst
    TElem getCurrent(); //Theta(1)
    [[nodiscard]] bool valid() const;   //Theta(1)
};


