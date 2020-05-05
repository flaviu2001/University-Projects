#include "UI/UI.h"
#include "Tests/tests.h"
#include "Repositories/RepositoryHTML.h"

int main() {
    test_all();
    Repository* repository = new RepositoryCSV;
    Service service(repository);
    UI ui(service);
    ui.start();
    delete repository;
    return 0;
}
