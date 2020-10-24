def fibo(n):
	'''
	This function takes an integer parameter n and
	returns the first fibonacci number larger than n
	'''
	a = 1
	b = 1
	if n < 1:
		return 1
	while b <= n:
		c = a+b
		a = b
		b = c
	return b

n = int(input('input the value of n '))
print('The answer is ' + str(fibo(n)))