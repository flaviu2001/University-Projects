//#include "UndoListsOfLists.h"
//#include "stdlib.h"
//#include "Gear.h"
//#include "Repository.h"
//#include "Service.h"
//
//Undo_Redo_Service* create_undoRedoService(struct _Service *service){
//    Undo_Redo_Service* undoRedoService = (Undo_Redo_Service*)malloc(sizeof(Undo_Redo_Service));
//    undoRedoService->undo_length = 1;
//    undoRedoService->undo_capacity = 1;
//    undoRedoService->undo_pointer = 0;
//    undoRedoService->undo_list = malloc(undoRedoService->undo_capacity * sizeof(Gear *));
//    undoRedoService->undo_list_len = malloc(undoRedoService->undo_capacity * sizeof(int));
//
//    Gear* copy_of_gear_list = (Gear*)malloc(get_gear_list_length(service->repository) * sizeof(Gear));
//    for (int i = 0; i < get_gear_list_length(service->repository); ++i)
//        copy_of_gear_list[i] = gear_copy(get_gear_list(service->repository)[i]);
//    undoRedoService->undo_list_len[0] = get_gear_list_length(service->repository);
//    undoRedoService->undo_list[0] = copy_of_gear_list;
//
//    return undoRedoService;
//}
//
//void destroy_undo_list_at_position(Undo_Redo_Service *undoRedoService, int position)
//{
//    for (int i = 0; i < undoRedoService->undo_list_len[position]; ++i){
//        Gear current_gear = ((Gear**)undoRedoService->undo_list)[position][i];
//        destroy_gear(current_gear);
//    }
//    free(((Gear**)undoRedoService->undo_list)[position]);
//}
//
//void destroy_undo(Undo_Redo_Service **undoRedoService){
//    for (int i = 0; i < (**undoRedoService).undo_length; ++i)
//        destroy_undo_list_at_position(*undoRedoService, i);
//    free((**undoRedoService).undo_list);
//    free((**undoRedoService).undo_list_len);
//    free(*undoRedoService);
//    *undoRedoService = NULL;
//}
//
//void prepare_stack(Service *service) {
//    while (service->undoRedoService->undo_length > service->undoRedoService->undo_pointer + 1) {
//        destroy_undo_list_at_position(service->undoRedoService, service->undoRedoService->undo_length - 1);
//        --service->undoRedoService->undo_length;
//    }
//    if (service->undoRedoService->undo_length == service->undoRedoService->undo_capacity) {
//        service->undoRedoService->undo_capacity *= 2;
//        Gear **new_undo_list = (Gear **) malloc(service->undoRedoService->undo_capacity * sizeof(Gear *));
//        for (int i = 0; i < service->undoRedoService->undo_length; ++i)
//            new_undo_list[i] = service->undoRedoService->undo_list[i];
//        free(service->undoRedoService->undo_list);
//        service->undoRedoService->undo_list = (void **) new_undo_list;
//
//        int *new_undo_list_len = (int *) malloc(service->undoRedoService->undo_capacity * sizeof(int));
//        for (int i = 0; i < service->undoRedoService->undo_length; ++i)
//            new_undo_list_len[i] = service->undoRedoService->undo_list_len[i];
//        free(service->undoRedoService->undo_list_len);
//        service->undoRedoService->undo_list_len = new_undo_list_len;
//    }
//    service->undoRedoService->undo_list[service->undoRedoService->undo_length] = malloc(
//            get_gear_list_length(service->repository) * sizeof(Gear));
//    service->undoRedoService->undo_list_len[service->undoRedoService->undo_length] = get_gear_list_length(
//            service->repository);
//    for (int i = 0; i < service->undoRedoService->undo_list_len[service->undoRedoService->undo_length]; ++i)
//        ((Gear **) service->undoRedoService->undo_list)[service->undoRedoService->undo_length][i] = gear_copy(
//                get_gear_list(service->repository)[i]);
//    ++service->undoRedoService->undo_length;
//    service->undoRedoService->undo_pointer = service->undoRedoService->undo_length - 1;
//}
//
//void set_gear_list_to_current_pointer(Service* service)
//{
//    for (int i = 0; i < get_gear_list_length(service->repository); ++i){
//        Gear* gear_list = get_gear_list(service->repository);
//        destroy_gear(gear_list[i]);
//    }
//    free(get_gear_list(service->repository));
//
//    set_gear_list_length(service->repository, service->undoRedoService->undo_list_len[service->undoRedoService->undo_pointer]);
//    Gear* new_gear_list = (Gear*)malloc(service->undoRedoService->undo_list_len[service->undoRedoService->undo_pointer] * sizeof(Gear));
//    for (int i = 0; i < service->undoRedoService->undo_list_len[service->undoRedoService->undo_pointer]; ++i)
//        new_gear_list[i] = gear_copy(((Gear**)service->undoRedoService->undo_list)[service->undoRedoService->undo_pointer][i]);
//    set_gear_list(service->repository, new_gear_list);
//}
//
//int undo(Service* service)
//{
//    if (service->undoRedoService->undo_pointer == 0)
//        return -1;
//    --service->undoRedoService->undo_pointer;
//    set_gear_list_to_current_pointer(service);
//    return 0;
//}
//
//int redo(Service* service)
//{
//    if (service->undoRedoService->undo_pointer+1 == service->undoRedoService->undo_length)
//        return -1;
//    ++service->undoRedoService->undo_pointer;
//    set_gear_list_to_current_pointer(service);
//    return 0;
//}
