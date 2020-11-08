#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#ifndef _WIN32
	#include <sys/types.h>
	#include <sys/socket.h>
	#include <netinet/in.h>
	#include <netinet/ip.h>
	#include <unistd.h>
	#include <errno.h>
	#include <arpa/inet.h>
	#include <unistd.h> 
	#define closesocket close
	typedef int SOCKET;
#else
	#define _WINSOCK_DEPRECATED_NO_WARNINGS
	#include<WinSock2.h>
	#include<stdint.h>
	#include<wsipv6ok.h>
	typedef int socklen_t;
#endif

#define SERVER_PORT 1234

void server_init(SOCKET *s)
{
	struct sockaddr_in server;
	#ifdef WIN32
		WSADATA wsaData;
		if (WSAStartup(MAKEWORD(2, 2), &wsaData) < 0) {
		    printf("Error initializing the Windows Sockets Library");
		    exit(1);
		}
	#endif
	*s = socket(AF_INET, SOCK_STREAM, 0);
	if (*s < 0) {
	    perror("Error on server socket creation\n");
	    exit(1);
	}
	memset(&server, 0, sizeof(server));
	server.sin_port = htons(SERVER_PORT);
	server.sin_family = AF_INET;
	server.sin_addr.s_addr = INADDR_ANY;
	if (bind(*s, (struct sockaddr*) &server, sizeof(server)) < 0) {
	    perror("Bind error:");
	    exit(1);
	}
	listen(*s, 5);
}

int connect_to_client(SOCKET *s, struct sockaddr_in *client)
{
	memset(client, 0, sizeof(struct sockaddr_in));
	int l = sizeof(struct sockaddr_in);
	int c = accept(*s, (struct sockaddr*) client, (socklen_t*)&l);
	int err = errno;
	#ifdef WIN32
        err = WSAGetLastError();
	#endif
	if (c < 0) {
		printf("Accept error: %d", err);
		exit(1);
	}
	return c;
}

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

void sendShort(SOCKET c, int16_t x)
{
	x = htons(x);
	int sizeof_x = sizeof(x);
	if (send(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to send a short value");
		exit(1);
	}
	printf("Sent a short value: %hd\n", ntohs(x));
}

void sendUShort(SOCKET c, uint16_t x)
{
	x = htons(x);
	int sizeof_x = sizeof(x);
	if (send(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to send a short value");
		exit(1);
	}
	printf("Sent a short value: %hu\n", ntohs(x));	
}

void sendLong(SOCKET c, int32_t x)
{
	x = htonl(x);
	int sizeof_x = sizeof(x);
	if (send(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to send a short value");
		exit(1);
	}
	printf("Sent a long value: %d\n", ntohl(x));
}

void sendULong(SOCKET c, uint32_t x)
{
	x = htonl(x);
	int sizeof_x = sizeof(x);
	if (send(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to send a short value");
		exit(1);
	}
	printf("Sent a long value: %u\n", ntohl(x));
}

void sendFloat(SOCKET c, float x)
{
	x = htonf(x);
	int sizeof_x = sizeof(x);
	if (send(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to send a float value");
		exit(1);
	}
	printf("Sent a float value: %f\n", ntohf(x));
}

void sendChar(SOCKET c, char q)
{
	int sizeof_x = sizeof(q);
	if (send(c, (char*)&q, sizeof(q), 0) < sizeof_x) {
		perror("Failed to send a char value");
		exit(1);
	}
	printf("Sent a char value: %c\n", q);
}

void sendString(SOCKET c, char *S)
{
	int n = strlen(S);
	sendLong(c, n);
	for (int i = 0; i < n; ++i)
		sendChar(c, S[i]);
}

int16_t recvShort(SOCKET c)
{
	int16_t x;
	int sizeof_x = sizeof(x);
	if (recv(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to receive a short value");
		exit(1);
	}
	printf("Received a short value: %hd\n", ntohs(x));
	return ntohs(x);
}

uint16_t recvUShort(SOCKET c)
{
	uint16_t x;
	int sizeof_x = sizeof(x);
	if (recv(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to receive a short value");
		exit(1);
	}
	printf("Received a short value: %hu\n", ntohs(x));
	return ntohs(x);
}

int32_t recvLong(SOCKET c)
{
	int32_t x;
	int sizeof_x = sizeof(x);
	if (recv(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to receive a short value");
		exit(1);
	}
	printf("Received a long value: %d\n", ntohl(x));
	return ntohl(x);
}

uint32_t recvULong(SOCKET c)
{
	uint32_t x;
	int sizeof_x = sizeof(x);
	if (recv(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to receive a short value");
		exit(1);
	}
	printf("Received a long value: %u\n", ntohl(x));
	return ntohl(x);
}

float recvFloat(SOCKET c)
{
	float x;
	int sizeof_x = sizeof(x);
	if (recv(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to receive a float value");
		exit(1);
	}
	printf("Received a float value: %f\n", ntohf(x));
	return ntohf(x);
}

char recvChar(SOCKET c)
{
	char x;
	int sizeof_x = sizeof(x);
	if (recv(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to receive a char value");
		exit(1);
	}
	printf("Received a char value: %c\n", x);
	return x;
}

char* recvString(SOCKET c)
{
	int n = recvLong(c);
	char *S = (char*)malloc(n*sizeof(char));
	for (int i = 0; i < n; ++i)
		S[i] = recvChar(c);
	return S;
}

void server_finish()
{
	#ifdef WIN32
    	WSACleanup();
	#endif
}

int main() {
	SOCKET s;
	server_init(&s);
    while (1) {
        printf("Listening for incomming connections\n");
        struct sockaddr_in client;
        SOCKET c = connect_to_client(&s, &client);
        if (c == -1)
        	continue;
        printf("Incomming connected client from: %s:%d\n", inet_ntoa(client.sin_addr), ntohs(client.sin_port));

        

        closesocket(c);
    }
    server_finish();
}
