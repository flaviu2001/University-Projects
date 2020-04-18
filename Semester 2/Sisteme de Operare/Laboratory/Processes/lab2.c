// 2. Create a C program that generates N random integers (N given at the command line). It then creates a child, sends the numbers via pipe. The child calculates the average and sends the result back.

#include <stdio.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <string.h>

int main(int argc, char **argv)
{
    srand(time(NULL)^clock()^getpid());
    if (argc != 2){
	printf("Please insert only one number.\n");
	exit(1);
    }
    int n = 0, l1 = strlen(argv[1]);
    for (int i = 0; i < l1; ++i){
	if (argv[1][i] > '9' && argv[1][i] < '0'){
	    perror("Insert a valid number.\n");
	    exit(1);
	}
	n = n*10+argv[1][i]-'0';
    }
    int p2c[2], c2p[2];
    pipe(p2c); 
    pipe(c2p);
    int f = fork();
    if (f == -1){
	perror("Could not fork\n");
	exit(1);
    }
    if (f == 0){
	//inside child 1
	close(p2c[1]);
	close(c2p[0]);
	long long sum = 0;
	int cnt = 0;
	double ans = 0;
	for (int i = 0; i < n; ++i){
	    int x;
	    read(p2c[0], &x, sizeof(int));
	    sum += x;
	    ++cnt; 
	}
	close(p2c[0]);
	ans = ((double)sum)/cnt;
	write(c2p[1], &ans, sizeof(double));
	close(c2p[1]);
	exit(0);
    }
    close(p2c[0]);
    close(c2p[1]);
    int *v = (int*)malloc(n*sizeof(int));
    for (int i = 0; i < n; ++i){
	v[i] = rand();
	printf("%d\n", v[i]);
    }
    for (int i = 0; i < n; ++i)
	write(p2c[1], &v[i], sizeof(int));
    free(v);
    double ans;
    read(c2p[0], &ans, sizeof(ans));
    printf("%lf the average of %d numbers.\n", ans, n);
    return 0;
}
