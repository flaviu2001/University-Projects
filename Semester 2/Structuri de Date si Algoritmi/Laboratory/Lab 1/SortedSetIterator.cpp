#include "SortedSetIterator.h"
#include <exception>

using namespace std;

SortedSetIterator::SortedSetIterator(const SortedSet& m) : multime(m)
{
    pos = 0;
}


void SortedSetIterator::first() {
    pos = 0;
}


void SortedSetIterator::next() {
    if (pos >= multime.arr_size)
        throw std::exception();
    ++pos;
}


TElem SortedSetIterator::getCurrent()
{
    if (pos >= multime.arr_size)
        throw std::exception();
    return multime.array[pos];
}

bool SortedSetIterator::valid() const {
    return pos < multime.arr_size;
}

