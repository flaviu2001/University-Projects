def getReal(x):
	return x[0]

def setReal(x, y):
	x[0] = y

def getImaginary(x):
	return x[1]

def setImaginary(x, y):
	x[1] = y

def complexNumber(a = 0, b = 0):
	return [a, b]

def modulus(x):
	from math import sqrt
	return sqrt(getReal(x)**2 + getImaginary(x)**2)

def readList(s):
	n = input('How many complex numbers do you wish to insert: ')
	if not n.isdigit():
		print('Not a valid number')
		return
	n = int(n)
	t = []
	for i in range(n):
		real = input('Real part of number ' + str(i+1) + ": ")
		try:
			real = float(real)
		except:
			print('Not a valid number')
			return
		if real == int(real):
			real = int(real)
		imag = input('Imaginary part of number ' + str(i+1) + ": ")
		try:
			imag = float(imag)
		except:
			print('Not a valid number')
			return
		if imag == int(imag):
			imag = int(imag)
		x = complexNumber()
		setReal(x, real)
		setImaginary(x, imag)
		t.append(x)
	s += t
	print('Succesfully added sequence\n')

def displayList(s):
	if(len(s) == 0):
		print('The list is empty')
		return
	print('The list has ' + str(len(s)) + ' complex numbers')
	for i in s:
		t = ''
		if getReal(i) != 0:
			t += str(getReal(i))
		if getImaginary(i) != 0:
			if t == '':
				if getImaginary(i) == 1:
					t += 'i'
				elif getImaginary(i) == -1:
					t += '-i'
				else:
					t += str(getImaginary(i))+'i'
			elif getImaginary(i) > 0:
				if getImaginary(i) == 1:
					t += '+i'
				else:
					t += '+' + str(getImaginary(i))+'i'
			else:
				if getImaginary(i) == -1:
					t += '-i'
				else:
					t += str(getImaginary(i))+'i'
		if t == '':
			t = 0
		print(t)
	print()

def propertyOne(s):
	'''
	Input: list s of complex numbers
	Effect: Prints on the console the longest sequence in the list with the property that all real parts are increasing
	'''
	t = [s[0]]
	ans = t
	for x in s[1:]:
		if getReal(x) > getReal(t[-1]):
			t.append(x)
		else:
			t = [x]
		if len(t) > len(ans):
			ans = t
	return ans

def propertyTwo(s):
	'''
	Input: list s of complex numbers
	Effect: Prints on the console the longest sequence in the list with the property that all numbers have modulus in the [0,10] range
	'''
	t = [s[0]]
	if modulus(s[0]) > 10:
		t = []
	ans = t
	for x in s[1:]:
		if modulus(x) <= 10:
			t.append(x)
		else:
			t = []
		if len(t) > len(ans):
			ans = t
	return ans

def printMenu():
	print('1. Read a list of complex numbers')
	print('2. Display all complex numbers in the list')
	print('3. Display the longest sequence with strictly increasing real part')
	print('4. Display the longest sequence with modulus of each number less or equal to 10')
	print('5. Exit the program\n')

def main():
	s = [complexNumber(2, 3), 
	 complexNumber(1, 4), 
	 complexNumber(11, 2), 
	 complexNumber(-2, -5),
	 complexNumber(2.2, 0),
	 complexNumber(10, 10), 
	 complexNumber(3.5, -2.7), 
	 complexNumber(), 
	 complexNumber(0, 1), 
	 complexNumber(2, 2)]
	while True:
		printMenu()
		choice = input('>')
		if choice == '1':
			readList(s)
		elif choice == '2':
			displayList(s)
		elif choice == '3':
			displayList(propertyOne(s))
		elif choice == '4':
			displayList(propertyTwo(s))
		elif choice == '5':
			return
		else:
			print('Invalid command')

main()
