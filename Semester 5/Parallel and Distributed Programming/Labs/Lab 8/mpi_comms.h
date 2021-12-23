#ifndef LAB_8_MPI_COMMS_H
#define LAB_8_MPI_COMMS_H

void send_update_message(const char *var, int new_value, int id);

void send_subscribe_message(const char *var, int new_id, int id);

void send_close_message(int id);

int* get_message();

#endif //LAB_8_MPI_COMMS_H
