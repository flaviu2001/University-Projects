/* 
// Multiperson chat using select
// Client part
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
 
fd_set read_fds,master; // temp file descriptor list for select()
int sock;        //socket
struct sockaddr_in servaddr; 
char buf[256];  // buffer for client data
int nbytes, ret, int_port;
 
int main(int argc, char **argv)
{
    
    if (argc < 3 ) {
      printf("Usage:\n%s <hostname or IP address> <portno>\n",argv[0]);
      exit(1);
    }
    
    
    int_port = atoi(argv[2]);
    
    int ipaddr = inet_addr(argv[1]);
    // check if address is a hostname
    
    printf("%s => %d ip address\n",argv[1],ipaddr);
    if (ipaddr == -1 ) {
      struct in_addr inaddr;
      struct hostent * host = gethostbyname( argv[1] );
      if (host == NULL ) {
               printf("Error getting the host address\n");
               exit(1);
      }
      memcpy(&inaddr.s_addr, host->h_addr_list[0],sizeof(inaddr));
      printf("Connecting to %s ...\n",inet_ntoa( inaddr) );
      memcpy(&ipaddr, host->h_addr_list[0],sizeof(unsigned long int)) ;
    }
 
    // get the socket
    if ((sock = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
        perror("socket");
        exit(1);
    }
    
    memset(&servaddr,0, sizeof(servaddr));
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = ipaddr;
    servaddr.sin_port = htons( int_port );
    // connect to server
    if (connect(sock, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0 ) {
      perror("connect");
      exit(1);
    }
  
    // add the listener to the master set
    FD_ZERO(&read_fds);    // clear the set
    FD_ZERO(&master);
    FD_SET(0, &master);
    FD_SET(sock, &master);
 
    for(;;) {
      read_fds = master;
      if (select(sock+1, &read_fds, NULL, NULL, NULL) == -1) {
                perror("select");
               exit(1);
      }
      
      // check if read from keyboard 
      if ( FD_ISSET(0, &read_fds) ) {
               nbytes = read(0, buf,sizeof(buf)-1);
               ret = send(sock, buf, nbytes,0);
               if (ret <= 0 ){
                       perror("send");
                       exit(1);
               }
        //else  printf("WARNING Not all data has been sent: %d bytes out of %d\n", ret, nbytes);
      }
      
      // check if read from server
      if ( FD_ISSET(sock, &read_fds) ) {
               nbytes = read(sock, buf, sizeof(buf)-1);
               if (nbytes <= 0) { 
                       printf("Server has closed connection... closing...\n");
                       exit(2);
               }
               write(1,buf, nbytes);
     }
    }
    
    return 0;
}
 
