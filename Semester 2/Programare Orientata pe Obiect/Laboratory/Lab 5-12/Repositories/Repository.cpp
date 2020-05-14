//
// Created by jack on 4/22/20.
//

#include "Repository.h"
#include "../Utilities/Validator.h"

void Repository::add(const Turret &turret) {
    Validator::validate_repository_add(this, turret.get_location());
}

void Repository::remove(const std::string &location) {
    Validator::validate_repository_remove(this, location);
}

void Repository::update(const std::string &location, const std::string &new_size, const int &new_aura_level,
                        const int &new_separate_parts, const std::string &new_vision) {
    Validator::validate_repository_update(this, location);
}

bool Repository::has_turret(const std::string &location) {
    for (const auto& x : this->get_turret_list())
        if (x.get_location() == location)
            return true;
    return false;
}

Turret Repository::find_turret(const std::string &location) {
    Validator::validate_repository_find(this, location);
    for (const auto& x : this->get_turret_list())
        if (x.get_location() == location)
            return x;
    return Turret();
}

std::string Repository::get_file_name() {
    throw RepositoryError("Cannot use files.");
}

void Repository::set_file_name(const std::string &file_name_to_set) {
    throw RepositoryError("Cannot use files.");
}

bool Repository::is_file() {
    return false;
}

