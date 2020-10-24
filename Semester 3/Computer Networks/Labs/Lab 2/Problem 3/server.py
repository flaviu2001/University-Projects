import socket
import struct
from multiprocessing import Process

rs = socket.create_server(('127.0.0.1', 1234), family=socket.AF_INET, backlog=10, reuse_port=True)
print("Server started.")


def worker(cs):
    n = struct.unpack("!i", cs.recv(4))[0]
    array = []
    for i in range(n):
        x = struct.unpack("!f", cs.recv(4))[0]
        print(x, end=" ")
        array.append(x)
    array = sorted(array)
    cs.send(struct.pack("!i", n))
    for x in array:
        cs.send(struct.pack("!f", x))


while True:
    client_socket, client_address = rs.accept()
    p = Process(target=worker, args=(client_socket,))
    p.start()
