from threading import Thread
import socket
import sys
import time
import random


def worker_leader(sock, port):
	while True:
		sock.sendto('leader'.encode('ascii'), ('<broadcast>', port))
		time.sleep(5)


def handle_leader(port):
	sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEPORT, 1)
	sock.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
	# sock.bind(('0.0.0.0', port))
	t = Thread(target=worker_leader, args=(sock, port))
	t.start()
	teacher = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	teacher.connect(('192.168.43.59', 1234))
	while True:
		msg, addr = sock.recvfrom(1024)
		msg = msg.decode('ascii')
		print('Gotta send to teacher {0} from {1}'.format(msg, addr))
		teacher.send(msg.encode('ascii'))
		answer = teacher.recv(1024)
		answer = answer.decode('ascii')
		print(answer)
		sock.sendto("{0} : {1}".format(msg, answer).encode('ascii'), ('<broadcast>', port))


def worker_student(sock):
	while True:
		msg, addr = sock.recvfrom(1024)
		msg = msg.decode('ascii')
		if msg != 'leader':
			print("{0} from {1}".format(msg, addr))

def handle_student(port):
	sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEPORT, 1)
	sock.bind(('0.0.0.0', port))
	msg = None
	addr = None
	while msg != 'leader':
		msg, addr = sock.recvfrom(1024)
		msg = msg.decode('ascii')
	Thread(target=worker_student, args=(sock,)).start()
	while True:
		if random.randint(1, 2) == 1:
			msg = 'intrebare'
			sock.sendto(msg.encode('ascii'), addr)
		time.sleep(3)


if __name__ == "__main__":
	group = int(sys.argv[1])
	leaderbit = sys.argv[2]

	if leaderbit == "1":
		handle_leader(group)
	else:
		handle_student(group)
