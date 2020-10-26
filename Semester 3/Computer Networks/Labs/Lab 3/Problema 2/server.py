import random
import socket
import struct

random.seed()
start = 1
stop = 2 ** 17 - 1
my_num = random.randint(start, stop)
print('Server number: ', my_num)

if __name__ == '__main__':
    udp_socket = None
    try:
        udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        udp_socket.bind(('127.0.0.1', 1234))
    except socket.error as msg:
        print(msg.strerror)
        exit(-1)

    players = set()

    while True:
        client_number, address = udp_socket.recvfrom(4)
        players.add(address)
        number = struct.unpack('!I', client_number)[0]
        if number > my_num:
            udp_socket.sendto(b'S', address)
        if number < my_num:
            udp_socket.sendto(b'H', address)
        if number == my_num:
            winner = address
            break

    for addr in players:
        print(addr)
        if addr == winner:
            udp_socket.sendto(b'G', addr)
        else:
            udp_socket.sendto(b'L', addr)


