import socket
import struct
from threading import Thread, Lock

rs = socket.create_server(('127.0.0.1', 1234), family=socket.AF_INET, backlog=10, reuse_port=True)
print("Server started.")

mutex = Lock()
over = False

def worker(cs, ca, elems):
	global over
	n = struct.unpack("!i", cs.recv(4))[0]
	print("Received {0} from {1}".format(n, ca))
	if n == 0:
		mutex.acquire()
		over = True
		mutex.release()
		return
	array = []
	mutex.acquire()
	for i in range(n):
		x = struct.unpack("!f", cs.recv(4))[0]
		print("Received {0} from {1}".format(x, ca))
		array.append(x)
	elems += array
	mutex.release()


sockets = []
elems = []
threads = []

while not over:
	client_socket, client_address = rs.accept()
	sockets.append(client_socket)
	t = Thread(target=worker, args=(client_socket, client_address, elems))
	threads.append(t)
	t.start()

for t in threads:
	t.join()

elems = sorted(elems) # Suppose this is a merge sort

for cs in sockets:
	cs.send(struct.pack("!i", len(elems)))
	for x in elems:
		cs.send(struct.pack("!f", x))
	cs.close()
