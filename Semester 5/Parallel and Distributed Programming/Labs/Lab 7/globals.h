#ifndef LAB_7_GLOBALS_H
#define LAB_7_GLOBALS_H

extern int id;
extern int parent;
extern int procs;
extern int which_mpi; // 0 - karatsuba, 1 - naive
extern bool debug;

void set_which_mpi(int);
void set_id(int);
void set_parent(int);
void set_procs(int);

#endif //LAB_7_GLOBALS_H
