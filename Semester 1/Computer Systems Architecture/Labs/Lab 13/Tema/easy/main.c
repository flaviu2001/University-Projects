#include <stdio.h>

int maxy(int nr, int v[]);

int main()
{
	int nr = 0, v[101];
	scanf("%d", &nr);
	for (int i = 0; i < nr; ++i)
		scanf("%d", &v[i]);
	FILE *f = fopen("max.txt", "w");
	fprintf(f, "The maximum number of the array of integers you have chosen to read is %x.", maxy(nr, v));
	return 0;
}