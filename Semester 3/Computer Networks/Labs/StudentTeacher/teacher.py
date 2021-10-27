import socket
import select

sock = socket.create_server(('0.0.0.0', 1234), family=socket.AF_INET, backlog=10, reuse_port=True)
sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
master = [sock]
while True:
	ready_read, _, _ = select.select(master, [], [])
	for fd in ready_read:
		if fd == sock:
			client_sock, addr = sock.accept()
			master.append(client_sock)
		else:
			msg = fd.recv(1024)
			msg = msg.decode('ascii')
			print("Teacher received {0}".format(msg))
			fd.send('raspuns'.encode('ascii'))
