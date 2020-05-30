#pragma once
//DO NOT INCLUDE SORTEDBAGITERATOR

//DO NOT CHANGE THIS PART
typedef int TComp;
typedef TComp TElem;
typedef bool(*Relation)(TComp, TComp);
#define NULL_TCOMP -11111;

class SortedBagIterator;

class SortedBag {
    friend class SortedBagIterator;

private:
    struct treap{
        TComp key;
        int priority, frequency;
        treap *left, *right;
        treap *father;

        treap(TComp _key, int _priority, treap *_left, treap *_right, treap *_father){  //Theta(1)
            this->key = _key;
            this->frequency = 1;
            this->priority = _priority;
            this->left = _left;
            this->right = _right;
            this->father = _father;
        }
    };

    treap *root;
    Relation comp;
    int number_of_elements;

    static void rotleft(treap* &n); //Theta(1)

    static void rotright(treap* &n);    //Theta(1)

    void balance(treap* &n);    //Theta(1)

    void add(treap* &n, TComp e, treap* father);    //total O(n) but Theta(logn) on average

    bool remove(treap* &n, TComp e);    //total O(n) but Theta(logn) on average

    treap * get_node(treap *n, TComp e) const; //total O(n) but Theta(logn) on average

    int toSetRec(treap *n); //total Theta(n)

    static void destroy(treap *&n); //Theta(n) where n is the number of distinct elements

public:
    //constructor
    explicit SortedBag(Relation r);

    //adds an element to the sorted bag
    void add(TComp e);  //total O(n) but Theta(logn) on average

    //removes one occurrence of an element from a sorted bag
    //returns true if an element was removed, false otherwise (if e was not part of the sorted bag)
    bool remove(TComp e);   //total O(n) but Theta(logn) on average

    //checks if an element appears is the sorted bag
    [[nodiscard]] bool search(TComp e) const;   //total O(n) but Theta(logn) on average

    //returns the number of occurrences for an element in the sorted bag
    [[nodiscard]] int nrOccurrences(TComp e) const; //total O(n) but Theta(logn) on average

    //returns the number of elements from the sorted bag
    [[nodiscard]] int size() const; //Theta(1)

    //returns an iterator for this sorted bag
    [[nodiscard]] SortedBagIterator iterator() const;   //total O(n) but Theta(logn) on average

    //checks if the sorted bag is empty
    [[nodiscard]] bool isEmpty() const; //Theta(1)

    //keeps only one occurrence of all elements from the SortedBag
    //returns the number of removed elements
    int toSet();    //total Theta(n)

    //destructor
    ~SortedBag();    //Theta(n) where n is the number of distinct elements
};