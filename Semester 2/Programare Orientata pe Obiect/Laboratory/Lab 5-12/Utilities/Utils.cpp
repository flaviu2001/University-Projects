//
// Created by jack on 4/22/20.
//

#include "Utils.h"
#include "algorithm"

void strip(std::string &string) {
    while (!string.empty() && (string.back() == ' ' || string.back() == '\t' || string.back() == '\n'))
        string.pop_back();
    reverse(string.begin(), string.end());
    while (!string.empty() && (string.back() == ' ' || string.back() == '\t' || string.back() == '\n'))
        string.pop_back();
    reverse(string.begin(), string.end());
}

int string_to_int(const std::string& string) {
    int number = 0;
    for (auto x : string)
        number = number*10 + x-'0';
    return number;
}

void html_strip(std::string &string) {
    strip(string);
    string = string.substr(0, string.size()-5);
    reverse(string.begin(), string.end());
    string = string.substr(0, string.size()-4);
    reverse(string.begin(), string.end());
    strip(string);
}

std::string termination(const std::string &string)
{
    std::string result;
    unsigned position = string.size();
    for (unsigned i = 0; i < string.size(); ++i)
        if (string[i] == '.')
            position = i;
    for (unsigned i = position+1; i < string.size(); ++i)
        result.push_back(string[i]);
    return result;
}
