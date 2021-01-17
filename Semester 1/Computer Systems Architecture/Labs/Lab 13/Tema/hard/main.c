#include <stdio.h>

void citireSirC(char sir[])
{
    gets(sir);
}

void reverse(char sir[]);

int main()
{
    char proposition[101];
    citireSirC(proposition);
    reverse(proposition);
    puts(proposition);
    return 0;
}
