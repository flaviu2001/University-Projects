/*
Using PIPE channels create and implement the following scenario: 
Process A reads N integer numbers from the keyboard and sends them another process named B. 
Process B will add a random number, between 2 and 5, to each received number from 
process A and will send them to another process named C. 
The process C will add all the received numbers and will send the result back to process A. 
All processes will print a debug message before sending and after receiving a number.
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
    int a2b[2], b2c[2], c2a[2];
    if (pipe(a2b) == -1){
	perror("Error creating pipe.\n");
	exit(1);
    }
    if (pipe(b2c) == -1){
	perror("Error creating pipe.\n");
	close(a2b[0]);
	close(a2b[1]);
	exit(1);
    }
    if (pipe(c2a) == -1){
	perror("Error creating pipe.\n");
	close(a2b[0]);
	close(a2b[1]);
	close(b2c[0]);
	close(b2c[1]);
	exit(1);
    }
    if (fork() == 0){
	//B
	close(a2b[1]);
	close(b2c[0]);
	close(c2a[0]);
	close(c2a[1]);
	srand(time(NULL));
	int n;
	if (read(a2b[0], &n, sizeof(int)) <= 0){
	    close(a2b[0]);
	    close(b2c[1]);
	    exit(1);
	}
	printf("Read from A to B the length of the array %d.\n", n);
	int *v = (int*)malloc(n*sizeof(int));
	for (int i = 0; i < n; ++i){
	    if (read(a2b[0], v+i, sizeof(int)) <= 0){
		close(a2b[0]);
		close(b2c[1]);
		exit(1);
	    }
	    printf("Read from A to B the %dth element: %d.\n", i, v[i]);
	}
	close(a2b[0]);
	printf("Writing from B to C the length of the array %d.\n", n);
	if (write(b2c[1], &n, sizeof(int)) <= 0){
	    close(b2c[1]);
	    exit(1);
	}
	for (int i = 0; i < n; ++i){
	    v[i] += rand()%4+2;
	    printf("Writing from B to C the new %dth element: %d.\n", i, v[i]);
	    if (write(b2c[1], v+i, sizeof(int)) <= 0){
		close(b2c[1]);
		exit(1);
	    }
	}
	close(b2c[1]);
	free(v);
	exit(0);
    }
    if (fork() == 0){
	//C
	close(a2b[0]);
	close(a2b[0]);
	close(b2c[1]);
	close(c2a[0]);
	int n;
	if (read(b2c[0], &n, sizeof(int)) <= 0){
	    close(b2c[0]);
	    close(c2a[1]);
	    exit(1);
	}
	printf("Read from B to C the length of the array %d.\n", n);
	int *v = (int*)malloc(n*sizeof(int));
	int sum = 0;
	for (int i = 0; i < n; ++i){
	    if (read(b2c[0], v+i, sizeof(int)) <= 0){
		close(b2c[0]);
		close(c2a[1]);
		exit(1);
	    }
	    sum += v[i];
	    printf("Read from B to C the %dth element: %d.\n", i, v[i]);
	}
	close(b2c[0]);
	printf("Writing from C to A the sum: %d.\n", sum);
	if (write(c2a[1], &sum, sizeof(int)) <= 0){
	    close(c2a[1]);
	    exit(1);
	}
	close(c2a[1]);
	free(v);
	exit(0);
    }
    //A
    close(a2b[0]);
    close(b2c[0]);
    close(b2c[1]);
    close(c2a[1]);
    int n;
    scanf("%d", &n);
    int *v = (int*)malloc(n*sizeof(int));
    for (int i = 0; i < n; ++i)
	scanf("%d", &v[i]);
    printf("Writing from A to B the length of the array %d.\n", n);
    if (write(a2b[1], &n, sizeof(int)) <= 0){
	close(a2b[1]);
	close(c2a[0]);
	exit(1);
    }
    for (int i = 0; i < n; ++i){
	printf("Writing from A to B the %dth number: %d.\n", i, v[i]);
	if (write(a2b[1], v+i, sizeof(int)) <= 0){
	    close(a2b[1]);
	    close(c2a[0]);
	    exit(1);
	}
    }
    free(v);
    close(a2b[1]);
    int sum;
    if (read(c2a[0], &sum, sizeof(int)) <= 0){
        close(c2a[0]);
	exit(1);
    }
    printf("Read from C to A the sum: %d.\n", sum);
    close(c2a[0]);
    wait(0);
    wait(0);
    return 0;
}

