class Student:
	def __init__(self, ID, Name, Grade):
		self.ID = ID
		self.Name = Name
		self.Grade = Grade

def student_exists(s, student):
	for i in s:
		if i.ID == student.ID:
			return True
	return False

def add_student(s, student):
	if student_exists(s, student):
		print('A student with same ID already exists')
		return
	s.append(student)
	print('Succesfully added student\n')

def rem_student(s, ID):
	for i in s:
		if i.ID == ID:
			print('Succesfully removed student\n')
			s.remove(i)
			return
	print('No student with ID found\n')

def show_students(s, n = 0):
	cnt = 0
	for i in s:
		if i.Grade >= n:
			cnt += 1
			print('Student ' + str(i.ID) + ' with name ' + i.Name + ' and grade ' + str(i.Grade))
	if cnt == 0:
		print('No students found')
	print()

def print_menu():
	print('1. Add a student')
	print('2. Remove a student with given ID')
	print('3. Show all students')
	print('4. Show students with grade higher or equal to a given number')
	print('5. Exit\n')

def start():
	s = []
	while True:
		print_menu()
		choice = input('>')
		if choice == '1':
			sid = input('Input student id: ')
			if len(sid) == 0:
				print('Inputted id is empty')
				continue
			sname = input('Input student name: ')
			if len(sname) == 0:
				print('Inputted name is empty')
				continue
			sgrade = input('Input student grade: ')
			if not sgrade.isdigit() or int(sgrade) < 1 or int(sgrade) > 10:
				print('Inputted grade not a number between 1 and 10')
				continue
			add_student(s, Student(sid, sname, int(sgrade)))
		elif choice == '2':
			sid = input('Input student id: ')
			if len(sid) == 0:
				print('Inputted id is empty')
				continue
			rem_student(s, sid)
		elif choice == '3':
			show_students(s)
		elif choice == '4':
			sgrade = input('Input student grade: ')
			if not sgrade.isdigit() or int(sgrade) < 1 or int(sgrade) > 10:
				print('Inputted grade not a number between 1 and 10')
				continue
			show_students(s, int(sgrade))
		elif choice == '5':
			return
		else:
			print('Invalid command')

start()