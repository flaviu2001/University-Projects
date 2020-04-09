//
// Created by jack on 3/21/20.
//

#include "Service.h"
#include "Exceptions.h"

Service::Service(Repository &repository){
    this->main_repository = repository;
    this->mode = "";
    this->iterator = 0;
}

void Service::set_mode(const std::string &new_mode) {
    this->mode = new_mode;
}

void Service::add(const std::string &location, const std::string &size, const int &aura_level, const int &separate_parts,
             const std::string &vision) {
    if (this->mode != GOD_MODE)
        throw ValueError("Invalid mode for the operation.");
    Turret turret(location, size, aura_level, separate_parts, vision);
    if (this->main_repository.has_turret(location))
        throw ValueError("A turret already exists in the given location.");
    this->main_repository.add(turret);
    this->reset_iterator();
}

void Service::remove(const std::string &location) {
    if (this->mode != GOD_MODE)
        throw ValueError("Invalid mode for the operation.");
    if (!this->main_repository.has_turret(location))
        throw ValueError("No turret exists in the given location.");
    this->main_repository.remove(location);
    if (this->saved_turrets_repository.has_turret(location))
        this->saved_turrets_repository.remove(location);
    this->reset_iterator();
}

void Service::update(const std::string &location, const std::string &new_size, const int &new_aura_level,
                     const int &new_separate_parts, const std::string &new_vision) {
    if (this->mode != GOD_MODE)
        throw ValueError("Invalid mode for the operation.");
    if (!this->main_repository.has_turret(location))
        throw ValueError("No turret exists in the given location.");
    this->main_repository.update(location, new_size, new_aura_level, new_separate_parts, new_vision);
}

void Service::reset_iterator() {
    this->iterator = 0;
}

Turret Service::next_turret() {
    if (this->mode != B_MODE)
        throw ValueError("Incorrect mode!");
    if (this->main_repository.get_turret_list().size() == 0)
        throw ValueError("There is no turret to show.");
    Turret to_return = this->main_repository.get_turret_list()[this->iterator];
    ++this->iterator;
    if (this->iterator == this->main_repository.get_turret_list().size())
        this->iterator = 0;
    return to_return;
}

void Service::save_turret(const std::string &location) {
    if (this->mode != B_MODE)
        throw ValueError("Incorrect mode!");
    if (!this->main_repository.has_turret(location))
        throw ValueError("The provided location is not associated to any turret.");
    if (this->saved_turrets_repository.has_turret(location))
        throw ValueError("The turret you wish to add already exists.");
    Turret turret = this->main_repository.find_turret(location);
    this->saved_turrets_repository.add(turret);
}

DynamicArray<Turret> &Service::get_turret_list() {
    return this->main_repository.get_turret_list();
}

DynamicArray<Turret> Service::get_turret_list_by_size_by_parts(const std::string &size, int parts) {
    if (this->mode != B_MODE)
        throw ValueError("Incorrect mode!");
    DynamicArray<Turret> turret_list;
    for (const auto &element : this->main_repository.get_turret_list())
        if (element.get_size() == size && element.get_separate_parts() >= parts)
            turret_list.add(element);
    return turret_list;
}

DynamicArray<Turret> &Service::get_saved_turrets_list() {
    if (this->mode != B_MODE)
        throw ValueError("Incorrect mode!");
    return this->saved_turrets_repository.get_turret_list();
}
