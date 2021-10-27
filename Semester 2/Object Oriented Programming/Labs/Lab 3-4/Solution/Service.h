#ifndef IMRE_3_SERVICE_H
#define IMRE_3_SERVICE_H

#include "Repository.h"
#include "UndoListsOfOperations.h"

struct _Service{
    Repository *repository;

    Undo_Redo_Service *undoRedoService;

    char *type_to_filter, *state_to_filter;
    int maximum_value_to_filer;

    char *inverse_of_last_operation;
    Gear gear_to_undo;
    char *last_operation;
    Gear gear_to_redo;
};
typedef struct _Service Service;

/**
 * Constructor for the service
 * @param repository: repository for the service
 * @return: pointer to created service
 */
Service* create_service(Repository *repository);

/**
 * Destroys the service and frees up the memory
 * @param service: service to destroy
 */
void destroy_service(Service** service);

/**
 * Adds a gear to the repository inside the service
 * @param catalogue_number: unique catalogue number
 * @param state: string of letters
 * @param type: string of letters
 * @param value: positive integer
 * @return: -1 if adding failed, 0 otherwise
 */
int service_add_gear(Service* service, int catalogue_number, char* state, char* type, int value);

/**
 * Updates the gear with the corresponding catalogue number from inside the repository in the service
 * @param catalogue_number: the target catalogue number
 * @param new_state: string
 * @param new_type: string
 * @param new_value: positive integer
 * @return: -1 if updating failed, 0 otherwise
 */
int service_update_gear(Service* service, int catalogue_number, char* new_state, char* new_type, int new_value);

/**
 * Removes a gear and frees up its memory from the repository
 * @param catalogue_number: target catalogue number
 * @return: -1 if removing failed, 0 otherwise
 */
int service_remove_gear(Service* service, int catalogue_number);

/**
 * Undoes the last operation executed.
 * Returns 0 if the operations succeeded, -1 otherwise
 */
int service_undo(Service* service);

/**
 * Redoes the last operation undone
 * Returns 0 if the operations succeeded, -1 otherwise
 */
int service_redo(Service* service);

/**
 * Returns a pointer towards the gear list inside the repository inside the service and
 * modifies the length accordingly
 * You also have to free the pointer returned by the function upon finishing using it.
 */
Gear* service_get_gear_list(Service* service, int *length);

/**
 * Returns a pointer towards a list of gears from the repository which have
 * the type equal to the provided one and modifies the length accordingly.
 * You also have to free the pointer returned by the function upon finishing using it.
 */
Gear* service_get_gear_list_filtered_by_type(Service *service, int *length, char *type);

/**
 * Returns a pointer towards a list of gears from the repository which have
 * the state equal to the provided one and modifies the length accordingly.
 * You also have to free the pointer returned by the function upon finishing using it.
 */
Gear* service_get_gear_list_filtered_by_state(Service *service, int *length, char *state);

/**
 * Returns a pointer towards a list of gears from the repository which
 * have value at most maximum_value and are sorted ascending by state.
 * You also have to free the pointer returned by the function upon finishing using it.
 */
Gear* service_get_gear_maximum_value_sorted_ascending(Service* service, int *length, int maximum_value);

/**
 * Returns a pointer towards a list of gears from the repository which
 * have value at most maximum_value and are sorted descending by state.
 * You also have to free the pointer returned by the function upon finishing using it.
 */
Gear* service_get_gear_maximum_value_sorted_descending(Service* service, int *length, int maximum_value);

#endif //IMRE_3_SERVICE_H
