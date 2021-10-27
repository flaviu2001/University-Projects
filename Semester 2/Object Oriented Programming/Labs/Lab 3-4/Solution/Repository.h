#ifndef IMRE_3_REPOSITORY_H
#define IMRE_3_REPOSITORY_H

#include "Gear.h"

typedef struct{
    void* gear_list;
    int length, capacity;
}Repository;

/**
 * Creates and empty repository and returns a pointer to it.
 */
Repository* create_repository();

/**
 * Returns a pointer towards an array of gears from a repository
 */
Gear* get_gear_list(Repository *repository);

/**
 * Sets the pointer of gears from the repository to a new value=
 */
void set_gear_list(Repository *repository, Gear* new_gear_list);

/**
 * Returns the length of the array from a repository
 */
int get_gear_list_length(Repository *repository);

/**
 * Sets the length of the array from a repository
 */
void set_gear_list_length(Repository *repository, int new_length);

/**
 * Destroys the repository and frees up the memory held by it.
 * @param: pointer to the repository(sent as another pointer) to be deleted
 */
void destroy_repository(Repository **repository);

/**
 * If the catalogue_number doesn't exist -1 is returned, else returns 1
 * @return non-negative index if search was successful, -1 otherwise
 */
int has_gear(Repository *repository, int catalogue_number);

/**
 * Returns the gear if catalogue_number exists, otherwise an invalid gear with catalogue_number = -1
 */
Gear return_gear(Repository *repository, int catalogue_number);

/**
 * Adds a gear to the repository
 * @param gear: gear to be added
 */
void add_gear(Repository *repository, Gear gear);

/**
 * Removes a gear from the repository
 * @param catalogue_number: catalogue number of the gear to be removed
 */
void remove_gear(Repository *repository, int catalogue_number);

/**
 * Updates a gear already present in the repository
 * @param catalogue_number: catalogue_number of gear to be updated
 * @param new_state: new state of the gear
 * @param new_type: new type of the gear
 * @param new_value: new value of the gear
 */
void update_gear(Repository *repository, int catalogue_number, char* new_state, char* new_type, int new_value);

#endif //IMRE_3_REPOSITORY_H
