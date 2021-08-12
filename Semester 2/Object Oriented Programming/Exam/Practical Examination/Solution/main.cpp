#include "gui.h"
#include <QtWidgets/QApplication>
#include "Service.h"
#include "repository.h"
#include "qdebug.h"
#include "TableModel.h"
#include "ModelWrapper.h"
#include "tests.h"

int main(int argc, char *argv[])
{
    test_all();
    QApplication a(argc, argv);
    Repository<Star> stars{ "stars.txt" };
    Repository<Astronomer> astronomers{ "astronomers.txt" };
    Service s(astronomers, stars);
    ModelWrapper* m = new ModelWrapper;
    for (auto guy : s.get_astronomers()) {
        auto* window = new GUI{ s, guy.name, m };
        window->show();
    }
    return a.exec();
}
