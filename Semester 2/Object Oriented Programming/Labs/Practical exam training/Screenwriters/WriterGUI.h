//
// Created by jack on 6/13/20.
//

#ifndef SCREENWRITERS_WRITERGUI_H
#define SCREENWRITERS_WRITERGUI_H


#include <QtWidgets/QWidget>
#include <QtWidgets/QVBoxLayout>
#include "service.h"
#include "WriterModel.h"

class WriterGUI : public QWidget {
private:
    Service &service;
    Screenwriter writer;
    WriterModel* model;
    QVBoxLayout* layout;
public:
    WriterGUI(Service &_service, Screenwriter &_writer, WriterModel* _model);
};


#endif //SCREENWRITERS_WRITERGUI_H
