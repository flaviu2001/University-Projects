//
// Created by jack on 5/4/20.
//

#include "Controller.h"
#include <fstream>

void Controller::addPavilion(Pavilion *pavilion) {
    this->repository.add(pavilion);
}

std::vector<Pavilion *> Controller::get_pavilions() {
    return this->repository.get_pavilions();
}

std::vector<Pavilion *> Controller::get_efficient() {
    auto list = this->repository.get_pavilions();
    std::vector<Pavilion*> to_return;
    for (auto p : list)
        if (p->is_efficient())
            to_return.push_back(p);
    return to_return;
}

void Controller::write_to_file(const std::string &name) {
    std::ofstream fout (name);
    for (auto x : this->repository.get_pavilions()){
        fout << x->to_string() << " ";
        // So to get 10 I had to sort this repository before printing by name or something
        // Literally a sort() with a lambda function for comparator
        // I would have done it in 3 minutes (actually did after test) but the professor didn't accept me submitting after 9:00
        // I asked if I could implement it quickly and he said I can't. Others did and they were graded accordingly
        // I hope this professor feels bad about themselves and gets flak from students every single time (if he deserves it, he usually does)
        // So yea i got a 9 on a shitty horribly cramped test with the only excuse being that we are allowed to use external sources.
        if (x->is_efficient())
            fout << "efficient\n";
        else fout << "not efficient\n";
    }
    fout.close();
}


