#include <QtWidgets/QApplication>
#include <UI/GUI.h>
#include "Tests/tests.h"
#include <QDialog>
#include <QtWidgets>
#include <Utilities/SettingsParser.h>

int main(int argc, char **argv) {
    test_all();
    QApplication a(argc, argv);
    Service service = SettingsParser::get_service();
    auto *gui = new GUI(service);
    gui->show();
    return QApplication::exec();
//    UI ui(service);
//    ui.start();
//    return 0;
}
