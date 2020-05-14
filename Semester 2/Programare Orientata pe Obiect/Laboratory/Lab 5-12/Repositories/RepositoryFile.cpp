//
// Created by jack on 4/22/20.
//

#include "RepositoryFile.h"

RepositoryFile::RepositoryFile(): size{0}{}

RepositoryFile::~RepositoryFile(){
    {} //Empty scope to fool the compiler from issuing a warning
}

void RepositoryFile::add(const Turret &turret) {
    Repository::add(turret);
    std::vector<Turret> turret_list = this->get_turret_list();
    turret_list.push_back(turret);
    ++this->size;
    this->save_to_file(turret_list);
}

void RepositoryFile::remove(const std::string &location) {
    Repository::remove(location);
    std::vector<Turret> turret_list = this->get_turret_list();
    for (auto turret = turret_list.begin(); turret != turret_list.end(); ++turret)
        if (turret->get_location() == location){
            turret_list.erase(turret);
            --this->size;
            break;
        }
    this->save_to_file(turret_list);
}

void RepositoryFile::update(const std::string &location, const std::string &new_size, const int &new_aura_level,
                           const int &new_separate_parts, const std::string &new_vision) {
    Repository::update(location, new_size, new_aura_level, new_separate_parts, new_vision);
    std::vector<Turret> turret_list = this->get_turret_list();
    for (auto &element : turret_list)
        if (element.get_location() == location){
            element.set_size(new_size);
            element.set_aura_level(new_aura_level);
            element.set_separate_parts(new_separate_parts);
            element.set_vision(new_vision);
            break;
        }
    this->save_to_file(turret_list);
}

std::string RepositoryFile::get_file_name() {
    return this->file_name;
}

int RepositoryFile::length() {
    return this->size;
}

bool RepositoryFile::is_file() {
    return true;
}
