#include <stdio.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <string.h>

int isboltz(int x)
{
    if (x%7 == 0)
	return 1;
    while(x != 0){
	if (x%10 == 7)
	    return 1;
	x /= 10;
    }
    return 0;
}

int main(int argc, char** argv)
{
    if (argc != 2){
	printf("Insert exactly one number.\n");
	exit(1);
    }
    int n = 0, len1 = strlen(argv[1]);
    for (int i = 0; i < len1; ++i){
	if (argv[1][i] > '9' || argv[1][i] < '0'){
	    printf("Not a number.\n");
	    exit(1);
	}
	n = n*10 + argv[1][i]-'0';
    }
    int p[n][2];    // 0 is root, 0 sends to 1, 1 sends to 2 .. n-1 sends to 0
    for (int i = 0; i < n; ++i)
	pipe(p[i]);
    for (int i = 1; i < n; ++i){
	int f = fork();
	if (f == 0){
	    // inside child i
	    srand(clock()^time(NULL)^getpid());
	    int read_from = p[i-1][0];
	    int write_to = p[i][1];
	    for (int j = 0; j < n; ++j)
		for (int k = 0; k < 2; ++k)
		    if ( !(j == i-1 && k == 0) && !(j == i && k == 1) )
			close(p[j][k]);
	    while(1){
		int x;
		if (read(read_from, &x, sizeof(int)) <= 0){
		    close(read_from);
		    close(write_to);
		    exit(0);
		}
		if (isboltz(x)){
		    if (rand()%10 == 0){
			close(read_from);
			close(write_to);
			printf("%d: %d\n", i, x);
			printf("Oh no! Mistake, %d loses!\n", i);
			exit(0);
		    }
		    printf("%d: BOLTZ\n", i);
		}else{
		    printf("%d: %d\n", i, x);
		}
		++x;
		if (write(write_to, &x, sizeof(int)) <= 0){
		    close(read_from);
		    close(write_to);
		    exit(0);
		}
	    }
	    exit(0);
	}
    }
    srand(clock()^time(NULL)^getpid());
    int read_from = p[n-1][0];
    int write_to = p[0][1];
    for (int i = 0; i < n; ++i)
	for (int j = 0; j < 2; ++j)
	    if ( !(i == n-1 && j == 0) && !(i == 0 && j == 1) )
		close(p[i][j]);
    int x = 0;
    while(1){
	++x;
	if (write(write_to, &x, sizeof(int)) <= 0){
	    close(read_from);
	    close(write_to);
	    break;
	}
	int rr = read(read_from, &x, sizeof(int));
	if (rr <= 0){
	    close(read_from);
	    close(write_to);
	    break;
	}
	if (isboltz(x)){
	    if (rand()%10 == 0){
		close(read_from);
		close(write_to);
		printf("0: %d\n", x);
		printf("Oh no! Mistake, 0 loses!\n");
		break; 
	    }
	    printf("0: BOLTZ\n");
	}else{
	    printf("0: %d\n", x);
	}
    }
    for (int i = 1; i < n; ++i)
	wait(0);
    return 0;
}
