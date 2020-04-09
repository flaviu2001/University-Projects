//
// Created by jack on 3/21/20.
//

#include "Repository.h"
#include "Exceptions.h"

Repository::Repository()=default;

void Repository::add(const Turret &turret) {
    this->turret_list.add(turret);
}

void Repository::remove(const std::string &location) {
    for (const auto &element : this->turret_list)
        if (element.get_location() == location) {
            this->turret_list.remove(element);
            break;
        }
}

void Repository::update(const std::string &location, const std::string &new_size, const int &new_aura_level,
                        const int &new_separate_parts, const std::string &new_vision) {
    for (auto &element : this->turret_list)
        if (element.get_location() == location){
            element.set_size(new_size);
            element.set_aura_level(new_aura_level);
            element.set_separate_parts(new_separate_parts);
            element.set_vision(new_vision);
            break;
        }
}

DynamicArray<Turret>& Repository::get_turret_list() {
    return this->turret_list;
}

bool Repository::has_turret(const std::string &location) {
    for (const auto &element : this->turret_list)
        if (element.get_location() == location)
            return true;
    return false;
}

Turret Repository::find_turret(const std::string &location) {
    for (const auto &element: this->turret_list)
        if (element.get_location() == location)
            return element;
    throw ValueError("Turret not found.");
}
