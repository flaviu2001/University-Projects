#ifdef _WIN32
#define _WINSOCK_DEPRECATED_NO_WARNINGS
#endif

// ServerSuma-Iterativ.cpp : Defines the entry point for the console application.
// exists on all platforms
#include <stdio.h>

// this section will only be compiled on NON Windows platforms

#ifndef _WIN32
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
// for inet_ntoa
#include <arpa/inet.h>
#define closesocket close
typedef int SOCKET;
#else

// on Windows include and link these things
#include<WinSock2.h>
// for uint16_t 
#include<cstdint>
// for inet_ntoa
#include<wsipv6ok.h>
// this is how we can link a library directly from the source code with the VC++ compiler –
// otherwise got o project settings and link to it explicitly
#pragma comment(lib,"Ws2_32.lib")

typedef int socklen_t;
#endif



int main() {

    SOCKET s;
    struct sockaddr_in server, client;
    int c, l, err;

    // initialize the Windows Sockets LIbrary only when compiled on Windows
#ifdef WIN32
    WSADATA wsaData;
    if (WSAStartup(MAKEWORD(2, 2), &wsaData) < 0) {
        printf("Error initializing the Windows Sockets LIbrary");
        return -1;
    }
#endif
    //  AF_INET - communication domain
    // SOCK_STREAM - communication type
    // 0 - protocol (IP)
    s = socket(AF_INET, SOCK_STREAM, 0);

    if (s < 0) {
        printf("Eroare la crearea socketului server\n");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    // INADDR_ANY, as defined in netinet/in.h, 
    // the caller is requesting that the socket be bound to all network interfaces on the host.
    server.sin_addr.s_addr = INADDR_ANY;

    // bind() assigns the address specified by server to the socket referred to by the file descriptor s
    if (bind(s, (struct sockaddr*) & server, sizeof(server)) < 0) {
        perror("Bind error:");
        return 1;
    }

    // puts the server socket in a passive mode, 
    // where it waits for the client to approach the server to make a connection.
    listen(s, 5);
    l = sizeof(client);
    memset(&client, 0, sizeof(client));

    while (1) {

        uint16_t a, b, suma;
        printf("Listening for incomming connections\n");
        // first connection request on the queue of pending connections for the listening socket, s, 
        // creates a new connected socket c, and returns a new file descriptor referring to that socket.
        c = accept(s, (struct sockaddr*) & client, (socklen_t*)&l);

        err = errno;
#ifdef WIN32
        err = WSAGetLastError();
#endif

        if (c < 0) {
            printf("accept error: %d", err);
            continue;
        }

        // converts the Internet host address , given in network byte order, to a string in IPv4 dotted-decimal notation.
        printf("Incomming connected client from: %s:%d\n", 
            inet_ntoa(client.sin_addr), ntohs(client.sin_port));

        // serving the connected client
        int res = recv(c, (char*)&a, sizeof(a), 0);
        //check we got an unsigned short value
        if (res != sizeof(a)) 
            printf("Error receiving operand\n");

        res = recv(c, (char*)&b, sizeof(b), 0);
        if (res != sizeof(b)) 
            printf("Error receiving operand\n");



        //decode the value to the local representation
        a = ntohs(a);
        b = ntohs(b);

        suma = a + b;
        suma = htons(suma);
        res = send(c, (char*)&suma, sizeof(suma), 0);

        if (res != sizeof(suma)) 
            printf("Error sending result\n");

        //on Linux closesocket does not exist but was defined above as a define to close
        closesocket(c);

    }


#ifdef WIN32
    // Release the Windows Sockets Library
    WSACleanup();
#endif

}

