import socket, struct, select, sys, random

"""
! - (from or to) network
c - char
h - short
H - unsigned short
i - int
I - unsigned int
q - long long
Q - unsigned long long
f - float
d - double
"""

def tcp_send_int(sock, x):
	# print("Sending {0}".format(x))
	sock.send(struct.pack("!i", x))

def tcp_recv_int(sock):
	x = struct.unpack("!i", sock.recv(4))[0]
	# print("Received {0}".format(x))
	return x

def tcp_send_string(sock, string):
	print("Sending {0}".format(string))
	sock.send(string.encode('ascii'))

def tcp_recv_string(sock, sz=1024):
	string = sock.recv(sz).decode('ascii')
	# print("Received {0}".format(string))
	return string

def tcp_server_init(ip_addr, port):
	return socket.create_server((ip_addr, port), family=socket.AF_INET, backlog=10, reuse_port=True)

def tcp_client_init(ip_addr, port):
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.connect((ip_addr, port))
	return s

def udp_send_int(sock, x, dest_addr):
	print("Sending {0}".format(x))
	sock.sendto(struct.pack("!i", x), dest_addr)

def udp_recv_int(sock):
	number, addr = sock.recvfrom(4)
	converted_number = struct.unpack('!i', number)[0]
	print("Received {0}".format(converted_number))
	return (converted_number, addr)

def udp_send_string(sock, string, dest_addr):
	# print("Sending {0}".format(string))
	sock.sendto(string.encode('ascii'), dest_addr)

def udp_recv_string(sock):
	string, addr = sock.recvfrom(1024)
	converted_string = string.decode('ascii')
	# print("Received {0}".format(converted_string))
	return (converted_string, addr)

def udp_client_init():
	return socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)


def str_to_pair(addr):
	pos = None
	for i in range(len(addr)):
		if addr[i] == ':':
			pos = i
	ip = addr[:pos]
	port = int(addr[pos+1:])
	return (ip, port)

def pair_to_str(addr):
	return "{0}:{1}".format(addr[0], addr[1])


csock = tcp_client_init('192.168.100.5', 7000)
udpsock = udp_client_init()
udpsock.bind(('0.0.0.0', random.randint(50000, 60000)))
tcp_send_int(csock, udpsock.getsockname()[1])
clients = set()
n = tcp_recv_int(csock)
for _ in range(n):
	sz = tcp_recv_int(csock)
	clients.add(str_to_pair(tcp_recv_string(csock, sz)))
# print(clients)
master = [sys.stdin, csock, udpsock]
while True:
	ready_read, _, _ = select.select(master, [], [])
	for fd in ready_read:
		if fd == csock:
			case = tcp_recv_int(csock)
			sz = tcp_recv_int(csock)
			addr = str_to_pair(tcp_recv_string(csock, sz))
			if case == 0:
				clients.add(addr)
				print("Client {0} has connected".format(pair_to_str(addr)))
			else:
				clients.remove(addr)
				print("Client {0} has disconnected".format(pair_to_str(addr)))
			# print(clients)
		elif fd == sys.stdin:
			msg = input()
			if msg == "QUIT":
				tcp_send_string(csock, msg)
				exit(0)
			for x in clients:
				udp_send_string(udpsock, msg, x)
		elif fd == udpsock:
			msg, addr = udp_recv_string(udpsock)
			print("{0} -> {1}".format(pair_to_str(addr), msg))
