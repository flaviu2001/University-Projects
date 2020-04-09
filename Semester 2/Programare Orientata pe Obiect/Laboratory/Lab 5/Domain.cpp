//
// Created by jack on 3/21/20.
//

#include "Domain.h"

Turret::Turret(const std::string &location, const std::string &size, const int &aura_level, const int &separate_parts, const std::string &vision) {
    this->location = location;
    this->size = size;
    this->aura_level = aura_level;
    this->separate_parts = separate_parts;
    this->vision = vision;
}

std::string Turret::get_location() const{
    return this->location;
}

std::string Turret::get_size() const {
    return this->size;
}

int Turret::get_aura_level() const {
    return this->aura_level;
}

int Turret::get_separate_parts() const {
    return this->separate_parts;
}

std::string Turret::get_vision() const {
    return this->vision;
}

void Turret::set_size(const std::string &new_size) {
    this->size = new_size;
}

void Turret::set_aura_level(const int &new_aura_level) {
    this->aura_level = new_aura_level;
}

void Turret::set_separate_parts(const int &new_separate_parts) {
    this->separate_parts = new_separate_parts;
}

void Turret::set_vision(const std::string &new_vision) {
    this->vision = new_vision;
}

Turret &Turret::operator=(const Turret &new_turret) {
    if (this == &new_turret)
        return *this;
    this->location = new_turret.location;
    this->size = new_turret.size;
    this->aura_level = new_turret.aura_level;
    this->separate_parts = new_turret.separate_parts;
    this->vision = new_turret.vision;
    return *this;
}

bool Turret::operator==(const Turret &other_turret) const {
    return location == other_turret.location;
}

Turret::Turret()=default;
