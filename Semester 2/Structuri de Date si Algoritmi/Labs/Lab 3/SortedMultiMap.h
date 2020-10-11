#pragma once
//DO NOT INCLUDE SMMITERATOR

//DO NOT CHANGE THIS PART
#include <vector>
#include <utility>
typedef int TKey;
typedef int TValue;
typedef std::pair<TKey, TValue> TElem;
#define NULL_TVALUE -11111
#define NULL_TELEM make_pair(-11111, -11111)
using namespace std;
class SMMIterator;
typedef bool(*Relation)(TKey, TKey);


class SortedMultiMap {
    friend class SMMIterator;
private:
    struct DLLANode{
        TElem info;
        int prev;
        int next;

        DLLANode(); //Theta(1)
        DLLANode(TElem info, int prev, int next); //Theta(1)
    };

    struct DLLA{
        DLLANode *list;
        int head, tail;
        int capacity, size;
        int first_empty;

        DLLA(); //Theta(1)

        DLLANode& operator[](int pos); //Theta(1)
    };

    DLLA elems;
    Relation comp;
    int length;

public:

    // constructor
    explicit SortedMultiMap(Relation r);	//Theta(1)

    //adds a new key value pair to the sorted multi map
    void add(TKey c, TValue v);	//O(n)

    //returns the values belonging to a given key
    vector<TValue> search(TKey c) const;  //O(n)

    //removes a key value pair from the sorted multimap
    //returns true if the pair was removed (it was part of the multimap), false if nothing is removed
    bool remove(TKey c, TValue v);	//O(n)

    //returns the number of key-value pairs from the sorted multimap
    int size() const;	//Theta(1)

    //verifies if the sorted multi map is empty
    bool isEmpty() const;	//Theta(1)

    //returns a vector with all the keys from the sortedmultimap
    vector<TKey> keySet() const;    //best: Theta(n), average: Theta(n), worst: Theta(n)

    // returns an iterator for the sorted multimap. The iterator will returns the pairs as required by the relation (given to the constructor)
    SMMIterator iterator() const;	//Theta(1)

    // destructor
    ~SortedMultiMap();	//Theta(n)
};
