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

int f[10];
pthread_mutex_t m[10];

void* fun(void* thing)
{
    char *c = (char*)thing;
    int len = strlen(c);
    for (int i = 0; i < len; ++i)
	if (c[i] >= '0' && c[i] <= '9'){
	    pthread_mutex_lock(&m[c[i]-'0']);
	    ++f[c[i]-'0'];
	    pthread_mutex_unlock(&m[c[i]-'0']);
	}
    return NULL;
}

int main (int argc, char **argv)
{
    for (int i = 0; i < 10; ++i)
	if (pthread_mutex_init(&m[i], NULL) != 0){
	    perror("Error on creating mutex");
	    exit(1);
	}
    pthread_t thread[argc-1];
    for (int i = 1; i < argc; ++i){
	if (pthread_create(&thread[i-1], NULL, fun, argv[i]) != 0){
	    perror("Error on thread creation");
	}
    }
    for (int i = 0; i < argc-1; ++i)
	if (pthread_join(thread[i], NULL) != 0){
	    perror("Error waiting on thread");
	}
    for (int i = 0; i < 10; ++i)
	pthread_mutex_destroy(&m[i]);
    for (int i = 0; i < 10; ++i)
	printf("%d ", f[i]);
    printf("\n");
    return 0;
}

