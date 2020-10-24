#include "Repository.h"
#include "stdlib.h"

Repository* create_repository()
{
    Repository* repository = (Repository*)malloc(sizeof(Repository));
    if (repository == NULL)
        return NULL;
    repository->length = 0;
    repository->capacity = 1;
    repository->gear_list = (Gear*)malloc(repository->capacity*sizeof(Gear));
    return repository;
}

void destroy_repository(Repository **repository) {
    for (int i = 0; i < (*repository)->length; ++i){
        Gear* gear_list = (*repository)->gear_list;
        destroy_gear(gear_list[i]);
    }
    free((*repository)->gear_list);
    free(*repository);
    *repository = NULL;
}

Gear* get_gear_list(Repository *repository)
{
    return repository->gear_list;
}

void set_gear_list(Repository *repository, Gear *new_gear_list)
{
    repository->gear_list = new_gear_list;
}

int get_gear_list_length(Repository *repository)
{
    return repository->length;
}

void set_gear_list_length(Repository *repository, int new_length)
{
    repository->length = new_length;
}

int has_gear(Repository *repository, int catalogue_number)
{
    Gear* gear_list = repository->gear_list;
    for (int i = 0; i < repository->length; ++i)
        if (get_catalogue_number(gear_list[i]) == catalogue_number)
            return 1;
    return -1;
}

Gear return_gear(Repository *repository, int catalogue_number) {
    Gear* gear_list = repository->gear_list;
    Gear to_return;
    to_return.catalogue_number = -1;
    for (int i = 0; i < repository->length; ++i)
        if (get_catalogue_number(gear_list[i]) == catalogue_number){
            to_return = gear_list[i];
        }
    return to_return;
}

void add_gear(Repository *repository, Gear gear)
{
    if (repository->length == repository->capacity){
        repository->capacity *= 2;
        Gear *new_list = (Gear*)malloc(repository->capacity*sizeof(Gear));
        for (int i = 0; i < repository->length; ++i)
            new_list[i] = ((Gear*)repository->gear_list)[i];
        free(repository->gear_list);
        repository->gear_list = new_list;
    }
    ((Gear*)repository->gear_list)[repository->length] = gear;
    ++repository->length;
}

void remove_gear(Repository *repository, int catalogue_number)
{
    Gear* gear_list = repository->gear_list;
    int position = -1;
    for (int i = 0; i < repository->length; ++i)
        if (get_catalogue_number(gear_list[i]) == catalogue_number){
            position = i;
            break;
        }
    destroy_gear(gear_list[position]);
    for (int i = position; i < repository->length-1; ++i)
        gear_list[i] = gear_list[i+1];
    --repository->length;
}

void update_gear(Repository *repository, int catalogue_number, char* new_state, char* new_type, int new_value)
{
    Gear* gear_list = repository->gear_list;
    int position = -1;
    for (int i = 0; i < repository->length; ++i)
        if (get_catalogue_number(gear_list[i]) == catalogue_number){
            position = i;
            break;
        }
    set_state(&((Gear*)repository->gear_list)[position], new_state);
    set_type(&((Gear*)repository->gear_list)[position], new_type);
    set_value(&((Gear*)repository->gear_list)[position], new_value);
}
