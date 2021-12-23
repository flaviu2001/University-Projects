#include <cstring>

#include "DistributedSharedMemory.h"
#include "globals.h"
#include "mpi_comms.h"

Variable &DistributedSharedMemory::get_variable(const char *var) {
    if (strcmp(var, "a") == 0)
        return a;
    else if (strcmp(var, "b") == 0)
        return b;
    else if (strcmp(var, "c") == 0)
        return c;
    throw;
}

void DistributedSharedMemory::set_variable(const char *var, int new_value) {
    Variable &x = get_variable(var);
    x.set_value(new_value);
}

int DistributedSharedMemory::get_var(const char *var) {
    Variable &x = get_variable(var);
    int value = x.get_value();
    return value;
}

void DistributedSharedMemory::subscribe(const char *var) {
    for (int id = 0; id < procs; ++id)
        send_subscribe_message(var, current_id, id);
}

void DistributedSharedMemory::update_subscription(const char *var, int id) {
    Variable &x = get_variable(var);
    x.add_subscriber(id);
}

void DistributedSharedMemory::update_variable(const char *var, int new_value) {
    Variable &x = get_variable(var);
    for (int id : x.get_subscribers())
        send_update_message(var, new_value, id);
}

void DistributedSharedMemory::check_and_replace(const char *var, int old_value, int new_value) {
    Variable &x = get_variable(var);
    if (x.get_value() == old_value) {
        for (int id : x.get_subscribers())
            send_update_message(var, new_value, id);
    }
}

void DistributedSharedMemory::close() {
    for (int id = 0; id < procs; ++id)
        send_close_message(id);
}
