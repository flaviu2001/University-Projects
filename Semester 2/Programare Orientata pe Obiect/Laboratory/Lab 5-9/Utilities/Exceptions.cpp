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

ModeError::ModeError(const char *c) {
    this->message = c;
}

const char *ModeError::what() const noexcept {
    return message;
}

RepositoryError::RepositoryError(const char *c) {
    this->message = c;
}

const char *RepositoryError::what() const noexcept {
    return message;
}
