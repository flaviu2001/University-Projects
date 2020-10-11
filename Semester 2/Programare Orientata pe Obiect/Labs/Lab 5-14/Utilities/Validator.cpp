//
// Created by jack on 4/22/20.
//

#include "Validator.h"
#include "Exceptions.h"

void Validator::validate_repository_add(Repository *repository, const std::string &location) {
    if (repository->has_turret(location))
        throw ValueError("A turret already exists in the given location.");
}

void Validator::validate_repository_remove(Repository *repository, const std::string &location) {
    if (!repository->has_turret(location))
        throw ValueError("No turret exists in the given location.");
}

void Validator::validate_repository_update(Repository *repository, const std::string &location) {
    if (!repository->has_turret(location))
        throw ValueError("No turret exists in the given location.");
}

void Validator::validate_repository_find(Repository *repository, const std::string &location) {
    if (!repository->has_turret(location))
        throw ValueError("Turret not found.");
}

void Validator::validate_repository_file_existence(Repository *repository) {
    if (repository->get_file_name().empty())
        throw ValueError("The file name cannot be empty. (Perhaps you forgot to initialize it)");
}

void Validator::validate_service_mode_a(Service &service) {
    if (service.get_mode() != A_MODE)
        throw ModeError("Incorrect mode!");
}

void Validator::validate_service_mode_b(Service &service) {
    if (service.get_mode() != B_MODE)
        throw ModeError("Incorrect mode!");
}

void Validator::validate_turret_fields(const std::string &location, const std::string &turret_size, const std::string &number1,
                                       const std::string &number2, const std::string &vision) {
    for (auto x : number1)
        if (x > '9' or x < '0')
            throw ValueError("Aura level is not a valid number.");
    for (auto x : number2)
        if (x > '9' or x < '0')
            throw ValueError("Separate parts is not a valid number.");
    if (location.empty() || turret_size.empty() || number1.empty() || number2.empty() || vision.empty())
        throw ValueError("Cannot have empty fields.");
}

void Validator::validate_service_next_turret(Service &service) {
    if (service.main_repository->get_turret_list().empty())
        throw ValueError("There is no turret to show.");
}

void Validator::validate_service_save_turret(Service &service, const std::string &location) {
    if (!service.main_repository->has_turret(location))
        throw ValueError("The provided location is not associated to any turret.");
    if (service.saved_turrets_repository->has_turret(location))
        throw ValueError("The turret you wish to add already exists.");
}

void Validator::validate_service_saved_repository_exists(Service &service) {
    if (service.saved_turrets_repository == nullptr)
        throw RepositoryError("Repository not initialised.");
}

void Validator::validate_service_undo(Service &service) {
    if (service.stack_pointer < 0)
        throw ValueError("No more operations to undo.");
}

void Validator::validate_service_redo(Service &service) {
    if (service.stack_pointer+1 >= int(service.undo_stack.size()))
        throw ValueError("No more operations to undo.");
}

void Validator::validate_service_undo_mylist(Service &service) {
    if (service.mylist_pointer < 0)
        throw ValueError("No more operations to undo.");
}

void Validator::validate_service_redo_mylist(Service &service) {
    if (service.mylist_pointer+1 >= int(service.mylist_stack.size()))
        throw ValueError("No more operations to undo.");
}
