#ifndef IMRE_3_UNDOLISTSOFOPERATIONS_H
#define IMRE_3_UNDOLISTSOFOPERATIONS_H

#include "Gear.h"

struct _Service;

typedef struct{
    char **undo_operation_list;
    char **redo_operation_list;
    Gear* gear_to_undo_list;
    Gear* gear_to_redo_list;
    int undo_length, undo_pointer, undo_capacity;
} Undo_Redo_Service;

Undo_Redo_Service* create_undoRedoService(struct _Service *service);
void destroy_undo(Undo_Redo_Service **undoRedoService);
void prepare_stack(struct _Service *service);
int undo(struct _Service *service);
int redo(struct _Service *service);


#endif