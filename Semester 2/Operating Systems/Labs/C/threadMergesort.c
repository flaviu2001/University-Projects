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
	int *a, st, dr, dep;
};

void* fun(void* arg)
{
	int *a = ((struct thing*)arg)->a;
	int st = ((struct thing*)arg)->st;	
	int dr = ((struct thing*)arg)->dr;
	int dep = ((struct thing*)arg)->dep;
	if (dr-st <= 0)
		return NULL;
	struct thing t1, t2;
	int mid = (st+dr)/2;
	t1.a = a;
	t1.st = st;
	t1.dr = mid;
	t1.dep = dep+1;
	t2.a = a;
	t2.st = mid+1;
	t2.dr = dr;
	t2.dep = dep+1;
	
	if (dep <= -1){
		pthread_t tr1, tr2;
		if (pthread_create(&tr1, NULL, fun, &t1) != 0){
			perror("Bad create fun1");
			exit(1);
		}
		if (pthread_create(&tr2, NULL, fun, &t2) != 0){
			perror("Bad create fun2");
			exit(1);
		}
		if (pthread_join(tr1, NULL) != 0){
			perror("Bad join thread 1");
			exit(1);
		}
		if (pthread_join(tr2, NULL) != 0){
			perror("Bad join thread 2");
			exit(1);
		}
	}else{
		fun(&t1);
		fun(&t2);
	}

	int *b = (int*)malloc((dr-st+1)*sizeof(int));
	int p = 0, p1 = st, p2 = mid+1;
	while (p1 <= mid && p2 <= dr){
		if (a[p1] < a[p2])
			b[p++] = a[p1++];
		else b[p++] = a[p2++];
	}
	while (p1 <= mid)
		b[p++] = a[p1++];
	while (p2 <= dr)
		b[p++] = a[p2++];
	for (int i = st; i <= dr; ++i)
		a[i] = b[i-st];
	free(b);
	return NULL;
}

int main (int argc, char **argv)
{
	int n=100000000;
	// FILE *f = fopen(argv[1], "r");
	// fscanf(f, "%d", &n);
	int *a = (int*)malloc(n*sizeof(int));
	for(int i = 0; i < n; ++i)
		a[i] = i;
		//fscanf(f, "%d", a+i);
	//fclose(f);
	struct thing thing;
	thing.a = a;
	thing.st = 0;
	thing.dr = n-1;
	thing.dep = 0;
	pthread_t t;
	if (pthread_create(&t, NULL, fun, &thing) != 0){
		perror("Bad create");
		exit(1);
	}
	if (pthread_join(t, NULL) != 0){
		perror("Bad join");
		exit(1);
	}
	free(a);
	return 0;
}

