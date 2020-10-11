//
// Created by jack on 6/13/20.
//

#include "domain.h"
#include <sstream>

std::istream &operator>>(std::istream &reader, Screenwriter &screenwriter) {
    std::string line;
    std::getline(reader, line);
    if (line.empty())
        return reader;
    std::stringstream stream(line);
    std::string name;
    std::string expertise;
    std::getline(stream, name, ';');
    std::getline(stream, expertise, ';');
    screenwriter.name = name;
    screenwriter.expertise = expertise;
    return reader;
}

std::ostream &operator<<(std::ostream &writer, const Screenwriter &screenwriter) {
    writer << screenwriter.name << ";" << screenwriter.expertise << "\n";
    return writer;
}

std::istream &operator>>(std::istream &reader, Idea &idea) {
    std::string line;
    std::getline(reader, line);
    if (line.empty())
        return reader;
    std::stringstream stream(line);
    std::string description;
    std::string status;
    std::string creator;
    std::string act;
    std::getline(stream, description, ';');
    std::getline(stream, status, ';');
    std::getline(stream, creator, ';');
    std::getline(stream, act, ';');
    idea.description = description;
    idea.status = status;
    idea.creator = creator;
    idea.act = atoi(act.c_str()); // NOLINT(cert-err34-c)
    return reader;
}

std::ostream &operator<<(std::ostream &writer, const Idea &idea) {
    writer << idea.description << ";" << idea.status << ";" << idea.creator << ";" << idea.act << "\n";
    return writer;
}
