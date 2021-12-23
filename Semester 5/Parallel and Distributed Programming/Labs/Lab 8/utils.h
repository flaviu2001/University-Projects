#ifndef LAB_8_UTILS_H
#define LAB_8_UTILS_H

#include <iostream>

#include "globals.h"

template<typename T>
void println(T t) {
    std::cout << current_id << ": " << t << "\n";
}

#endif //LAB_8_UTILS_H
