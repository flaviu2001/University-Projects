#include <stdio.h>
#include <string.h>

int main() {
    char input[100];
    while (scanf("%s", input)){
        if (strcmp(input, "exit") == 0)
            break;
        printf("%s\n", input);
    }
    return 0;
}
