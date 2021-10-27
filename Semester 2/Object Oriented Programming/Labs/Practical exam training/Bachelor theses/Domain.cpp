#include "Domain.h"
#include <sstream>

using namespace std;

std::istream& operator>>(std::istream& reader, Teacher& teacher)
{
    reader >> teacher.name;
    return reader;
}

std::istream& operator>>(std::istream& reader, Student& student)
{
    string line;
    getline(reader, line);
    if (line.empty())
        return reader;
    stringstream ss(line);
    std::string year;
    getline(ss, student.id, ';');
    getline(ss, student.name, ';');
    getline(ss, student.email, ';');
    getline(ss, year, ';');
    student.year = atoi(year.c_str());
    getline(ss, student.title, ';');
    getline(ss, student.coordinator, ';');
    return reader;
}

std::string Student::to_string()
{
    return id + ";" + name + ";" + email + ";" + std::to_string(year) + ";" + title + ";" + coordinator;
}
