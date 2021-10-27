//
// Created by jack on 5/4/20.
//

#include "Repository.h"

void Repository::add(Pavilion *pavilion) {
    this->list.push_back(pavilion);
}

std::vector<Pavilion *> Repository::get_pavilions() {
    return this->list;
}

Repository::~Repository() {
    for (auto x : this->list)
        delete x;
}
