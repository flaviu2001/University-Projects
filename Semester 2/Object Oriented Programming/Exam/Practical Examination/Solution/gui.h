#pragma once

#include <QtWidgets/QWidget>
#include "Service.h"
#include "ModelWrapper.h"

class GUI : public QWidget
{
Q_OBJECT
private:
    std::string name;
    Service& serv;
    ModelWrapper* m;

public:
    GUI(Service &_serv, std::string _name, ModelWrapper *_m, QWidget *parent = Q_NULLPTR);
};
