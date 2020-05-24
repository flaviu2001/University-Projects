/*
Implement a program that continuously reads two integers from the console, 
sends them to another process, gets back from that process their sum and product, 
and display them to the console. The program stops when the sum and the product are equal.
Done with fifos.
*/

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

int main (int argc, char **argv)
{
    if (mkfifo("a2b", 0600) == -1){
	perror("Error on creating fifo");
	exit(1);
    }
    if (mkfifo("b2a", 0600) == -1){
	perror("Error on creating second fifo");
	exit(1);
    }
    if (fork() == 0){
	int a2b = open("a2b", O_RDONLY);
	int b2a = open("b2a", O_WRONLY);
	while(1){
	    int a, b;
	    if (read(a2b, &a, sizeof(int)) <= 0)
		break;
	    if (read(a2b, &b, sizeof(int)) <= 0)
		break;
	    int sum = a+b;
	    int prod = a*b;
	    if (write(b2a, &sum, sizeof(int)) <= 0)
		break;
	    if (write(b2a, &prod, sizeof(int)) <= 0)
		break;
	}
	close(a2b);
	close(b2a);
	exit(0);
    }
    int a2b = open("a2b", O_WRONLY);
    int b2a = open("b2a", O_RDONLY);
    while(1){
	int a, b;
	scanf("%d%d", &a, &b);
	if (write(a2b, &a, sizeof(int)) <= 0)
	    break;
	if (write(a2b, &b, sizeof(int)) <= 0)
	    break;
	int sum, prod;
	if (read(b2a, &sum, sizeof(int)) <= 0)
	    break;
	if (read(b2a, &prod, sizeof(int)) <= 0)
	    break;
	printf("%d %d\n", sum, prod);
	if (sum == prod)
	    break;
    }
    close(a2b);
    close(b2a);
    if (unlink("a2b") == -1){
	perror("Couldn't unlink");
	exit(1);
    }
    if (unlink("b2a") == -1){
	perror("Couldn't unlink second fifo");
	exit(1);
    }
    return 0;
}

