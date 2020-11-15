import socket, struct, select

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
	# print("Sending {0}".format(string))
	sock.send(string.encode('ascii'))

def tcp_recv_string(sock):
	string = sock.recv(1024).decode('ascii')
	# print("Received {0}".format(string))
	return string

def tcp_server_init(ip_addr, port):
	return socket.create_server((ip_addr, port), family=socket.AF_INET, backlog=10, reuse_port=True)

def tcp_client_init(ip_addr, port):
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.connect((ip_addr, port))
	return s

def udp_send_int(sock, x, dest_addr):
	# print("Sending {0}".format(x))
	sock.sendto(struct.pack("!i", x), dest_addr)

def udp_recv_int(sock):
	number, addr = sock.recvfrom(4)
	converted_number = struct.unpack('!i', number)[0]
	# print("Received {0}".format(converted_number))
	return (converted_number, addr)

def udp_send_string(sock, string, dest_addr):
	# print("Sending {0}".format(string))
	sock.sendto(string.encode('ascii'), dest_addr)

def udp_recv_string(sock):
	string, addr = sock.recvfrom(1024)
	converted_string = string.decode('ascii')
	# print("Received {0}".format(converted_string))
	return (converted_string, addr)

def udp_server_init(ip_addr, port):
	udp_socket = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
	udp_socket.bind((ip_addr, port))
	return udp_socket

def udp_client_init():
	return socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)


def pair_to_str(addr):
	return "{0}:{1}".format(addr[0], addr[1])


rsock = tcp_server_init('0.0.0.0', 7000)
clients = set()
master = [rsock]
while True:
	ready_read, _, _ = select.select(master, [], [])
	for fd in ready_read:
		if fd == rsock:
			csock, addr1 = rsock.accept()
			cport = tcp_recv_int(csock)
			addr = (addr1[0], cport)
			for client in clients:
				tcp_send_int(client[0], 0)
				tcp_send_int(client[0], len(pair_to_str(addr)))
				tcp_send_string(client[0], pair_to_str(addr))
			tcp_send_int(csock, len(clients))
			for x in clients:
				tcp_send_int(csock, len(pair_to_str(x[1])))
				tcp_send_string(csock, pair_to_str(x[1]))
			clients.add((csock, addr))
			master.append(csock)
		else:
			msg = tcp_recv_string(fd)
			cl = None
			for x in clients:
				if x[0] == fd:
					cl = x
			if msg == "QUIT":
				for client in clients:
					if client != cl:
						tcp_send_int(client[0], 1)
						tcp_send_int(client[0], len(pair_to_str(cl[1])))
						tcp_send_string(client[0], pair_to_str(cl[1]))
				clients.remove(cl)
			fd.close()
			master.remove(fd)
