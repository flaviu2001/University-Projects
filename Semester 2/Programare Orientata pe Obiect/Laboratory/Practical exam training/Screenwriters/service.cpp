//
// Created by jack on 6/13/20.
//

#include <fstream>
#include <utility>
#include <algorithm>
#include "service.h"

Service::Service(const std::string& writer_file, const std::string& idea_file) {
    std::ifstream fin (writer_file);
    Screenwriter s;
    while (fin >> s)
        this->writers.push_back(s);
    fin.close();

    std::ifstream fin2(idea_file);
    Idea i;
    while (fin2 >> i)
        this->ideas.push_back(i);
    fin2.close();

    std::sort(this->ideas.begin(), this->ideas.end(), [](Idea &a, Idea &b){
        return a.act < b.act;
    });
}

std::vector<Screenwriter> Service::get_writers() {
    return this->writers;
}

std::vector<Idea>& Service::get_ideas() {
    return this->ideas;
}

void Service::add_idea(const Idea& idea) {
    if (idea.act < 0 || idea.act > 3 || idea.description.empty())
        throw std::runtime_error{"Bad"};
    for (const auto& x : this->ideas)
        if (x.act == idea.act && x.description == idea.description)
            throw std::runtime_error{"Bad2"};
    this->ideas.push_back(idea);
    std::sort(this->ideas.begin(), this->ideas.end(), [](Idea &a, Idea &b){
        return a.act < b.act;
    });
}

void Service::update_idea(Idea& idea, std::string new_status) {
    idea.status = std::move(new_status);
}