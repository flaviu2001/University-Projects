#include <stdio.h>
	#ifndef _WIN32
	#include <sys/types.h>
	#include <sys/socket.h>
	#include <stdio.h>
	#include <netinet/in.h>
	#include <netinet/ip.h>
	#include <arpa/inet.h>
	#include <unistd.h> 
	#define closesocket close
	typedef int SOCKET;
#else
	#define _WINSOCK_DEPRECATED_NO_WARNINGS
	#include<WinSock2.h>
	#include<cstdint>
	#include<wsipv6ok.h>
#endif

#include <string.h>

float htonf(float val) {
    int32_t rep;
    memcpy(&rep, &val, sizeof rep);
    rep = htonl(rep);
    memcpy(&val, &rep, sizeof rep);
    return val;
}

float ntohf(float val) {
    int32_t rep;
    memcpy(&rep, &val, sizeof rep);
    rep = ntohl(rep);
    memcpy(&val, &rep, sizeof rep);
    return val;
}

int main() {
	#ifdef WIN32
	    WSADATA wsaData;
	    if (WSAStartup(MAKEWORD(2, 2), &wsaData) < 0) {
	        printf("Error initializing the Windows Sockets Library");
	        return -1;
	    }
	#endif
    int c;
    c = socket(AF_INET, SOCK_STREAM, 0);
    if (c < 0) {
        printf("Eroare la crearea socketului client\n");
        return 1;
    }
    struct sockaddr_in server;
    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr("127.0.0.1");
    if (connect(c, (struct sockaddr*) & server, sizeof(server)) < 0) {
        printf("Eroare la conectarea la server\n");
        return 1;
    }
    int n;
    scanf("%d", &n);
    n = htonl(n);
    send(c, &n, sizeof(int), 0);
    n = ntohl(n);
    for (int i = 0; i < n; ++i) {
    	float x;
    	scanf("%f", &x);
    	x = htonf(x);
    	send(c, &x, sizeof(x), 0);
    }
    recv(c, &n, sizeof(n), 0);
    n = ntohl(n);
    printf("Received %d.\n", n);
    for (int i = 0; i < n; ++i) {
    	float x;
    	recv(c, &x, sizeof(x), 0);
    	x = ntohf(x);
    	printf("%f ", x);
    }
    printf("\n");
	#ifdef WIN32
	    WSACleanup();
	    closesocket(c);
	#else
    	close(c);
	#endif
}