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

int even, odd;
pthread_mutex_t m;

void* f(void* arg)
{
	char *c = (char*)arg;
	FILE *f = fopen(c, "r");
	int x;
	int parity[2] = {0};
	while(fscanf(f, "%d", &x) && !feof(f))
		++parity[x&1];
	pthread_mutex_lock(&m);
	even += parity[0];
	odd += parity[1];
	pthread_mutex_unlock(&m);
	fclose(f);
	return NULL;
}

int main (int argc, char **argv)
{
	if (argc < 2){
		printf("Bad args");
		exit(1);
	}
	if (pthread_mutex_init(&m, NULL) != 0){
		perror("Bad mutex");
		exit(1);
	}
	pthread_t *th = (pthread_t*)malloc((argc-1)*sizeof(pthread_t));
	for (int i = 1; i < argc; ++i)
		if (pthread_create(&th[i-1], NULL, f, argv[i]) != 0){
			perror("Bad create");
			exit(1);
		}
	for (int i = 1; i < argc; ++i)
		if (pthread_join(th[i-1], NULL) != 0){
			perror("Bad join");
			exit(1);
		}
	if (pthread_mutex_destroy(&m) != 0){
		perror("Bad destroy");
		exit(1);
	}
	free(th);
	printf("Even: %d\nOdd: %d\n", even, odd);
	return 0;
}

