#include "UI.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define COMMAND_LENGTH 100
#define DELIMITERS " ,\n"

UI* create_ui()
{
    UI *ui = (UI*)malloc(sizeof(UI));
    Repository *repository = create_repository();
    Service *new_service = create_service(repository);
    ui->service = new_service;
    return ui;
}

void start(UI *ui)
{
    char command[COMMAND_LENGTH];
    while (fgets(command, COMMAND_LENGTH, stdin)){
        char *argument = strtok(command, DELIMITERS), *useless_pointer;
        if (strcmp(argument, "add") == 0){

            /* Builds the catalogue_number */
            argument = strtok(NULL, DELIMITERS);
            int catalogue_number = (int)strtol(argument, &useless_pointer, 10);

            /* Builds the state */
            argument = strtok(NULL, DELIMITERS);
            char *state = argument;

            /* Builds the type */
            argument = strtok(NULL, DELIMITERS);
            char *type = argument;

            /* Builds the value */
            argument = strtok(NULL, DELIMITERS);
            int value = (int)strtol(argument, &useless_pointer, 10);

            if (service_add_gear(ui->service, catalogue_number, state, type, value) == -1)
                printf("No!\n");
        }else if (strcmp(argument, "delete") == 0){

            /* Builds the catalogue_number */
            argument = strtok(NULL, DELIMITERS);
            int catalogue_number = (int)strtol(argument, &useless_pointer, 10);

            if (service_remove_gear(ui->service, catalogue_number) == -1)
                printf("No!\n");
        }else if (strcmp(argument, "update") == 0){

            /* Builds the catalogue_number */
            argument = strtok(NULL, DELIMITERS);
            int catalogue_number = (int)strtol(argument, &useless_pointer, 10);

            /* Builds the new_state */
            argument = strtok(NULL, DELIMITERS);
            char *new_state = argument;

            /* Builds the new_type */
            argument = strtok(NULL, DELIMITERS);
            char *new_type = argument;

            /* Builds the new_value */
            argument = strtok(NULL, DELIMITERS);
            int new_value = (int)strtol(argument, &useless_pointer, 10);

            if (service_update_gear(ui->service, catalogue_number, new_state, new_type, new_value) == -1)
                printf("No!\n");
        }else if (strcmp(argument, "list") == 0) {
            argument = strtok(NULL, DELIMITERS);

            int length;
            Gear *list_to_iterate;
            if (argument == NULL)
                list_to_iterate = service_get_gear_list(ui->service, &length);
            else if (argument[0] >= 'a' && argument[0] <= 'z')    //list by type
                list_to_iterate = service_get_gear_list_filtered_by_type(ui->service, &length, argument);
            else/* if (argument[0] >= '0' && argument[0] <= '9')*/    //list by maximum value
                list_to_iterate = service_get_gear_maximum_value_sorted_ascending(ui->service, &length, (int)strtol(argument, &useless_pointer, 10));
            for (int i = 0; i < length; ++i){
                Gear gear = list_to_iterate[i];
                printf("%d %s %s %d\n", get_catalogue_number(gear), get_state(gear), get_type(gear), get_value(gear));
            }
            free(list_to_iterate);
        }else if (strcmp(argument, "list_of_state") == 0) {
            argument = strtok(NULL, DELIMITERS);

            int length;
            Gear *list_to_iterate = service_get_gear_list_filtered_by_state(ui->service, &length, argument);
            for (int i = 0; i < length; ++i){
                Gear gear = list_to_iterate[i];
                printf("%d %s %s %d\n", get_catalogue_number(gear), get_state(gear), get_type(gear), get_value(gear));
            }
            free(list_to_iterate);
        }else if (strcmp(argument, "list_descending") == 0) {
            argument = strtok(NULL, DELIMITERS);

            int length;
            Gear *list_to_iterate = service_get_gear_maximum_value_sorted_descending(ui->service, &length, (int)strtol(argument, &useless_pointer, 10));
            for (int i = 0; i < length; ++i){
                Gear gear = list_to_iterate[i];
                printf("%d %s %s %d\n", get_catalogue_number(gear), get_state(gear), get_type(gear), get_value(gear));
            }
            free(list_to_iterate);
        }else if (strcmp(argument, "undo") == 0){
            if (service_undo(ui->service) == -1)
                printf("No!\n");
        }else if (strcmp(argument, "redo") == 0){
            if (service_redo(ui->service) == -1)
                printf("No!\n");
        }else if (strcmp(argument, "exit") == 0)
            break;
    }
}

void destroy_ui(UI **ui)
{
    destroy_service(&(**ui).service);
    free(*ui);
    *ui = NULL;
}