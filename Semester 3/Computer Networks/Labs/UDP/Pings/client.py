import socket
import sys
import time

udp_socket = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
server_address = (sys.argv[1], int(sys.argv[2]))
message = str(list(range(1, 10)))
begin = time.time()
udp_socket.sendto(message.encode("UTF-8"), server_address)
received, address = udp_socket.recvfrom(1024)
end = time.time()
if message == received.decode("UTF-8"):
    print("Same message")
else:
    print("{0} differs from {1}".format(message, received.decode("UTF-8")))
print("It took {0} seconds".format(end-begin))
