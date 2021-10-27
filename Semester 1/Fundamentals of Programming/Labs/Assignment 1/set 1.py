def isprime(n):
	'''
	This function takes an integer parameter n, 
	checks whether it is prime and returns a boolean 
	value of True if the number is prime, False otherwise
	'''
	if n < 2:
		return False
	i = 2
	while i*i <= n:
		if n%i == 0:
			return False
		i += 1
	return True
def goldbach(n):
	'''
	This function takes an integer parameter n and
	returns a list of 2 integers where the integers
	are the two prime values which add up to n. If there is
	no such pair the function returns the object [0,0]
	'''
	if n < 2:
		return []
	if n%2 == 1:
		if isprime(n-2):
			return [[2, n-2]]
		return []
	l = []
	for i in range(3, n//2+1, 2):
		if isprime(i) and isprime(n-i):
			l.append([i, n-i])
	return l
def printoutput(l):
	'''
	This function takes a list of two integers and prints
	that no prime numbers were found if the list has the first
	element 0, otherwise the two values
	'''
	if len(l) == 0:
		print('No prime numbers were found')
	else: 
		print('There are ' + str(len(l)) + ' pairs of prime numbers\n')
		for i in range(len(l)):
			print('The ' + str(i+1) +  '-th pair is ' + str(l[i][0]) + ' ' + str(l[i][1]))

n = int(input('Input the value of n '))
printoutput(goldbach(n))