import sys
import socket
import select

port = int(sys.argv[2])
IP_address = sys.argv[1]

master = []
hello_message = "Hello client! Welcome to the chat!"

def sendToAll(message, ignore_list):
    for fd in master:
        if fd not in ignore_list:
            fd.send(bytes(message, "ascii"))

def getIpAddress(socket_descriptor):
    peer = socket_descriptor.getpeername()
    return peer

if __name__ == "__main__":
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM, 0)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

    master.append(server_socket)

    server_socket.bind((IP_address, port))

    server_socket.listen(10)

    while True:
        ready_to_read, _, _ = select.select(master, [], [])

        for fd in ready_to_read:
            if fd == server_socket:
                client_socket, address = server_socket.accept()
                master.append(client_socket)
                client_socket.send(bytes(hello_message, "ascii"))
            else:
                received_message = fd.recv(1024).decode("ascii")
                if received_message == "QUIT":
                    master.remove(fd)
                    fd.close()
                else:
                    message = str(getIpAddress(fd)[0]) + "," + str(getIpAddress(fd)[1])  + " : " + received_message
                    sendToAll(message, [server_socket, fd])