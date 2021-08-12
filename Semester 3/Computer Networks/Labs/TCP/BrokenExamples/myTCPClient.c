include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdio.h>
#include <unistd.h> /* close */
 
#define SERVER_PORT 1500
#define MAX_MSG 100
 
int main (int argc, char *argv[]) {
 
  int sd, rc, i;
  struct sockaddr_in servAddr;
  struct hostent *h;
  char msg[300];
   if(argc < 3) {
    printf("usage: %s <server> <text>\n",argv[0]);
    exit(1);
  }
 
  h = gethostbyname(argv[1]);
  if (h==NULL) {
    printf("%s: unknown host '%s'\n",argv[0],argv[1]);
    exit(1);
  }
 
  servAddr.sin_family = h->h_addrtype;
  memcpy((char *) &servAddr.sin_addr.s_addr, h->h_addr_list[0], h->h_length);
  servAddr.sin_port = htons(SERVER_PORT);
/* create socket */
  sd = socket(AF_INET, SOCK_STREAM, 0);
  if(sd<0) {
    perror("cannot open socket ");
    exit(1);
  }
  /* connect to server */
  rc = connect(sd, (struct sockaddr *) &servAddr, sizeof(servAddr));
  if(rc<0) {
    perror("cannot connect ");
    exit(1);
  }
  write(rc, argv[1],strlen(argv[1]+1) );
  read(rc, msg, 300);
  printf(”Received back: %s\n”, msg);
  close(rc);
  return 0;
}
