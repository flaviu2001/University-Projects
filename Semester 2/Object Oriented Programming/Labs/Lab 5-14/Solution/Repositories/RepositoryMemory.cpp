//
// Created by jack on 3/21/20.
//

#include "RepositoryMemory.h"

RepositoryMemory::RepositoryMemory(){
    {} //Empty scope to fool the compiler from issuing a warning
}

void RepositoryMemory::add(const Turret &turret) {
    Repository::add(turret);
    this->turret_list.push_back(turret);
}

void RepositoryMemory::remove(const std::string &location) {
    Repository::remove(location);
    for (auto it = this->turret_list.begin(); it != this->turret_list.end(); ++it)
        if (it->get_location() == location){
            this->turret_list.erase(it);
            break;
        }
}

void RepositoryMemory::update(const std::string &location, const std::string &new_size, const int &new_aura_level,
                              const int &new_separate_parts, const std::string &new_vision) {
    Repository::update(location, new_size, new_aura_level, new_separate_parts, new_vision);
    for (auto &element : this->turret_list)
        if (element.get_location() == location){
            element.set_size(new_size);
            element.set_aura_level(new_aura_level);
            element.set_separate_parts(new_separate_parts);
            element.set_vision(new_vision);
            break;
        }
}

std::vector<Turret> RepositoryMemory::get_turret_list() {
    return this->turret_list;
}

int RepositoryMemory::length() {
    return this->turret_list.size();
}

RepositoryMemory::~RepositoryMemory() {
    {} //Empty scope to fool the compiler from issuing a warning
}
