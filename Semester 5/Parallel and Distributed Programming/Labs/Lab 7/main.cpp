#include <mpi.h>
#include <vector>
#include <iostream>
#include <chrono>

#include "utils.h"
#include "globals.h"
#include "multiplications.h"
#include "poly_ops.h"
#include "mpi_comms.h"
#include "Int.h"

using namespace std;

template<typename T>
void actual_main(char **argv)
{
    if (id == 0) {
        vector<T> poly1 = generate_poly<T>((int) strtol(argv[1], nullptr, 10));
        vector<T> poly2 = generate_poly<T>((int) strtol(argv[2], nullptr, 10));

        chrono::high_resolution_clock::time_point beginTime = chrono::high_resolution_clock::now();
        vector<T> mpi_ans = which_mpi == 0 ? mpi_karatsuba(poly1, poly2) : mpi_naive(poly1, poly2);
        chrono::high_resolution_clock::time_point endTime = chrono::high_resolution_clock::now();
        string title;
        if (which_mpi == 0)
            title = "MPI Karatsuba";
        else title = "MPI Naive";
        println(title + " - " + to_string(chrono::duration_cast<chrono::milliseconds>(endTime-beginTime).count()) + " ms");

        beginTime = chrono::high_resolution_clock::now();
        vector<T> simple_karastuba_ans = karatsuba(poly1, poly2);
        endTime = chrono::high_resolution_clock::now();
        println("Simple Karatsuba - " + to_string(chrono::duration_cast<chrono::milliseconds>(endTime-beginTime).count()) + " ms");

        beginTime = chrono::high_resolution_clock::now();
        vector<T> simple_ans = naive(poly1, poly2);
        endTime = chrono::high_resolution_clock::now();
        println("Simple Naive - " + to_string(chrono::duration_cast<chrono::milliseconds>(endTime-beginTime).count()) + " ms");

        if (different(mpi_ans, simple_ans))
            println("wrong mpi karatsuba");
        if (different(simple_karastuba_ans, simple_ans))
            println("wrong simple karatsuba");
    } else {
        vector<T> x;
        vector<T> y;
        get_parameters(x, y);
        vector<T> ans = which_mpi == 0 ? mpi_karatsuba(x, y) : mpi_naive(x, y);
        send_to_parent(ans);
    }
}

int main(int argc, char** argv)
{
    if (argc < 3 || argc > 5) {
        cerr << "Invalid params\n";
        exit(1);
    }
    bool bigint = false;
    for (int i = 3; i < argc; ++i)
        if (strcmp(argv[i], "bigint") == 0)
            bigint = true;
        else if (strcmp(argv[i], "simple") == 0)
            set_which_mpi(1);
        else {
            cerr << "Invalid params\n";
            exit(1);
        }
    init();
    if (bigint)
        actual_main<Int>(argv);
    else
        actual_main<int>(argv);
    MPI_Finalize();
}
