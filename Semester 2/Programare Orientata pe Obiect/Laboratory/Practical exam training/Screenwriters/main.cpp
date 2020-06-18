#include <iostream>
#include <QtWidgets/QApplication>
#include <qdebug.h>
#include "service.h"
#include "WriterGUI.h"
#include "WriterModel.h"
#include "tests.h"

using namespace std;

int main(int argc, char **argv) {
    test__service_add_idea();
    test__service_update_idea();
    QApplication a(argc, argv);
    Service s("screenwriters.txt", "ideas.txt");
    auto model = new WriterModel(s);
    for (auto x : s.get_writers()){
        auto window = new WriterGUI(s, x, model);
        window->show();
    }
    return QApplication::exec();
}
