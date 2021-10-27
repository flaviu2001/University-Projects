#import socket for all socket related primitives
import socket
# we need struct in order to be able to pack data in
# a stream of bytes so that we can actually send
# an integer as a binary four byte sequence - instead
# of a string
import struct

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# input return actually a string and we need an int
cmd=input('command=')

# The obscure struct_addr is elegantly replaced by
# a simple pair - very convenient. Replace the IP Address with
# the one of your server
s.connect( ("192.168.0.8",4321) )

# pack the value of a as a short int (16 bits) in network representation
# res = s.send(struct.pack("s", a))
for x in cmd:
	s.send(struct.pack("c", x.encode("ascii")))
s.send(struct.pack("c", '\0'.encode("ascii")))
while True:
	c = s.recv(1)
	c = c.decode('ascii')
	if c == '\0':
		break
	print(c, end="")
c = s.recv(4)
c = struct.unpack('!i', c)
print("The exit code is {0}.".format(c[0].__format__('d')))

s.close()

  
