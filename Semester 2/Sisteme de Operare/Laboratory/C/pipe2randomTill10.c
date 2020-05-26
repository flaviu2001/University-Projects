//  3. Write a C program that creates two child processes. The two child processes will alternate sending random integers between 1 and 10(inclusively) to one another until one of them sends the number 10. Print messages as the numbers are sent.

#include <stdio.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <string.h>

int main()
{
    int f1_to_f2[2], f2_to_f1[2], f1, f2;
    pipe(f1_to_f2);
    pipe(f2_to_f1);
    f1 = fork();
    if (f1 < 0){
	printf("Fork went bad.\n");
	exit(1);
    }
    if (f1 == 0){
	// Inside child 1
	srand(clock()^time(NULL)^getpid());
	close(f1_to_f2[0]);
	close(f2_to_f1[1]);
	int x;
	while(1){
	    x = rand()%10+1;
	    write(f1_to_f2[1], &x, sizeof(int));
	    int rr = read(f2_to_f1[0], &x, sizeof(int));
	    if (rr < 0){
		close(f1_to_f2[1]);
		close(f2_to_f1[0]);
		exit(0);
	    }
	    if (x == 10){
	       printf("F1 has received a 10, ending...\n");
	       close(f1_to_f2[1]);
	       close(f2_to_f1[0]);
	       exit(0);
	    }
	    printf("F1 has received a %d.\n", x);
	}
	exit(0);
    }
    f2 = fork();
    if (f2 < 0){
	printf("Fork went bad.\n");
	exit(1);
    }
    if (f2 == 0){
	// Inside child 2
	srand(clock()^time(NULL)^getpid());
	close(f2_to_f1[0]);
	close(f1_to_f2[1]);
	int x;	
	while(1){
	    int rr = read(f1_to_f2[0], &x, sizeof(int));
	    if (rr < 0){
		close(f1_to_f2[0]);
		close(f2_to_f1[1]);
		exit(0); 
	    }
	    if (x == 10){
		printf("F2 has received a 10, ending...\n");
		close(f1_to_f2[0]);
		close(f2_to_f1[1]);
		exit(0);
	    }
	    printf("F2 has received a %d\n", x);
	    x = rand()%10+1;
	    write(f2_to_f1[1], &x, sizeof(int));
	}
	exit(0);
    }
    close(f1_to_f2[0]);
    close(f1_to_f2[1]);
    close(f2_to_f1[0]);
    close(f2_to_f1[1]);
    wait(0);
    wait(0);
    return 0;
}
