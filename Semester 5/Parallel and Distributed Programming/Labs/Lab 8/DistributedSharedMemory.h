#ifndef LAB_8_DISTRIBUTEDSHAREDMEMORY_H
#define LAB_8_DISTRIBUTEDSHAREDMEMORY_H


#include "Variable.h"

class DistributedSharedMemory {
public:
    Variable a;
    Variable b;
    Variable c;

    Variable& get_variable(const char *var);
public:
    void set_variable(const char *var, int new_value);
    int get_var(const char *var);
    void subscribe(const char *var);
    void update_subscription(const char *var, int id);
    void update_variable(const char *var, int new_value);
    void check_and_replace(const char *var, int old_value, int new_value);
    static void close();
};


#endif //LAB_8_DISTRIBUTEDSHAREDMEMORY_H
