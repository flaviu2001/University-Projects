#include <QtWidgets/QApplication>
#include <UI/GUI.h>
#include "UI/UI.h"
#include "Tests/tests.h"
#include <QDialog>
#include <QtWidgets>

int main(int argc, char **argv) {
    test_all();
    QApplication a(argc, argv);
    auto *gui = new GUI;
    gui->show();
    return QApplication::exec();
    UI ui;
    ui.start();
    return 0;
}
