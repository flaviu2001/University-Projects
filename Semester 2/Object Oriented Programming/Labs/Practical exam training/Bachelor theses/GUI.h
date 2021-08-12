#pragma once

#include <QtWidgets/QWidget>
#include "Service.h"
#include "Domain.h"
#include "ModelWrapper.h"

class GUI : public QWidget
{
    Q_OBJECT
private:
    Service& service;
    Teacher teacher;
    ModelWrapper& m;
public:
    GUI(Service& _serv, Teacher _teacher, ModelWrapper& _m, QWidget* parent = Q_NULLPTR);
};
