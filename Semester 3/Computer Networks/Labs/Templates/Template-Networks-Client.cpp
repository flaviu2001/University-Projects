#include <cstdlib>
#include <cstring>
#include <iostream>
#ifndef _WIN32
	#include <sys/types.h>
	#include <sys/socket.h>
	#include <netinet/in.h>
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
using namespace std;

void connection_init(int &c)
{
	#ifdef WIN32
	    WSADATA wsaData;
	    if (WSAStartup(MAKEWORD(2, 2), &wsaData) < 0) {
	        perror("Error initializing the Windows Sockets Library");
	        return -1;
	    }
	#endif
    c = socket(AF_INET, SOCK_STREAM, 0);
    if (c < 0) {
        cerr << "Error on socket creation\n";
        exit(1);
    }
    sockaddr_in server{};
    memset(&server, 0, sizeof(server));
    server.sin_port = htons(SERVER_PORT);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr(SERVER_IP);
    if (connect(c, (struct sockaddr*) &server, sizeof(server)) < 0) {
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
		cerr << "Failed to send a short value";
		exit(1);
	}
	int16_t copy = ntohs(x);
	cout << "Sent a short value: " << copy << "\n";
}

void sendUShort(int c, uint16_t x)
{
	x = htons(x);
	int sizeof_x = sizeof(x);
	if (send(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		cerr << "Failed to send a short value";
		exit(1);
	}
	uint16_t copy = ntohs(x);
	cout << "Sent a short value: " << copy << "\n";
}

void sendLong(int c, int32_t x)
{
	x = htonl(x);
	int sizeof_x = sizeof(x);
	if (send(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		cerr << "Failed to send a short value";
		exit(1);
	}
	int32_t copy = ntohl(x);
	cout << "Sent a long value: " << copy << "\n";
}

void sendULong(int c, uint32_t x)
{
	x = htonl(x);
	int sizeof_x = sizeof(x);
	if (send(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		cerr << "Failed to send a short value";
		exit(1);
	}
	uint32_t copy = ntohl(x);
	cout << "Sent a long value: " << copy << "\n";
}

void sendFloat(int c, float x)
{
	x = htonf(x);
	int sizeof_x = sizeof(x);
	if (send(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		cerr << "Failed to send a float value";
		exit(1);
	}
	float copy = ntohf(x);
	cout << "Sent a float value: " << copy << "\n";
}

void sendChar(int c, char q)
{
	int sizeof_x = sizeof(q);
	if (send(c, (char*)&q, sizeof(q), 0) < sizeof_x) {
		cerr << "Failed to send a char value";
		exit(1);
	}
	cout << "Sent a char value: " << q << "\n";
}

void sendString(int c, string S)
{
	int n = S.length();
	sendLong(c, n);
	for (int i = 0; i < n; ++i)
		sendChar(c, S[i]);
}

int16_t recvShort(int c)
{
	int16_t x;
	int sizeof_x = sizeof(x);
	if (recv(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		cerr << "Failed to receive a short value";
		exit(1);
	}
	int16_t copy = ntohs(x);
	cout << "Received a short value: " << copy << "\n";
	return ntohs(x);
}

uint16_t recvUShort(int c)
{
	uint16_t x;
	int sizeof_x = sizeof(x);
	if (recv(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		cerr << "Failed to receive a short value";
		exit(1);
	}
	uint16_t copy = ntohs(x);
	cout << "Received a short value: " << copy << "\n";
	return ntohs(x);
}

int32_t recvLong(int c)
{
	int32_t x;
	int sizeof_x = sizeof(x);
	if (recv(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		cerr << "Failed to receive a short value";
		exit(1);
	}
	int32_t copy = ntohl(x);
	cout << "Received a long value: " << copy << "\n";
	return ntohl(x);
}

uint32_t recvULong(int c)
{
	uint32_t x;
	int sizeof_x = sizeof(x);
	if (recv(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		cerr << "Failed to receive a short value";
		exit(1);
	}
	uint32_t copy = ntohl(x);
	cout << "Received a long value: " << copy << "\n";
	return ntohl(x);
}

float recvFloat(int c)
{
	float x;
	int sizeof_x = sizeof(x);
	if (recv(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		cerr << "Failed to receive a float value";
		exit(1);
	}
	float copy = ntohf(x);
	cout << "Received a float value: " << copy << "\n";
	return ntohf(x);
}

char recvChar(int c)
{
	char x;
	int sizeof_x = sizeof(x);
	if (recv(c, (char*)&x, sizeof(x), 0) < sizeof_x) {
		cerr << "Failed to receive a char value";
		exit(1);
	}
	cout << "Received a char value: " << x << "\n";
	return x;
}

string recvString(int c)
{
	int n = recvLong(c);
	string string;
	for (int i = 0; i < n; ++i)
		string += recvChar(c);
	return string;
}

int main() {
	int c;
	connection_init(c);

	connection_finish(c);
}