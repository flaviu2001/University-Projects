#ifndef IMRE_3_UI_H
#define IMRE_3_UI_H

#include "Service.h"

typedef struct{
    Service *service;
} UI;

UI* create_ui();

void start(UI *ui);

void destroy_ui(UI **ui);

#endif //IMRE_3_UI_H
