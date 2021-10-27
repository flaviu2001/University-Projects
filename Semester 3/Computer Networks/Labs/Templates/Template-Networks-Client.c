#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#ifndef _WIN32
	#include <sys/types.h>
	#include <sys/socket.h>
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

#define SERVER_PORT 1234
#define SERVER_IP "127.0.0.1"

void connection_init(int *c)
{
	#ifdef WIN32
	    WSADATA wsaData;
	    if (WSAStartup(MAKEWORD(2, 2), &wsaData) < 0) {
	        perror("Error initializing the Windows Sockets Library");
	        return -1;
	    }
	#endif
    *c = socket(AF_INET, SOCK_STREAM, 0);
    if (*c < 0) {
        perror("Error on socket creation\n");
        exit(1);
    }
    struct sockaddr_in server;
    memset(&server, 0, sizeof(server));
    server.sin_port = htons(SERVER_PORT);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr(SERVER_IP);
    if (connect(*c, (struct sockaddr*) &server, sizeof(server)) < 0) {
        perror("Error on server connection\n");
        exit(1);
    }
}

void connection_finish(int c)
{
	#ifdef WIN32
	    WSACleanup();
	    closesocket(c);
	#else
    	close(c);
	#endif
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

void sendShort(int c, int16_t x)
{
	x = htons(x);
	int sizeof_x = sizeof(x);
	if (send(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to send a short value");
		exit(1);
	}
	printf("Sent a short value: %hd\n", ntohs(x));
}

void sendUShort(int c, uint16_t x)
{
	x = htons(x);
	int sizeof_x = sizeof(x);
	if (send(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to send a short value");
		exit(1);
	}
	printf("Sent a short value: %hu\n", ntohs(x));	
}

void sendLong(int c, int32_t x)
{
	x = htonl(x);
	int sizeof_x = sizeof(x);
	if (send(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to send a short value");
		exit(1);
	}
	printf("Sent a long value: %d\n", ntohl(x));
}

void sendULong(int c, uint32_t x)
{
	x = htonl(x);
	int sizeof_x = sizeof(x);
	if (send(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to send a short value");
		exit(1);
	}
	printf("Sent a long value: %u\n", ntohl(x));
}

void sendFloat(int c, float x)
{
	x = htonf(x);
	int sizeof_x = sizeof(x);
	if (send(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		perror("Failed to send a float value");
		exit(1);
	}
	printf("Sent a float value: %f\n", ntohf(x));
}

void sendChar(int c, char q)
{
	int sizeof_x = sizeof(q);
	if (send(c, (char*)&q, sizeof(q), 0) < sizeof_x) {
		perror("Failed to send a char value");
		exit(1);
	}
	printf("Sent a char value: %c\n", q);
}

void sendString(int c, char *S)
{
	int n = strlen(S);
	sendLong(c, n);
	for (int i = 0; i < n; ++i)
		sendChar(c, S[i]);
}

int16_t recvShort(int c)
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

uint16_t recvUShort(int c)
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

int32_t recvLong(int c)
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

uint32_t recvULong(int c)
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

float recvFloat(int c)
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

char recvChar(int c)
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

char* recvString(int c)
{
	int n = recvLong(c);
	char *S = (char*)malloc(n*sizeof(char));
	for (int i = 0; i < n; ++i)
		S[i] = recvChar(c);
	return S;
}

int main() {
	int c;
	connection_init(&c);

	connection_finish(c);
}