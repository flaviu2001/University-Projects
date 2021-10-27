//
// Created by jack on 5/4/20.
//

#ifndef TESTOOP_CONTROLLER_H
#define TESTOOP_CONTROLLER_H

#include "Repository.h"

class Controller {
private:
    Repository repository;
public:
    std::string file_name;
    bool set = false;
    void addPavilion(Pavilion *pavilion);
    std::vector<Pavilion*> get_pavilions();
    std::vector<Pavilion*> get_efficient();
    void write_to_file(const std::string &name);
};


#endif //TESTOOP_CONTROLLER_H
