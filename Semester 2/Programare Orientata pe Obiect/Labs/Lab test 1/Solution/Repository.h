//
// Created by jack on 5/4/20.
//

#ifndef TESTOOP_REPOSITORY_H
#define TESTOOP_REPOSITORY_H

#include <vector>
#include "Pavilion.h"

class Repository {
private:
    std::vector<Pavilion*> list;
public:
    ~Repository();
    void add(Pavilion* pavilion);
    std::vector<Pavilion*> get_pavilions();
};


#endif //TESTOOP_REPOSITORY_H
