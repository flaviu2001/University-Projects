#include <stdio.h>
#ifndef _WIN32

#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
// for inet_ntoa
#include <arpa/inet.h>
// for close
#include <unistd.h> 
#define closesocket close
typedef int SOCKET;
#else
#define _WINSOCK_DEPRECATED_NO_WARNINGS
// on Windows include and link these things
#include<WinSock2.h>
// for uint16_t 
#include<cstdint>
// for inet_ntoa
#include<wsipv6ok.h>
// this is how we can link a library directly from the source code with the VC++ compiler –
// otherwise got o project settings and link to it explicitly
#pragma comment(lib,"Ws2_32.lib")

#endif

#include <string.h>

int main(int argc, char **argv) {
	if (argc != 2) {
		printf("Needs exactly one argument.\n");
		return 1;
	}

// initialize the Windows Sockets Library only when compiled on Windows
#ifdef WIN32
    WSADATA wsaData;
    if (WSAStartup(MAKEWORD(2, 2), &wsaData) < 0) {
        printf("Error initializing the Windows Sockets Library");
        return -1;
    }
#endif
    int c;

    //  AF_INET - communication domain
    // SOCK_STREAM - communication type
    // 0 - protocol (IP)
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

    int file_path_len = strlen(argv[1]);
    printf("Sending a file name: %s\n", argv[1]);
    send(c, argv[1], file_path_len * sizeof(char), 0);

    int32_t file_len;
    recv(c, &file_len, sizeof(file_len), 0);
    file_len = ntohl(file_len);
    char str[file_len+1];
    str[file_len] = 0;
    recv(c, str, file_len*sizeof(char), 0);
    char file_path[file_path_len+6];
    for (int i = 0; i < file_path_len; ++i)
    	file_path[i] = argv[1][i];
    file_path[file_path_len] = '-';
    file_path[file_path_len+1] = 'c';
    file_path[file_path_len+2] = 'o';
    file_path[file_path_len+3] = 'p';
    file_path[file_path_len+4] = 'y';
    file_path[file_path_len+5] = 0;
    FILE *f = fopen(file_path, "w");
    fprintf(f, "%s", str);

#ifdef WIN32
    // Release the Windows Sockets Library
    WSACleanup();
    closesocket(c);
#else
    close(c);
#endif
}