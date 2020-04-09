//
// Created by jack on 3/21/20.
//

#ifndef IMRE5_UI_H
#define IMRE5_UI_H

#include "Service.h"

class UI {
    private:
        Service service;
    public:
        explicit UI(Service &the_service);
        void start();
};


#endif //IMRE5_UI_H
