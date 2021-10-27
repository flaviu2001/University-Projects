/*
Implement a program that continuously reads two integers from the console, 
sends them to another process, gets back from that process their sum and product, 
and display them to the console. The program stops when the sum and the product are equal.
Done with pipes.
*/

#include <stdio.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <string.h>
#include <pthread.h>
#include <semaphore.h>

int main (int argc, char **argv)
{
    int p2c[2], c2p[2];
    pipe(p2c);
    pipe(c2p);
    if (fork() == 0){
	close(p2c[1]);
	close(c2p[0]);	
	while(1){
	    int a, b;
	    if (read(p2c[0], &a, sizeof(int)) <= 0)
		break;
	    if (read(p2c[0], &b, sizeof(int)) <= 0)
		break;
	    int sum = a+b;
	    int prod = a*b;
	    write(c2p[1], &sum, sizeof(int));
	    write(c2p[1], &prod, sizeof(int));
	}
	close(p2c[0]);
	close(c2p[1]);
	exit(0);
    }
    close(p2c[0]);
    close(c2p[1]);
    int a, b;
    while(1){
        scanf("%d%d", &a, &b);
	write(p2c[1], &a, sizeof(int));
	write(p2c[1], &b, sizeof(int));
	int sum, prod;
	if (read(c2p[0], &sum, sizeof(int)) <= 0)
	    break;
	if (read(c2p[0], &prod, sizeof(int)) <= 0)
	    break;
	printf("%d %d\n", sum, prod);
	if (sum == prod)
	    break;
    }
    close(p2c[1]);
    close(c2p[1]);
    wait(0);
    wait(0);
    return 0;
}

