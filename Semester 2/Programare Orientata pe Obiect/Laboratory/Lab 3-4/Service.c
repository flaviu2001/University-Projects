#include "Service.h"
#include "stdlib.h"
#include "string.h"

typedef int (*filter_function)(Service*, Gear);
typedef void (*sort_function)(Gear*, int);

Service* create_service(Repository *repository)
{
    Service* service = (Service*)malloc(sizeof(Service));
    if (service == NULL)
        return NULL;
    service->repository = repository;
    service->type_to_filter = NULL;
    service->state_to_filter = NULL;
    service->maximum_value_to_filer = 0;
    service->undoRedoService = create_undoRedoService(service);
    service->inverse_of_last_operation = NULL;
    service->last_operation = NULL;
    service->gear_to_undo.type = NULL;
    service->gear_to_undo.state = NULL;
    service->gear_to_redo.type = NULL;
    service->gear_to_redo.state = NULL;
    return service;
}

void destroy_service(Service **service)
{
    destroy_repository(&(**service).repository);
    destroy_undo(&(**service).undoRedoService);
    destroy_gear((**service).gear_to_undo);
    destroy_gear((**service).gear_to_redo);
    free(*service);
    *service = NULL;
}

int service_add_gear(Service* service, int catalogue_number, char* state, char* type, int value)
{
    if (has_gear(service->repository, catalogue_number) == 1)
        return -1;
    Gear gear = create_gear(catalogue_number, state, type, value);
    service->inverse_of_last_operation = "remove";
    service->last_operation = "add";
    destroy_gear(service->gear_to_undo);
    destroy_gear(service->gear_to_redo);
    service->gear_to_undo = gear_copy(gear);
    service->gear_to_redo = gear_copy(gear);

    add_gear(service->repository, gear);
    prepare_stack(service);
    return 0;
}

int service_update_gear(Service* service, int catalogue_number, char* new_state, char* new_type, int new_value)
{
    if (has_gear(service->repository, catalogue_number) == -1)
        return -1;

    service->inverse_of_last_operation = "update";
    service->last_operation = "update";
    destroy_gear(service->gear_to_undo);
    destroy_gear(service->gear_to_redo);
    service->gear_to_undo = gear_copy(return_gear(service->repository, catalogue_number));
    service->gear_to_redo = create_gear(catalogue_number, new_state, new_type, new_value);

    update_gear(service->repository, catalogue_number, new_state, new_type, new_value);
    prepare_stack(service);
    return 0;
}

int service_remove_gear(Service* service, int catalogue_number)
{
    if (has_gear(service->repository, catalogue_number) == -1)
        return -1;

    service->inverse_of_last_operation = "add";
    service->last_operation = "remove";
    destroy_gear(service->gear_to_undo);
    destroy_gear(service->gear_to_redo);
    service->gear_to_undo = gear_copy(return_gear(service->repository, catalogue_number));
    service->gear_to_redo = gear_copy(return_gear(service->repository, catalogue_number));

    remove_gear(service->repository, catalogue_number);
    prepare_stack(service);
    return 0;
}

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunused-parameter"
int always_true_filter(Service *service, Gear gear)
{
    return 1;
}
#pragma clang diagnostic pop

int type_filter(Service *service, Gear gear)
{
    if (strcmp(service->type_to_filter, gear.type) == 0)
        return 1;
    return 0;
}

int state_filter(Service *service, Gear gear)
{
    if (strcmp(service->state_to_filter, gear.state) == 0)
        return 1;
    return 0;
}

int maximum_value_filter(Service *service, Gear gear)
{
    if (gear.value < service->maximum_value_to_filer)
        return 1;
    return 0;
}

void ascending_sort(Gear *gear_list, int length)
{
    int finished_sorting = 0;
    while (finished_sorting == 0){
        finished_sorting = 1;
        for (int i = 1; i < length; ++i)
            if (strcmp(get_state(gear_list[i-1]), get_state(gear_list[i])) > 0){
                finished_sorting = 0;
                Gear auxiliary_gear = gear_list[i-1];
                gear_list[i-1] = gear_list[i];
                gear_list[i] = auxiliary_gear;
            }
    }
}

void descending_sort(Gear *gear_list, int length)
{
    ascending_sort(gear_list, length);
    int i = 0, j = length-1;
    while (i < j){
        Gear aux = gear_list[i];
        gear_list[i] = gear_list[j];
        gear_list[j] = aux;
        ++i, --j;
    }
}

Gear* service_get_filtered_gear_list(Service *service, int *length, filter_function function)
{
    *length = 0;
    int capacity = 1;
    Gear *list_to_return = (Gear*)malloc(capacity*sizeof(Gear));
    for (int i = 0; i < get_gear_list_length(service->repository); ++i)
        if (function(service, get_gear_list(service->repository)[i]) == 1){
            if (*length == capacity){
                capacity *= 2;
                Gear *new_list = (Gear*)malloc(capacity*sizeof(Gear));
                for (int j = 0; j < *length; ++j)
                    new_list[j] = list_to_return[j];
                free(list_to_return);
                list_to_return = new_list;
            }
            list_to_return[*length] = get_gear_list(service->repository)[i];
            ++(*length);
        }
    return list_to_return;
}

Gear* service_get_gear_list(Service *service, int *length) {
    return service_get_filtered_gear_list(service, length, always_true_filter);
}

Gear* service_get_gear_list_filtered_by_type(Service *service, int *length, char *type) {
    service->type_to_filter = type;
    return service_get_filtered_gear_list(service, length, type_filter);
}

Gear* service_get_gear_list_filtered_by_state(Service *service, int *length, char *state) {
    service->state_to_filter = state;
    return service_get_filtered_gear_list(service, length, state_filter);
}

Gear *service_get_gear_maximum_value(Service *service, int *length, int maximum_value, sort_function function) {
    service->maximum_value_to_filer = maximum_value;
    Gear *gear_list = service_get_filtered_gear_list(service, length, maximum_value_filter);
    function(gear_list, *length);
    return gear_list;
}

Gear *service_get_gear_maximum_value_sorted_ascending(Service *service, int *length, int maximum_value) {
    return service_get_gear_maximum_value(service, length, maximum_value, ascending_sort);
}

Gear *service_get_gear_maximum_value_sorted_descending(Service *service, int *length, int maximum_value) {
    return service_get_gear_maximum_value(service, length, maximum_value, descending_sort);
}

int service_undo(Service *service) {
    return undo(service);
}

int service_redo(Service *service) {
    return redo(service);
}
