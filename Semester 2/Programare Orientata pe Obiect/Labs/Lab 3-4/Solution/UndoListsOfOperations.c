#include "UndoListsOfOperations.h"
#include "stdlib.h"
#include "Service.h"
#include "string.h"

Undo_Redo_Service* create_undoRedoService(struct _Service *service){
    Undo_Redo_Service* undoRedoService = (Undo_Redo_Service*)malloc(sizeof(Undo_Redo_Service));
    undoRedoService->undo_capacity = 1;
    undoRedoService->undo_length = 0;
    undoRedoService->undo_pointer = -1;
    undoRedoService->undo_operation_list = (char**)malloc(undoRedoService->undo_capacity*sizeof(char*));
    undoRedoService->redo_operation_list = (char**)malloc(undoRedoService->undo_capacity*sizeof(char*));
    undoRedoService->gear_to_undo_list = (Gear*)malloc(undoRedoService->undo_capacity*sizeof(Gear));
    undoRedoService->gear_to_redo_list = (Gear*)malloc(undoRedoService->undo_capacity*sizeof(Gear));
    return undoRedoService;
}

void destroy_undo(Undo_Redo_Service **undoRedoService){
    free((**undoRedoService).undo_operation_list);
    free((**undoRedoService).redo_operation_list);
    for (int i = 0; i < (**undoRedoService).undo_length; ++i) {
        destroy_gear((**undoRedoService).gear_to_undo_list[i]);
        destroy_gear((**undoRedoService).gear_to_redo_list[i]);
    }
    free((**undoRedoService).gear_to_undo_list);
    free((**undoRedoService).gear_to_redo_list);
    free(*undoRedoService);
    *undoRedoService = NULL;
}

void prepare_stack(struct _Service *service) {
    service->undoRedoService->undo_length = service->undoRedoService->undo_pointer + 1;
    if (service->undoRedoService->undo_length == service->undoRedoService->undo_pointer + 1) {
        service->undoRedoService->undo_capacity *= 2;
        char **new_undo_operation_list = (char **) malloc(service->undoRedoService->undo_capacity * sizeof(char *));
        char **new_redo_operation_list = (char **) malloc(service->undoRedoService->undo_capacity * sizeof(char *));
        Gear *new_gear_to_undo_list = (Gear *) malloc(service->undoRedoService->undo_capacity * sizeof(Gear));
        Gear *new_gear_to_redo_list = (Gear *) malloc(service->undoRedoService->undo_capacity * sizeof(Gear));
        for (int i = 0; i < service->undoRedoService->undo_length; ++i) {
            new_undo_operation_list[i] = service->undoRedoService->undo_operation_list[i];
            new_redo_operation_list[i] = service->undoRedoService->redo_operation_list[i];
            new_gear_to_undo_list[i] = service->undoRedoService->gear_to_undo_list[i];
            new_gear_to_redo_list[i] = service->undoRedoService->gear_to_redo_list[i];
        }
        free(service->undoRedoService->undo_operation_list);
        free(service->undoRedoService->redo_operation_list);
        free(service->undoRedoService->gear_to_undo_list);
        free(service->undoRedoService->gear_to_redo_list);
        service->undoRedoService->undo_operation_list = new_undo_operation_list;
        service->undoRedoService->redo_operation_list = new_redo_operation_list;
        service->undoRedoService->gear_to_undo_list = new_gear_to_undo_list;
        service->undoRedoService->gear_to_redo_list = new_gear_to_redo_list;
    }
    ++service->undoRedoService->undo_length;
    service->undoRedoService->undo_pointer = service->undoRedoService->undo_length-1;
    int pointer = service->undoRedoService->undo_pointer;
    service->undoRedoService->undo_operation_list[pointer] = service->inverse_of_last_operation;
    service->undoRedoService->redo_operation_list[pointer] = service->last_operation;
    service->undoRedoService->gear_to_undo_list[pointer] = gear_copy(service->gear_to_undo);
    service->undoRedoService->gear_to_redo_list[pointer] = gear_copy(service->gear_to_redo);
}

int undo(struct _Service *service) {
    if (service->undoRedoService->undo_pointer == -1)
        return -1;
    int pointer = service->undoRedoService->undo_pointer;
    if (strcmp(service->undoRedoService->undo_operation_list[pointer], "add") == 0)
        add_gear(service->repository, gear_copy(service->undoRedoService->gear_to_undo_list[pointer]));
    else if (strcmp(service->undoRedoService->undo_operation_list[pointer], "update") == 0) {
        Gear gear = service->undoRedoService->gear_to_undo_list[pointer];
        update_gear(service->repository, get_catalogue_number(gear), get_state(gear), get_type(gear), get_value(gear));
    }else if (strcmp(service->undoRedoService->undo_operation_list[pointer], "remove") == 0)
        remove_gear(service->repository, get_catalogue_number(service->undoRedoService->gear_to_undo_list[pointer]));
    --service->undoRedoService->undo_pointer;
    return 0;
}

int redo(struct _Service *service) {
    if (service->undoRedoService->undo_pointer+1 == service->undoRedoService->undo_length)
        return -1;
    ++service->undoRedoService->undo_pointer;
    int pointer = service->undoRedoService->undo_pointer;
    if (strcmp(service->undoRedoService->redo_operation_list[pointer], "add") == 0)
        add_gear(service->repository, gear_copy(service->undoRedoService->gear_to_redo_list[pointer]));
    else if (strcmp(service->undoRedoService->redo_operation_list[pointer], "update") == 0) {
        Gear gear = service->undoRedoService->gear_to_redo_list[pointer];
        update_gear(service->repository, get_catalogue_number(gear), get_state(gear), get_type(gear), get_value(gear));
    }else if (strcmp(service->undoRedoService->redo_operation_list[pointer], "remove") == 0)
        remove_gear(service->repository, get_catalogue_number(service->undoRedoService->gear_to_redo_list[pointer]));
    return 0;
}
