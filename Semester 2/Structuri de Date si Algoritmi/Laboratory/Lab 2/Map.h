#pragma once
#include <utility>
//DO NOT INCLUDE MAPITERATOR


//DO NOT CHANGE THIS PART
typedef int TKey;
typedef int TValue;
typedef std::pair<TKey, TValue> TElem;
#define NULL_TVALUE -11111
//#define NULL_TELEM pair<TKey, TValue>(-11111, -11111)
class MapIterator;

class Map {
    //DO NOT CHANGE THIS PART
    friend class MapIterator;

private:
    class LinkedList{
    public:
        TElem value;
        LinkedList *next;
        LinkedList(){
            value = {0, 0};
            next = nullptr;
        }
    };

    LinkedList* root;
    int length;

public:

    // implicit constructor
    Map();  //Theta(1)

    // adds a pair (key,value) to the map
    //if the key already exists in the map, then the value associated to the key is replaced by the new value and the old value is returned
    //if the key does not exist, a new pair is added and the value null is returned
    TValue add(TKey c, TValue v);   //O(n)

    //searches for the key and returns the value associated with the key if the map contains the key or null: NULL_TVALUE otherwise
    [[nodiscard]] TValue search(TKey c) const;  //O(n)

    //removes a key from the map and returns the value associated with the key if the key existed ot null: NULL_TVALUE otherwise
    TValue remove(TKey c);  //O(n)

    //returns the number of pairs (key,value) from the map
    [[nodiscard]] int size() const; //Theta(1)

    //checks whether the map is empty or not
    [[nodiscard]] bool isEmpty() const; //Theta(1)

    //returns an iterator for the map
    [[nodiscard]] MapIterator iterator() const; //Theta(1)

    // destructor
    ~Map();   //Theta(n)

};



