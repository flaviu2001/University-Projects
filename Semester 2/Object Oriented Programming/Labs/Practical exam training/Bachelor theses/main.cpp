#include "GUI.h"
#include <QtWidgets/QApplication>
#include <qdebug.h>
#include "Service.h"
#include <iostream>
#include "ModelWrapper.h"
#include "ChartGUI.h"
#include "tests.h"

int main(int argc, char* argv[])
{
    test__service_add_coordinator();
    test__service_service_get_by_name_id();
    QApplication a(argc, argv);
    Service s("teachers.txt", "students.txt");
    ModelWrapper m;
    auto w2 = new ChartGUI(s);
    w2->show();
    m.cg = w2;
    for (auto x : s.get_teachers()) {
        auto window = new GUI(s, x, m);
        window->show();
    }
    return a.exec();
}
