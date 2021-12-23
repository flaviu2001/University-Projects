#include <mpi.h>
#include <pthread.h>
#include <unistd.h>
#include <sstream>

#include "mpi_comms.h"
#include "DistributedSharedMemory.h"
#include "globals.h"
#include "utils.h"

pthread_mutex_t lock;
DistributedSharedMemory dsm; // NOLINT(cert-err58-cpp)

void* listener(void*)
{
    while (true) {
        println("waiting..");
        int *message;
        message = get_message();
        const char *var = nullptr;
        if (message[1] == 0)
            var = "a";
        else if (message[1] == 1)
            var = "b";
        else if (message[1] == 2)
            var = "c";
        if (message[0] == 0) {
            std::stringstream ss;
            ss << "updating " << var << " with " << message[2];
            println(ss.str());
            pthread_mutex_lock(&lock);
            dsm.set_variable(var, message[2]);
            pthread_mutex_unlock(&lock);
        } else if (message[0] == 1) {
            std::stringstream ss;
            ss << "subscribing " << var << " with " << message[2];
            println(ss.str());
            pthread_mutex_lock(&lock);
            dsm.update_subscription(var, message[2]);
            pthread_mutex_unlock(&lock);
        } else if (message[0] == 2) {
            println("quitting");
            break;
        }
        delete message;
    }
    return nullptr;
}

int main()
{
    MPI_Init(nullptr, nullptr);
    int id, procs_cnt;
    MPI_Comm_size(MPI_COMM_WORLD, &procs_cnt);
    MPI_Comm_rank(MPI_COMM_WORLD, &id);
    set_current_id(id);
    set_procs(procs_cnt);
    pthread_mutex_init(&lock, nullptr);
    pthread_t t;
    pthread_create(&t, nullptr, listener, &dsm);
    if (current_id == 0) {
        pthread_mutex_lock(&lock);
        dsm.subscribe("a");
        pthread_mutex_unlock(&lock);
        pthread_mutex_lock(&lock);
        dsm.subscribe("b");
        pthread_mutex_unlock(&lock);
        pthread_mutex_lock(&lock);
        dsm.subscribe("c");
        pthread_mutex_unlock(&lock);
        usleep(2000000);
        pthread_mutex_lock(&lock);
        dsm.update_variable("a", 2);
        pthread_mutex_unlock(&lock);
        pthread_mutex_lock(&lock);
        dsm.update_variable("c", 4);
        pthread_mutex_unlock(&lock);
        usleep(500000);
        pthread_mutex_lock(&lock);
        dsm.check_and_replace("c", 4, 6);
        pthread_mutex_unlock(&lock);
        DistributedSharedMemory::close();
    } else if (current_id == 1) {
        pthread_mutex_lock(&lock);
        dsm.subscribe("a");
        pthread_mutex_unlock(&lock);
        pthread_mutex_lock(&lock);
        dsm.subscribe("b");
        pthread_mutex_unlock(&lock);
        usleep(1000000);
        pthread_mutex_lock(&lock);
        dsm.update_variable("a", 6);
        pthread_mutex_unlock(&lock);
    } else if (current_id == 2) {
        pthread_mutex_lock(&lock);
        dsm.subscribe("c");
        pthread_mutex_unlock(&lock);
        usleep(1000000);
        pthread_mutex_lock(&lock);
        println(dsm.get_var("c"));
        pthread_mutex_unlock(&lock);
    }
    pthread_join(t, nullptr);
    println("a: " + std::to_string(dsm.get_var("a")));
    println("b: " + std::to_string(dsm.get_var("b")));
    println("c: " + std::to_string(dsm.get_var("c")));
    pthread_mutex_destroy(&lock);
    MPI_Finalize();
    return 0;
}
