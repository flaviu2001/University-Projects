#ifndef LAB_7_MPI_COMMS_H
#define LAB_7_MPI_COMMS_H

#include <mpi.h>
#include <vector>

#include "globals.h"
#include "Int.h"
#include "utils.h"

using namespace std;

const int tag_x_len = 0;
const int tag_x_arr = 1;
const int tag_y_len = 2;
const int tag_y_arr = 3;
const int tag_ans_len = 4;
const int tag_ans_arr = 5;
const int tag_x_len_bigint = 10;
const int tag_x_number_size_bigint = 11;
const int tag_x_number_string_bigint = 12;
const int tag_y_len_bigint = 13;
const int tag_y_number_size_bigint = 14;
const int tag_y_number_string_bigint = 15;
const int tag_ans_len_bigint = 16;
const int tag_ans_number_size_bigint = 17;
const int tag_ans_number_string_bigint = 18;

template<typename T>
void get_parameters(vector<T> &x, vector<T> &y);

template<typename T>
void call_child_send(const vector<T> &x, const vector<T> &y, int child_id);

template<typename T>
vector<T> call_child_recv(int child_id);

template<typename T>
void send_to_parent(const vector<T> &poly);

template<>
void get_parameters<int>(vector<int> &x, vector<int> &y)
{
    MPI_Status status;
    int x_len, y_len;
    MPI_Recv(&x_len, 1, MPI_INT, parent, tag_x_len, MPI_COMM_WORLD, &status);
    x.clear();
    x.resize(x_len);
    MPI_Recv(x.data(), x_len, MPI_INT, parent, tag_x_arr, MPI_COMM_WORLD, &status);
    MPI_Recv(&y_len, 1, MPI_INT, parent, tag_y_len, MPI_COMM_WORLD, &status);
    y.clear();
    y.resize(y_len);
    MPI_Recv(y.data(), y_len, MPI_INT, parent, tag_y_arr, MPI_COMM_WORLD, &status);
}

template<>
void get_parameters<float>(vector<float> &x, vector<float> &y)
{
    MPI_Status status;
    int x_len, y_len;
    MPI_Recv(&x_len, 1, MPI_INT, parent, tag_x_len, MPI_COMM_WORLD, &status);
    x.clear();
    x.resize(x_len);
    MPI_Recv(x.data(), x_len, MPI_FLOAT, parent, tag_x_arr, MPI_COMM_WORLD, &status);
    MPI_Recv(&y_len, 1, MPI_INT, parent, tag_y_len, MPI_COMM_WORLD, &status);
    y.clear();
    y.resize(y_len);
    MPI_Recv(y.data(), y_len, MPI_FLOAT, parent, tag_y_arr, MPI_COMM_WORLD, &status);
}

template<>
void get_parameters<Int>(vector<Int> &x, vector<Int> &y)
{
    MPI_Status status;
    int x_len, y_len;
    MPI_Recv(&x_len, 1, MPI_INT, parent, tag_x_len_bigint, MPI_COMM_WORLD, &status);
    x.clear();
    x.resize(x_len);
    for (int i = 0; i < x_len; ++i) {
        int number_len;
        MPI_Recv(&number_len, 1, MPI_INT, parent, tag_x_number_size_bigint, MPI_COMM_WORLD, &status);
        string number;
        number.resize(number_len);
        MPI_Recv(number.data(), number_len, MPI_CHAR, parent, tag_x_number_string_bigint, MPI_COMM_WORLD, &status);
        x[i] = Int(number.c_str());
    }
    MPI_Recv(&y_len, 1, MPI_INT, parent, tag_y_len_bigint, MPI_COMM_WORLD, &status);
    y.clear();
    y.resize(y_len);
    for (int i = 0; i < y_len; ++i) {
        int number_len;
        MPI_Recv(&number_len, 1, MPI_INT, parent, tag_y_number_size_bigint, MPI_COMM_WORLD, &status);
        string number;
        number.resize(number_len);
        MPI_Recv(number.data(), number_len, MPI_CHAR, parent, tag_y_number_string_bigint, MPI_COMM_WORLD, &status);
        y[i] = Int(number.c_str());
    }
}

template<>
void call_child_send<int>(const vector<int> &x, const vector<int> &y, int child_id)
{
    int x_len = (int)x.size();
    int y_len = (int)y.size();
    MPI_Ssend(&x_len, 1, MPI_INT, child_id, tag_x_len, MPI_COMM_WORLD);
    MPI_Ssend(x.data(), (int)x.size(), MPI_INT, child_id, tag_x_arr, MPI_COMM_WORLD);
    MPI_Ssend(&y_len, 1, MPI_INT, child_id, tag_y_len, MPI_COMM_WORLD);
    MPI_Ssend(y.data(), (int)y.size(), MPI_INT, child_id, tag_y_arr, MPI_COMM_WORLD);
}

template<>
void call_child_send<float>(const vector<float> &x, const vector<float> &y, int child_id)
{
    int x_len = (int)x.size();
    int y_len = (int)y.size();
    MPI_Ssend(&x_len, 1, MPI_INT, child_id, tag_x_len, MPI_COMM_WORLD);
    MPI_Ssend(x.data(), (int)x.size(), MPI_FLOAT, child_id, tag_x_arr, MPI_COMM_WORLD);
    MPI_Ssend(&y_len, 1, MPI_INT, child_id, tag_y_len, MPI_COMM_WORLD);
    MPI_Ssend(y.data(), (int)y.size(), MPI_FLOAT, child_id, tag_y_arr, MPI_COMM_WORLD);
}

template<>
void call_child_send<Int>(const vector<Int> &x, const vector<Int> &y, int child_id)
{
    int x_len = (int)x.size();
    int y_len = (int)y.size();
    MPI_Ssend(&x_len, 1, MPI_INT, child_id, tag_x_len_bigint, MPI_COMM_WORLD);
    for (int i = 0; i < x_len; ++i) {
        string number_string = x[i].to_string();
        int number_len = (int)number_string.length();
        MPI_Ssend(&number_len, 1, MPI_INT, child_id, tag_x_number_size_bigint, MPI_COMM_WORLD);
        MPI_Ssend(number_string.data(), number_len, MPI_CHAR, child_id, tag_x_number_string_bigint, MPI_COMM_WORLD);
    }
    MPI_Ssend(&y_len, 1, MPI_INT, child_id, tag_y_len_bigint, MPI_COMM_WORLD);
    for (int i = 0; i < y_len; ++i) {
        string number_string = y[i].to_string();
        int number_len = (int)number_string.length();
        MPI_Ssend(&number_len, 1, MPI_INT, child_id, tag_y_number_size_bigint, MPI_COMM_WORLD);
        MPI_Ssend(number_string.data(), number_len, MPI_CHAR, child_id, tag_y_number_string_bigint, MPI_COMM_WORLD);
    }
}

template<>
vector<int> call_child_recv<int>(int child_id)
{
    int ans_len;
    MPI_Status status;
    MPI_Recv(&ans_len, 1, MPI_INT, child_id, tag_ans_len, MPI_COMM_WORLD, &status);
    vector<int> ans = vector<int>(ans_len);
    MPI_Recv(ans.data(), ans_len, MPI_INT, child_id, tag_ans_arr, MPI_COMM_WORLD, &status);
    return ans;
}

template<>
vector<float> call_child_recv<float>(int child_id)
{
    int ans_len;
    MPI_Status status;
    MPI_Recv(&ans_len, 1, MPI_INT, child_id, tag_ans_len, MPI_COMM_WORLD, &status);
    vector<float> ans = vector<float>(ans_len);
    MPI_Recv(ans.data(), ans_len, MPI_FLOAT, child_id, tag_ans_arr, MPI_COMM_WORLD, &status);
    return ans;
}

template<>
vector<Int> call_child_recv<Int>(int child_id)
{
    int ans_len;
    MPI_Status status;
    MPI_Recv(&ans_len, 1, MPI_INT, child_id, tag_ans_len_bigint, MPI_COMM_WORLD, &status);
    vector<Int> ans = vector<Int>(ans_len);
    for (int i = 0; i < ans_len; ++i) {
        int number_len;
        MPI_Recv(&number_len, 1, MPI_INT, child_id, tag_ans_number_size_bigint, MPI_COMM_WORLD, &status);
        string number;
        number.resize(number_len);
        MPI_Recv(number.data(), number_len, MPI_CHAR, child_id, tag_ans_number_string_bigint, MPI_COMM_WORLD, &status);
        ans[i] = Int(number.c_str());
    }
    return ans;
}

template<>
void send_to_parent<int>(const vector<int> &poly)
{
    int len = (int)poly.size();
    MPI_Ssend(&len, 1, MPI_INT, parent, tag_ans_len, MPI_COMM_WORLD);
    MPI_Ssend(poly.data(), len, MPI_INT, parent, tag_ans_arr, MPI_COMM_WORLD);
}

template<>
void send_to_parent<float>(const vector<float> &poly)
{
    int len = (int)poly.size();
    MPI_Ssend(&len, 1, MPI_INT, parent, tag_ans_len, MPI_COMM_WORLD);
    MPI_Ssend(poly.data(), len, MPI_FLOAT, parent, tag_ans_arr, MPI_COMM_WORLD);
}

template<>
void send_to_parent<Int>(const vector<Int> &poly)
{
    int len = (int)poly.size();
    MPI_Ssend(&len, 1, MPI_INT, parent, tag_ans_len_bigint, MPI_COMM_WORLD);
    for (int i = 0; i < len; ++i) {
        string number_string = poly[i].to_string();
        int number_len = (int)number_string.length();
        MPI_Ssend(&number_len, 1, MPI_INT, parent, tag_ans_number_size_bigint, MPI_COMM_WORLD);
        MPI_Ssend(number_string.data(), number_len, MPI_CHAR, parent, tag_ans_number_string_bigint, MPI_COMM_WORLD);
    }
}

#endif //LAB_7_MPI_COMMS_H
