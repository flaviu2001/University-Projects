#include <mpi.h>
#include <sstream>

#include "mpi_comms.h"
#include "utils.h"

void send_update_message(const char *var, int new_value, int id)
{
    int which = 0;
    if (strcmp(var, "a") == 0)
        which = 0;
    else if (strcmp(var, "b") == 0)
        which = 1;
    else if (strcmp(var, "c") == 0)
        which = 2;
    int arr[3] = {0, which, new_value};
    std::stringstream ss;
    ss << "sending " << arr[0] << " " << arr[1] << " " << arr[2] << " to " << id;
    println(ss.str());
    MPI_Bsend(arr, 3, MPI_INT, id, 0, MPI_COMM_WORLD);
}

void send_subscribe_message(const char *var, int new_id, int id)
{
    int which = 0;
    if (strcmp(var, "a") == 0)
        which = 0;
    else if (strcmp(var, "b") == 0)
        which = 1;
    else if (strcmp(var, "c") == 0)
        which = 2;
    int arr[3] = {1, which, new_id};
    std::stringstream ss;
    ss << "sending " << arr[0] << " " << arr[1] << " " << arr[2] << " to " << id;
    println(ss.str());
    MPI_Bsend(arr, 3, MPI_INT, id, 0, MPI_COMM_WORLD);
}

void send_close_message(int id)
{
    int arr[3] = {2, 0, 0};
    std::stringstream ss;
    ss << "sending " << arr[0] << " " << arr[1] << " " << arr[2] << " to " << id;
    println(ss.str());
    MPI_Bsend(arr, 3, MPI_INT, id, 0, MPI_COMM_WORLD);
}

int* get_message()
{
    int *arr = new int[3];
    MPI_Recv(arr, 3, MPI_INT, MPI_ANY_SOURCE, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    return arr;
}