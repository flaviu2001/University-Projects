#include <mpi.h>
#include <sstream>

#include "utils.h"
#include "globals.h"

using namespace std;

void println(const string& s)
{
    cout << id << ": " << s << "\n";
}

int get_parent_id()
{
    if (which_mpi == 0) {
        int new_parent = ((id + 2) - (id + 2) % 3 - 3) / 3;
        if (id >= 1 && id <= 3)
            new_parent = 0;
        if (id == 0)
            new_parent = -1;
        return new_parent;
    }
    int new_parent = ((id+3) - (id+3)%4 - 4)/4;
    if (id >= 1 && id <= 4)
        new_parent = 0;
    if (id == 0)
        new_parent = -1;
    return new_parent;
}

void init()
{
    MPI_Init(nullptr, nullptr);
    int new_id, new_procs;
    MPI_Comm_size(MPI_COMM_WORLD, &new_procs);
    MPI_Comm_rank(MPI_COMM_WORLD, &new_id);
    set_id(new_id);
    set_procs(new_procs);
    set_parent(get_parent_id());
    srand(time(nullptr)); // NOLINT(cert-msc51-cpp)
}