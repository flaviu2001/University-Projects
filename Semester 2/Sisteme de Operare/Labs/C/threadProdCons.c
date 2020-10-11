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

int g;
pthread_rwlock_t rwl;

void* prod(void *arg)
{
	int i = *(int*)arg;
	while(1){
		int r = rand()%11+10;
		pthread_rwlock_wrlock(&rwl);
		g += r;
		printf("%d added %d and became %d\n", i, r, g);
		pthread_rwlock_unlock(&rwl);
		sleep(2);
	}
}

void* cons(void *arg)
{
	int i = *(int*)arg;
	while(1){
		pthread_rwlock_rdlock(&rwl);
		printf("%d -- %d\n", i, g);
		pthread_rwlock_unlock(&rwl);
		sleep(1);
	}
}

int main (int argc, char **argv)
{
	srand(time(NULL));
	int n;
	scanf("%d", &n);
	if (pthread_rwlock_init(&rwl, NULL) != 0){
		perror("Bad RW");
		exit(1);
	}
	pthread_t t[n];
	int I[n];
	for (int i = 0; i < n/2; ++i){
		I[i] = i;
		if (pthread_create(&t[i], NULL, prod, &I[i]) != 0){
			perror("Error on creating thread");
			exit(1);
		}
	}
	for (int i = n/2; i < n; ++i){
		I[i] = i;
		if (pthread_create(&t[i], NULL, cons, &I[i]) != 0){
			perror("Error on making thread");
			exit(1);
		}
	}
	while(1){
		sleep(10);
		pthread_rwlock_wrlock(&rwl);
		g = 0;
		printf("Main reseted\n");
		pthread_rwlock_unlock(&rwl);
	}
	for (int i = 0; i < n; ++i)
		if (pthread_join(t[i], NULL) != 0){
			perror("Error on join");
			exit(1);
		}
	if (pthread_rwlock_destroy(&rwl) != 0){
		perror("Error on destroy");
		exit(1);
	}
	return 0;
}

