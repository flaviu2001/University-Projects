import socket
import struct
import threading

e = threading.Event()
e.clear()
my_lock = threading.Lock()


def worker(cs):
    print('client from: ', cs.getpeername(), cs)
    file_path = cs.recv(1024).decode("ascii")
    file = None
    try:
        file = open(file_path, "r")
    except IOError:
        print("Unable to open file")
        cs.send("!i", -1)
    file_str = ""
    file_len = 0
    for x in file.readlines():
        file_len += len(x)
        file_str += x
    cs.send(struct.pack("!i", file_len))
    cs.send(file_str.encode())


rs = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
rs.bind(('127.0.0.1', 1234))
rs.listen(5)
print("Server started.")
while True:
    client_socket, client_address = rs.accept()
    t = threading.Thread(target=worker, args=(client_socket,))
    t.start()
