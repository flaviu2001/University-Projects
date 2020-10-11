#include <stdio.h>
#include <signal.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <string.h>
#include <pthread.h>
#include <semaphore.h>

struct thing{
    char *file;
    int n;
    char q;
};

void* f(void *arg)
{
    int *good = (int*)malloc(sizeof(int));
    struct thing *var = arg;
    FILE *file = fopen(var->file, "r");
    char now;
    for (int i = 0; i <= var->n; ++i){
	if (fscanf(file, "%c", &now) != 1){
	    *good = -1;
	    fclose(file);
	    return good;
	}
    }
    if (now == var->q)
	*good = 1;
    else *good = 2;
    fclose(file);
    return good;
}

int main (int argc, char **argv)
{
    if ((argc-1)%3 != 0){
	perror("Bad args");
	exit(1);
    }
    pthread_t thread[argc/3];
    struct thing guy[argc/3];
    for (int i = 1; i < argc; i += 3){
	guy[i/3].file = argv[i];
	guy[i/3].n = 0;
	for (int j = 0; j < strlen(argv[i+1]); ++j)
	    guy[i/3].n = guy[i/3].n*10 + argv[i+1][j]-'0';
	guy[i/3].q = argv[i+2][0];
	if (pthread_create(&thread[i/3], NULL, f, &guy[i/3]) != 0){
	    perror("Bad thread");
	    exit(1);
	}
    }
    for (int i = 1; i < argc; i += 3){
	void *arg = NULL;
	if (pthread_join(thread[i/3], &arg) != 0){
	    perror("Bad join");
	    exit(1);
	}
	int now = *((int*)arg);
	if (now == -1)
	    printf("%d: %s does not have %d characters!\n", i/3, guy[i/3].file, guy[i/3].n);
	else printf("%d: %s %s\n", i/3, guy[i/3].file, (now == 1 ? "is good" : "is not good"));
    }
    return 0;
}