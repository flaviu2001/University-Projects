import socket
import threading

udp_socket = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
udp_socket.bind(('127.0.0.1', 1234))
print("UDP server running.")


def worker(message, address):
    copy_socket = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
    print("{0}\nclient address: {1}".format(message.decode("UTF-8"), address))
    copy_socket.sendto(message, address)


while True:
    msg, addr = udp_socket.recvfrom(1024)
    threading.Thread(target=worker, args=(msg, addr)).start()
