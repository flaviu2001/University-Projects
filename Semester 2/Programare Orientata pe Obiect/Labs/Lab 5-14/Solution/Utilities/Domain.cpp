//
// Created by jack on 3/21/20.
//

#include <iostream>
#include <sstream>
#include "Domain.h"
#include "Validator.h"
#include "Utils.h"

Turret::Turret()=default;

Turret::Turret(const std::string &location, const std::string &size, const int &aura_level, const int &separate_parts, const std::string &vision) {
    this->location = location;
    this->size = size;
    this->aura_level = aura_level;
    this->separate_parts = separate_parts;
    this->vision = vision;
}

Turret::Turret(const std::string &location, const std::string &size, const std::string &aura_level,
               const std::string &separate_parts, const std::string &vision) {
    Validator::validate_turret_fields(location, size, aura_level, separate_parts, vision);
    this->location = location;
    this->size = size;
    this->aura_level = string_to_int(aura_level);
    this->separate_parts = string_to_int(separate_parts);
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

std::istream &operator>>(std::istream &reader, Turret &turret) {

    std::string line;
    std::getline(reader, line);
    if (line.empty())
        return reader;
    strip(line);
    std::stringstream stream(line);


    std::string location;
    std::string turret_size;
    std::string number1, number2;
    std::string vision;
    int aura_level;
    int separate_parts;

    std::getline(stream, location, ',');
    strip(location);

    std::getline(stream, turret_size, ',');
    strip(turret_size);

    std::getline(stream, number1, ',');
    strip(number1);

    std::getline(stream, number2, ',');
    strip(number2);

    std::getline(stream, vision);
    strip(vision);

    Validator::validate_turret_fields(location, turret_size, number1, number2, vision);

    aura_level = 0;
    for (auto character : number1)
        aura_level = aura_level*10 + character-'0';

    separate_parts = 0;
    for (auto character : number2)
        separate_parts = separate_parts*10 + character-'0';

    turret = Turret(location, turret_size, aura_level, separate_parts, vision);
    return reader;
}

std::ostream &operator<<(std::ostream &writer, const Turret &turret) {
    writer << turret.get_location() << ",";
    writer << turret.get_size() << ",";
    writer << turret.get_aura_level() << ",";
    writer << turret.get_separate_parts() << ",";
    writer << turret.get_vision();
    return writer;
}
