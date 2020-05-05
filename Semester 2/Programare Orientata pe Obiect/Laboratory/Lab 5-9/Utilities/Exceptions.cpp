//
// Created by jack on 3/22/20.
//

#include "Exceptions.h"

ValueError::ValueError(const char *message) {
    this->message = message;
}

const char *ValueError::what() const noexcept {
    return this->message;
}

ModeError::ModeError(const char *message) {
    this->message = message;
}

const char *ModeError::what() const noexcept {
    return this->message;
}

RepositoryError::RepositoryError(const char *message) {
    this->message = message;
}

const char *RepositoryError::what() const noexcept {
    return this->message;
}
