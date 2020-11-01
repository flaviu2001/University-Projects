#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <string.h>
#include <time.h>

int main(int argc, char **argv)
{
	if (argc != 2) {
		perror("Bad args");
		return 1;
	}
	unsigned len = sizeof(struct sockaddr_in);
	struct sockaddr_in Recv_addr, Sender_addr;
    Recv_addr.sin_family = AF_INET;  
    Recv_addr.sin_port = htons(7777);
    Recv_addr.sin_addr.s_addr = inet_addr(argv[1]);

	if (fork() == 0) {
    	//Child sends the queries and receives the times and dates
    	int child_sock = socket(AF_INET, SOCK_DGRAM, 0);
    	if (child_sock < 0) {
    		perror("Error on creating child socket:");
    		return 1;
    	}
    	int broadcast = 1;
	    if(setsockopt(child_sock,SOL_SOCKET,SO_BROADCAST,&broadcast,sizeof(broadcast)) < 0) {
	    	perror("Error in setting Broadcast option");
	        close(child_sock);
	        return 1;
	    }
	    struct timeval read_timeout;
		read_timeout.tv_sec = 1;
		read_timeout.tv_usec = 0;
		setsockopt(child_sock, SOL_SOCKET, SO_RCVTIMEO, &read_timeout, sizeof read_timeout);
	    int cnt = 0, last = -1;
	    while(1) {
	    	char recv_msg[100];
	    	int code = recvfrom(child_sock, recv_msg, 100, 0, (struct sockaddr*)&Sender_addr, &len);
	    	if (code < 0)
	    		++cnt;
	    	else
	    		printf("Child received : %s\n\n", recv_msg);
	    	if (cnt == last)
	    		continue;
	    	last = cnt;
	    	if (cnt%3 == 0) {
	    		char* msg = "TIMEQUERY";
	    		sendto(child_sock, msg, strlen(msg)+1, 0, (struct sockaddr*)&Recv_addr, (socklen_t)len);
	    	}
	    	if (cnt%10 == 0) {
	    		char* msg = "DATEQUERY";
	    		sendto(child_sock, msg, strlen(msg)+1, 0, (struct sockaddr*)&Recv_addr, (socklen_t)len);
	    	}
	    }
	    return 0;
    }

    int sock = socket(AF_INET, SOCK_DGRAM, 0);
    if (sock < 0) {
    	perror("Error on creating the socket:");
    	return 1;
    }
    if (bind(sock,(struct sockaddr*)&Recv_addr, sizeof (Recv_addr)) < 0) {
        perror("Error in binding:");
        close(sock);
        return 1;
    }
    printf("UDP application up and running.\n");
    while(1) {
    	//Parent simply calculates the times and dates whenever it gets the queries
    	int recvbufflen = 100;
		char recvbuff[recvbufflen];
    	if (recvfrom(sock,recvbuff,recvbufflen,0,(struct sockaddr*)&Sender_addr,&len) < 0)
    		perror("Error in recvfrom\n");
    	time_t t = time(NULL);
		struct tm curr_time = *localtime(&t);
		if (strcmp(recvbuff, "TIMEQUERY") == 0) {
			printf("Parent received a TIMEQUERY\n");
			char time[100];
			sprintf(time, "TIME %02d:%02d:%02d", curr_time.tm_hour, curr_time.tm_min, curr_time.tm_sec);
			sendto(sock, time, strlen(time)+1, 0, (struct sockaddr*) &Sender_addr, (socklen_t)len);
		}else if (strcmp(recvbuff, "DATEQUERY") == 0) {
			printf("Parent received a DATEQUERY\n");
			char time[100];
			sprintf(time, "DATE %04d:%02d:%02d", curr_time.tm_year + 1900, curr_time.tm_mon + 1, curr_time.tm_mday);
			sendto(sock, time, strlen(time)+1, 0, (struct sockaddr*) &Sender_addr, (socklen_t)len);
		}else printf("Rubbish - %s", recvbuff);
    }
    close(sock);
    return 0;
}
