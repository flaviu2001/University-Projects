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

int s[3];

void* f(void* arg)
{
    char *c = arg;
    int len = strlen(c);
    for (int i = 0; i < len; ++i){
	if (c[i] >= '0' && c[i] <= '9'){
	    ++s[0];
	}else if (strchr("aeiouAEIOU", c[i])){
	    ++s[1];
	}else{
	    ++s[2];
	}
    }
    return NULL;
}

int main (int argc, char **argv)
{
    if (argc < 2){
	perror("Bad args!");
	exit(1);
    }
    pthread_t p[argc-1];
    for (int i = 1; i < argc; ++i)
	if (pthread_create(&p[i-1], NULL, f, argv[i]) != 0){
	    perror("Bad thread!");
	    exit(1);
	}
    for (int i = 1; i < argc; ++i)
	if (pthread_join(p[i-1], NULL) != 0){
	    perror("Bad join!");
	    exit(1);
	}
    printf("Digits: %d\nVowels: %d\nConsonants: %d\n", s[0], s[1], s[2]);
    return 0;
}