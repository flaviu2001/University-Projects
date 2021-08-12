#include "domain.h"
#include <sstream>
#include <iostream>
using namespace std;

std::istream& operator>>(std::istream& is, Astronomer& s)
{
    string line;
    getline(is, line);
    if (line.empty())
        return is;
    stringstream ss(line);
    getline(ss, s.name, ';');
    getline(ss, s.constellation, ';');
    return is;
}

std::istream& operator>>(std::istream& is, Star& s)
{
    string line;
    getline(is, line);
    if (line.empty())
        return is;
    stringstream ss(line);
    getline(ss, s.name, ';');
    getline(ss, s.constellation, ';');
    string number;
    getline(ss, number, ';');
    s.ra = atoi(number.c_str());
    getline(ss, number, ';');
    s.dec = atoi(number.c_str());
    getline(ss, number, ';');
    s.diameter = atoi(number.c_str());
    return is;
}

std::string Astronomer::to_string()
{
    return string{name + ";" + constellation};
}

std::string Star::to_string()
{
    return std::string{ name + ";" + constellation + ";" + std::to_string(ra) + ";" + std::to_string(dec) + ";" + std::to_string(diameter) };
}
