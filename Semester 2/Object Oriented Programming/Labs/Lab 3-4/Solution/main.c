#include "UI.h"
#include "Tests.h"

int main() {
    test_all();
    UI *ui = create_ui();
    start(ui);
    destroy_ui(&ui);
    return 0;
}
