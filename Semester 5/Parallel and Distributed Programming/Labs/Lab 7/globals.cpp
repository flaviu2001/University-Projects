#include "globals.h"

int id;
int parent;
int procs;
int which_mpi; // 0 - karatsuba, 1 - naive
bool debug = false;

void set_id(int new_id)
{
    id = new_id;
}

void set_parent(int new_parent)
{
    parent = new_parent;
}

void set_procs(int new_procs)
{
    procs = new_procs;
}

void set_which_mpi(int new_which_mpi)
{
    which_mpi = new_which_mpi;
}