//
// Created by jack on 4/22/20.
//

#ifndef IMRE5_VALIDATOR_H
#define IMRE5_VALIDATOR_H


#include "../Repositories/RepositoryMemory.h"
#include "../Service/Service.h"

class Validator {
public:
    static void validate_turret_fields(const std::string &location, const std::string &turret_size, const std::string &number1, const std::string &number2, const std::string &vision);
    static void validate_repository_add(Repository *repository, const std::string &location);
    static void validate_repository_remove(Repository *repository, const std::string &location);
    static void validate_repository_update(Repository *repository, const std::string &location);
    static void validate_repository_find(Repository *repository, const std::string &location);
    static void validate_repository_file_existence(Repository *repositoryFile);
    static void validate_service_mode_a(Service &service);
    static void validate_service_mode_b(Service &service);
    static void validate_service_next_turret(Service &service);
    static void validate_service_save_turret(Service &service, const std::string &location);
    static void validate_service_saved_repository_exists(Service &service);
    static void validate_service_undo(Service &service);
    static void validate_service_redo(Service &service);
    static void validate_service_undo_mylist(Service &service);
    static void validate_service_redo_mylist(Service &service);
};


#endif //IMRE5_VALIDATOR_H
