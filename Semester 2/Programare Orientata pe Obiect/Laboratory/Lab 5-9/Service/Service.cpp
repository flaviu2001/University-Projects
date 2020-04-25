//
// Created by jack on 3/21/20.
//

#include "Service.h"
#include "../Utilities/Exceptions.h"
#include "../Utilities/Validator.h"
#include "../Utilities/Utils.h"
#include "../Repositories/RepositoryHTML.h"
#include "../Repositories/RepositorySQL.h"
#include <algorithm>

Service::Service(Repository *repository){
    this->main_repository = repository;
    this->saved_turrets_repository = nullptr;
    this->mode = "";
    this->iterator = 0;
}

Service::Service(Repository *repository, Repository *repository_saved) {
    this->main_repository = repository;
    this->saved_turrets_repository = repository_saved;
    this->mode = "";
    this->iterator = 0;
}

std::string Service::get_mode() {
    return this->mode;
}

void Service::set_mode(const std::string &new_mode) {
    this->mode = new_mode;
}

void Service::add(const std::string &location, const std::string &size, const int &aura_level, const int &separate_parts,
             const std::string &vision) {
    Validator::validate_service_mode_a(*this);
    Turret turret(location, size, aura_level, separate_parts, vision);
    this->main_repository->add(turret);
    this->reset_iterator();
}

void Service::remove(const std::string &location) {
    Validator::validate_service_mode_a(*this);
    this->main_repository->remove(location);
    if (this->saved_turrets_repository != nullptr && this->saved_turrets_repository->has_turret(location))
        this->saved_turrets_repository->remove(location);
    this->reset_iterator();
}

void Service::update(const std::string &location, const std::string &new_size, const int &new_aura_level,
                     const int &new_separate_parts, const std::string &new_vision) {
    Validator::validate_service_mode_a(*this);
    this->main_repository->update(location, new_size, new_aura_level, new_separate_parts, new_vision);
    if (this->saved_turrets_repository != nullptr && this->saved_turrets_repository->has_turret(location))
        this->saved_turrets_repository->update(location, new_size, new_aura_level, new_separate_parts, new_vision);
}

void Service::reset_iterator() {
    this->iterator = 0;
}

Turret Service::next_turret() {
    Validator::validate_service_mode_b(*this);
    Validator::validate_service_next_turret(*this);
    Turret to_return = this->main_repository->get_turret_list()[this->iterator];
    ++this->iterator;
    if (this->iterator == int(this->main_repository->get_turret_list().size()))
        this->iterator = 0;
    return to_return;
}

void Service::save_turret(const std::string &location) {
    Validator::validate_service_mode_b(*this);
    Validator::validate_service_save_turret(*this, location);
    Turret turret = this->main_repository->find_turret(location);
    this->saved_turrets_repository->add(turret);
}

std::vector<Turret> Service::get_turret_list() {
    return this->main_repository->get_turret_list();
}

std::vector<Turret> Service::get_turret_list_by_size_by_parts(const std::string &size, int parts) {
    Validator::validate_service_mode_b(*this);
    std::vector<Turret> turret_list, main_list = this->main_repository->get_turret_list();
    std::for_each(main_list.begin(), main_list.end(), [&turret_list, &size, parts](const Turret& turret){
        if (turret.get_size() == size && turret.get_separate_parts() >= parts)
            turret_list.push_back(turret);
    });
    return turret_list;
}

std::vector<Turret> Service::get_saved_turrets_list() {
    Validator::validate_service_mode_b(*this);
    Validator::validate_service_saved_repository_exists(*this);
    Validator::validate_service_saved_repository_exists(*this);
    return this->saved_turrets_repository->get_turret_list();
}

void Service::set_file_name_main_repository(const std::string &file_name_to_set) {
    this->main_repository->set_file_name(file_name_to_set);
}

void Service::set_file_name_saved_turrets_repository(const std::string &file_name_to_set) {
    if (termination(file_name_to_set) == "sql")
        this->saved_turrets_repository = new RepositorySQL;
    else if (termination(file_name_to_set) == "html")
        this->saved_turrets_repository = new RepositoryHTML;
    else this->saved_turrets_repository = new RepositoryCSV;
    this->saved_turrets_repository->set_file_name(file_name_to_set);
}

std::string Service::get_file_name_saved_turrets_repository() {
    Validator::validate_service_saved_repository_exists(*this);
    return this->saved_turrets_repository->get_file_name();
}

Service::~Service() {
    delete this->saved_turrets_repository;
}
