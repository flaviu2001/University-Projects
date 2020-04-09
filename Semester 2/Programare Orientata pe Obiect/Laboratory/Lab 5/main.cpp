#include "UI.h"
#include "tests.h"

int main() {
    test_all();
    Repository repository;
    Service service(repository);
    UI ui(service);
    ui.start();
    return 0;
}
