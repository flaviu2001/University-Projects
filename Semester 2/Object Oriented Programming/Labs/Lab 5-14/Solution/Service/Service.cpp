//
// Created by jack on 3/21/20.
//

#include "Service.h"
#include "../Utilities/Validator.h"
#include "../Utilities/Utils.h"
#include "../Repositories/RepositoryHTML.h"
#include "../Repositories/RepositorySQL.h"
#include <algorithm>

Service::Service(){
    this->main_repository = new RepositoryCSV;
    this->saved_turrets_repository = nullptr;
    this->mode = "";
    this->iterator = 0;
    this->stack_pointer = -1;
    this->mylist_pointer = -1;
}

Service::Service(Repository *main_repository) {
    this->main_repository = main_repository;
    this->saved_turrets_repository = nullptr;
    this->mode = "";
    this->iterator = 0;
    this->stack_pointer = -1;
    this->mylist_pointer = -1;
}

Service::Service(Repository *main_repository, Repository *saved_repository) {
    this->main_repository = main_repository;
    this->saved_turrets_repository = saved_repository;
    this->mode = "";
    this->iterator = 0;
    this->stack_pointer = -1;
    this->mylist_pointer = -1;
}

Service::Service(Service &other) {
    this->main_repository = other.main_repository;
    this->saved_turrets_repository = other.saved_turrets_repository;
    this->stack_pointer = -1;
    this->mylist_pointer = -1;
    this->mode = other.mode;
    this->iterator = other.iterator;
}

Service::~Service() {
    delete this->main_repository;
    delete this->saved_turrets_repository;
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
    this->prepare_main_stack();
    this->undo_stack.push_back(std::unique_ptr<Action>(new ActionAdd(this->main_repository, turret)));
    this->main_repository->add(turret);
    this->reset_iterator();
}

void Service::remove(const std::string &location) {
    Validator::validate_service_mode_a(*this);
    this->prepare_main_stack();
    this->undo_stack.push_back(std::unique_ptr<Action>(new ActionRemove(this->main_repository, this->main_repository->find_turret(location))));
    this->main_repository->remove(location);
    if (this->saved_turrets_repository != nullptr && this->saved_turrets_repository->has_turret(location))
        this->saved_turrets_repository->remove(location);
    this->reset_iterator();
}

void Service::update(const std::string &location, const std::string &new_size, const int &new_aura_level,
                     const int &new_separate_parts, const std::string &new_vision) {
    Validator::validate_service_mode_a(*this);
    this->prepare_main_stack();
    this->undo_stack.push_back(std::unique_ptr<Action>(new ActionUpdate(this->main_repository, this->main_repository->find_turret(location),
            new_size, new_aura_level, new_separate_parts, new_vision)));
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
    this->prepare_mylist_stack();
    this->mylist_stack.push_back(std::unique_ptr<Action>(new ActionSave(this->saved_turrets_repository, turret)));
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
    Validator::validate_service_saved_repository_exists(*this);
    return this->saved_turrets_repository->get_turret_list();
}

void Service::set_file_name_main_repository(const std::string &file_name_to_set) {
    this->main_repository->set_file_name(file_name_to_set);
    this->iterator = 0;
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

void Service::undo() {
    if (this->mode == A_MODE) {
        Validator::validate_service_undo(*this);
        this->undo_stack[this->stack_pointer]->execute_undo();
        --this->stack_pointer;
    }else if (this->mode == B_MODE){
        Validator::validate_service_undo_mylist(*this);
        this->mylist_stack[this->mylist_pointer]->execute_undo();
        --this->mylist_pointer;
    }
}

void Service::redo() {
    if (this->mode == A_MODE) {
        Validator::validate_service_redo(*this);
        ++this->stack_pointer;
        this->undo_stack[this->stack_pointer]->execute_redo();
    }else if (this->mode == B_MODE){
        Validator::validate_service_redo_mylist(*this);
        ++this->mylist_pointer;
        this->mylist_stack[this->mylist_pointer]->execute_redo();
    }
}

void Service::prepare_main_stack() {
    while (int(this->undo_stack.size())-1 > this->stack_pointer)
        this->undo_stack.pop_back();
    ++this->stack_pointer;
}

void Service::prepare_mylist_stack() {
    while (int(this->mylist_stack.size())-1 > this->mylist_pointer)
        this->mylist_stack.pop_back();
    ++this->mylist_pointer;
}

bool Service::is_repo_file() {
    return this->main_repository->is_file();
}

bool Service::is_saved_file() {
    return this->saved_turrets_repository->is_file();
}

std::string Service::get_file_name_main_repository() {
    return this->main_repository->get_file_name();
}

ActionAdd::ActionAdd(Repository *repository, const Turret& turret) {
    this->main_repository = repository;
    this->added_turret = turret;
}

void ActionAdd::execute_undo() {
    this->main_repository->remove(this->added_turret.get_location());
}

void ActionAdd::execute_redo() {
    this->main_repository->add(this->added_turret);
}

ActionRemove::ActionRemove(Repository *repository, const Turret& turret) {
    this->main_repository = repository;
    this->removed_turret = turret;
}

void ActionRemove::execute_undo() {
    this->main_repository->add(this->removed_turret);
}

void ActionRemove::execute_redo() {
    this->main_repository->remove(this->removed_turret.get_location());
}

ActionUpdate::ActionUpdate(Repository *repository, const Turret &old_turret, const std::string &new_size,
        const int &new_aura_level, const int &new_separate_parts, const std::string &new_vision) {
    this->main_repository = repository;
    this->old_turret = old_turret;
    this->new_size = new_size;
    this->new_aura_level = new_aura_level;
    this->new_separate_parts = new_separate_parts;
    this->new_vision = new_vision;
}

void ActionUpdate::execute_undo() {
    this->main_repository->update(this->old_turret.get_location(), this->old_turret.get_size(),
            this->old_turret.get_aura_level(), this->old_turret.get_separate_parts(), this->old_turret.get_vision());
}

void ActionUpdate::execute_redo() {
    this->main_repository->update(this->old_turret.get_location(), this->new_size, this->new_aura_level,
            this->new_separate_parts, this->new_vision);
}

ActionSave::ActionSave(Repository *repository, const Turret &turret) {
    this->main_repository = repository;
    this->saved_turret = turret;
}

void ActionSave::execute_undo() {
    this->main_repository->remove(saved_turret.get_location());
}

void ActionSave::execute_redo() {
    this->main_repository->add(saved_turret);
}
