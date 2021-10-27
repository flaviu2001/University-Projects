//
// Created by jack on 3/21/20.
//

#ifndef IMRE5_UI_H
#define IMRE5_UI_H

#include "../Service/Service.h"

class UI {
private:
    Service &service;
public:
    UI(Service &_service);
    void start();
};


#endif //IMRE5_UI_H
