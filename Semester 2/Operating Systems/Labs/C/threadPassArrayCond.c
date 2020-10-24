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

struct thing
{
	int *a;
	int *len;
	int n;
	int i;
};

pthread_mutex_t m[2], m2;
pthread_cond_t c[2];

void* f(void* arg)
{
	int *a = ((struct thing*)arg)->a;
	int *len = ((struct thing*)arg)->len;
	int n = ((struct thing*)arg)->n;
	int i = ((struct thing*)arg)->i;
	pthread_mutex_lock(&m2);
	pthread_mutex_lock(&m[1-i]);
	pthread_cond_signal(&c[1-i]);
	pthread_mutex_unlock(&m[1-i]);	
	while(1){
		pthread_mutex_lock(&m[i]);
		pthread_mutex_unlock(&m2);
		pthread_cond_wait(&c[i], &m[i]);
		pthread_mutex_unlock(&m[i]);
		if (*len >= n){	
			pthread_mutex_lock(&m[1-i]);
			pthread_cond_signal(&c[1-i]);
			pthread_mutex_unlock(&m[1-i]);
			break;
		}
		int r = rand()%1000;
		if (i==0)
			r *= 2;
		else r = r*2+1;
		a[(*len)++] = r;
		pthread_mutex_lock(&m2);
		pthread_mutex_lock(&m[1-i]);
		pthread_cond_signal(&c[1-i]);
		pthread_mutex_unlock(&m[1-i]);
	}
	return NULL;
}

int main (int argc, char **argv)
{
	srand(time(NULL));
	int n;
	scanf("%d", &n);
	int *a = (int*)malloc(n*sizeof(int));
	int len = 0;
	pthread_t t[2];
	struct thing thing[2];
	for (int i = 0; i < 2; ++i){
		if (pthread_cond_init(&c[i], NULL) != 0){
			perror("Bad cond");
			exit(1);
		}
		if (pthread_mutex_init(&m[i], NULL) != 0){
			perror("Bad mutex");
			exit(1);
		}
	}
	if (pthread_mutex_init(&m2, NULL) != 0){
		perror("Bad mutex");
		exit(1);
	}
	for (int i = 0; i < 2; ++i){
		thing[i].a = a;
		thing[i].len = &len;
		thing[i].n = n;
		thing[i].i = i;
		if (pthread_create(&t[i], NULL, f, &thing[i]) != 0){
			perror("Bad create");
			exit(1);
		}
	}
	for (int i = 0; i < 2; ++i)
		if (pthread_join(t[i], NULL) != 0){
			perror("Bad join");
			exit(1);
		}
	for (int i = 0; i < 2; ++i){
		if (pthread_mutex_destroy(&m[i]) != 0){
			perror("Bad mutex destroy");
			exit(1);
		}
		if (pthread_cond_destroy(&c[i]) != 0){
			perror("Bad cond destroy");
			exit(1);
		}
	}
	for (int i = 0; i < n; ++i)
		printf("%d ", a[i]);
	printf("\n");
	free(a);
	return 0;
}

