#include <assert.h>
#include <string.h>
#include <stdlib.h>
#include "Service.h"

#include <stdio.h>
void test__service_add_gear__valid_input__added_to_list()
{
    Repository *repository = create_repository();
    Service *service = create_service(repository);
    char *const_state = "abc";
    char *const_type = "def";
    service_add_gear(service, 123, const_state, const_type, 456);
    int length;
    Gear* gear_list = service_get_gear_list(service, &length);
    assert(length == 1 &&
           get_catalogue_number(gear_list[0]) == 123 &&
           strcmp(get_state(gear_list[0]), const_state) == 0 &&
           strcmp(get_type(gear_list[0]), const_type) == 0 &&
           get_value(gear_list[0]) == 456);
    free(gear_list);
    destroy_service(&service);
}

void test__service_add_gear__duplicate_id__returns_error()
{
    Repository *repository = create_repository();
    Service *service = create_service(repository);
    char *const_state = "abc";
    char *const_type = "def";
    service_add_gear(service, 123, const_state, const_type, 456);
    int length;
    Gear* gear_list = service_get_gear_list(service, &length);
    assert(service_add_gear(service, 123, const_type, const_state, 910) == -1 && length == 1);
    free(gear_list);
    destroy_service(&service);
}

void test__service_remove_gear__valid_input__removed_from_list()
{
    Repository *repository = create_repository();
    Service *service = create_service(repository);
    char *const_state = "abc";
    char *const_type = "def";
    service_add_gear(service, 123, const_state, const_type, 456);
    int return_value = service_remove_gear(service, 123);
    int length;
    Gear* gear_list = service_get_gear_list(service, &length);
    assert(return_value == 0 && length == 0);
    free(gear_list);
    destroy_service(&service);
}

void test__service_remove_gear__nonexistent_id__returns_error()
{
    Repository *repository = create_repository();
    Service *service = create_service(repository);
    char *const_state = "abc";
    char *const_type = "def";

    service_add_gear(service, 123, const_state, const_type, 456);
    int return_value = service_remove_gear(service, 122);
    int length;
    Gear* gear_list = service_get_gear_list(service, &length);
    assert(return_value == -1 && length == 1);
    free(gear_list);
    destroy_service(&service);
}

void test__service_update_gear__valid_input__updates_gear()
{
    Repository *repository = create_repository();
    Service *service = create_service(repository);
    char *const_state = "abc";
    char *const_type = "def";
    service_add_gear(service, 123, const_state, const_type, 456);
    int length;
    int return_value = service_update_gear(service, 123, const_type, const_state, 987);
    Gear* gear_list = service_get_gear_list(service, &length);
    assert(return_value == 0 &&
           strcmp(get_state(gear_list[0]), const_type) == 0 &&
           strcmp(get_type(gear_list[0]), const_state) == 0 &&
           get_value(gear_list[0]) == 987);
    free(gear_list);
    destroy_service(&service);
}

void test__service_update_gear__nonexistent_id__returns_error()
{
    Repository *repository = create_repository();
    Service *service = create_service(repository);
    char *const_state = "abc";
    char *const_type = "def";
    service_add_gear(service, 123, const_state, const_type, 456);
    assert(service_update_gear(service, 122, const_type, const_state, 987) == -1);
    destroy_service(&service);
}

void test__service_get_gear_maximum_value_sorted_ascending__valid_input__filters_value()
{
    Repository *repository = create_repository();
    Service *service = create_service(repository);
    char *const_state_1 = "abc";
    char *const_state_2 = "def";
    char *const_type_1 = "zzz";

    service_add_gear(service, 1, const_state_1, const_type_1, 123);
    service_add_gear(service, 2, const_state_2, const_type_1, 123);
    service_add_gear(service, 3, const_state_1, const_type_1, 456);
    service_add_gear(service, 4, const_state_2, const_type_1, 456);

    int length;
    Gear *gear_list = service_get_gear_maximum_value_sorted_ascending(service, &length, 200);
    assert(length == 2 && get_catalogue_number(gear_list[0]) == 1 && get_catalogue_number(gear_list[1]) == 2);
    free(gear_list);
    destroy_service(&service);
}

void test__undo__update__success()
{
    Repository *repository = create_repository();
    Service *service = create_service(repository);

    service_add_gear(service, 123, "abc", "abc", 456);
    service_update_gear(service, 123, "def", "def", 456);
    service_undo(service);
    int length;
    Gear *gear_list = service_get_gear_list(service, &length);
    assert(strcmp(get_type(gear_list[0]), "abc") == 0 && strcmp(get_state(gear_list[0]), "abc") == 0);
    free(gear_list);
    destroy_service(&service);
}

void test__redo__update__success()
{
    Repository *repository = create_repository();
    Service *service = create_service(repository);
    char *const_state = "abc";
    char *const_type = "def";
    service_add_gear(service, 123, const_state, const_state, 456);
    service_update_gear(service, 123, const_type, const_type, 456);
    service_undo(service);
    service_redo(service);
    int length;
    Gear *gear_list = service_get_gear_list(service, &length);
    assert(strcmp(get_type(gear_list[0]), const_type) == 0 && strcmp(get_state(gear_list[0]), const_type) == 0);
    free(gear_list);
    destroy_service(&service);
}

void test__undo__add__success()
{
    Repository *repository = create_repository();
    Service *service = create_service(repository);
    char *const_state = "abc";
    char *const_type = "def";
    service_add_gear(service, 123, const_state, const_type, 456);
    service_undo(service);

    int length;
    Gear* gear_list = service_get_gear_list(service, &length);
    assert(length == 0);
    free(gear_list);
    destroy_service(&service);
}

void test__redo__add__success()
{
    Repository *repository = create_repository();
    Service *service = create_service(repository);
    char *const_state = "abc";
    char *const_type = "def";
    service_add_gear(service, 123, const_state, const_type, 456);
    service_undo(service);
    service_redo(service);
    int length;
    Gear *gear_list = service_get_gear_list(service, &length);
    assert(length == 1 && get_catalogue_number(gear_list[0]) == 123);
    free(gear_list);
    destroy_service(&service);
}

void test__undo__remove__success()
{
    Repository *repository = create_repository();
    Service *service = create_service(repository);
    char *const_state = "abc";
    char *const_type = "def";
    service_add_gear(service, 123, const_state, const_type, 456);
    service_remove_gear(service, 123);
    service_undo(service);
    int length;
    Gear* gear_list = service_get_gear_list(service, &length);
    assert(length == 1);
    free(gear_list);
    destroy_service(&service);
}

void test__redo__remove__success()
{
    Repository *repository = create_repository();
    Service *service = create_service(repository);
    char *const_state = "abc";
    char *const_type = "def";
    service_add_gear(service, 123, const_state, const_type, 456);
    service_remove_gear(service, 123);
    service_undo(service);
    service_redo(service);
    int length;
    Gear* gear_list = service_get_gear_list(service, &length);
    assert(length == 0);
    free(gear_list);
    destroy_service(&service);
}

void test__service_add_gear__two_entries__success()
{
    Repository *repository = create_repository();
    Service *service = create_service(repository);
    char *const_state = "abc";
    char *const_type = "def";
    service_add_gear(service, 123, const_state, const_type, 456);
    service_add_gear(service, 124, const_type, const_state, 345);
    int length;
    Gear* gear_list = service_get_gear_list(service, &length);
    assert(length == 2);
    free(gear_list);
    destroy_service(&service);
}

void test_all() {
    test__service_add_gear__valid_input__added_to_list();
    test__service_add_gear__duplicate_id__returns_error();
    test__service_remove_gear__valid_input__removed_from_list();
    test__service_remove_gear__nonexistent_id__returns_error();
    test__service_update_gear__valid_input__updates_gear();
    test__service_update_gear__nonexistent_id__returns_error();
    test__service_get_gear_maximum_value_sorted_ascending__valid_input__filters_value();
    test__undo__update__success();
    test__redo__update__success();
    test__undo__add__success();
    test__redo__add__success();
    test__undo__remove__success();
    test__redo__remove__success();
    test__service_add_gear__two_entries__success();
}
