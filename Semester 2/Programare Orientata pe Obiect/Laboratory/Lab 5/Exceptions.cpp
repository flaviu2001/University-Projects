//
// Created by jack on 3/22/20.
//

#include "Exceptions.h"

ValueError::ValueError(const char *c) {
    this->message = c;
}

const char *ValueError::what() const noexcept {
    return message;
}
