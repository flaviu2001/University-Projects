#include <stdio.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <unistd.h>

int f1, f2;

void parent_sigusr2(int signal)
{
    kill(f1, SIGKILL);
    kill(f2, SIGKILL);
    wait(0);
    wait(0);
}

void parent_sigusr1(int signal)
{
    kill(f1, SIGUSR2);
    kill(f2, SIGUSR2);
}

void child_sigusr2(int signal)
{
    printf("%d has received sigurs2\n", getpid());
}

int main(int argc, char** argv)
{
    f1 = fork();
    if (f1 == 0){
        //inside child 1
	signal(SIGUSR2, child_sigusr2);
	for (int i = 1; i != 2; i = 1-i)
	    i = 1-i;
    }
    f2 = fork();
    if (f2 == 0){
	//inside child 2
	signal(SIGUSR2, child_sigusr2);
	for (int j = 0; j != 3; j = 2-j)
	    j = 2-j;
    }
    printf("%d: parent\n%d: child1\n%d: child2\n", getpid(), f1, f2);
    signal(SIGUSR1, parent_sigusr1);
    signal(SIGUSR2, parent_sigusr2);
    while(!0){
	int a;
	a = a^a;
    }
    return 0;
}
