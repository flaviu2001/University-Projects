#pragma once
//DO NOT INCLUDE SMMITERATOR

//DO NOT CHANGE THIS PART
#include <vector>
#include <utility>

typedef int TKey;
typedef int TValue;
typedef std::pair<TKey, TValue> TElem;
#define NULL_TVALUE -11111
#define NULL_TELEM pair<TKey, TValue>(-11111, -11111);
using namespace std;
class SMMIterator;
class ValueIterator;
typedef bool(*Relation)(TKey, TKey);


class SortedMultiMap {
    friend class SMMIterator;
    friend class ValueIterator;
private:
    struct Node{
        int key;
        int size, capacity;
        TValue *values;
        Node* next;

        Node(); //Theta(1)
    };
    int dict_size; // number of elements
    double load_factor;
    int capacity;
    Node** dict;
    Relation comp;

    //hash function for the hash table
    [[nodiscard]] int hash_function(int key) const; //Theta(1)

    //resize the hash table by doubling its capacity
    void resize();  //Theta(n)

    //resize the dynamic array in Node
    static void resize(Node *node); //Theta(n), n is the number of links in the list

public:

    // constructor
    explicit SortedMultiMap(Relation r);    //Theta(1)

    //adds a new key value pair to the sorted multi map
    void add(TKey c, TValue v); //O(n), average Theta(1)

    //returns the values belonging to a given key
    [[nodiscard]] vector<TValue> search(TKey c) const;  //Theta(n), n is the number of values with key = c

    //removes a key value pair from the sorted multimap
    //returns true if the pair was removed (it was part of the multimap), false if nothing is removed
    bool remove(TKey c, TValue v);  //O(n), average Theta(1)

    //returns the number of key-value pairs from the sorted multimap
    [[nodiscard]] int size() const; //Theta(1)

    //verifies if the sorted multi map is empty
    [[nodiscard]] bool isEmpty() const; //Theta(1)

    // returns an iterator for the sorted multimap. The iterator will returns the pairs as required by the relation (given to the constructor)
    [[nodiscard]] SMMIterator iterator() const; //Theta(nlogn + N), n is the number of different key-value pairs, N is the capacity of the hash table

    [[nodiscard]] ValueIterator iterator(TKey k) const;

    // destructor
    ~SortedMultiMap();  //Theta(n)
};
