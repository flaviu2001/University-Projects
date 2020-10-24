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

def count(n):
	'''
	This function takes an integer parameter n and
	returns how many prime factors have all the numbers 
	between 1 and n in total. 1 is considered to have 1 factor and
	it is 1.
	'''
	if n < 1:
		return 0
	cnt = 1+n//2
	for i in range(3, n+1, 2):
		if isprime(i):
			cnt += n//i
	return cnt

def nth(k):
	'''
	This function takes an integer parameter n and
	returns the nth number in the sequence 1,2,3,4,5,2,3,..
	which is built by replacing each element in 1,2,3,4,5,6..
	with its prime factors
	'''
	if k < 1:
		return -1
	st = 0
	dr = k
	mid = 0
	ans = 0
	while st <= dr:
		mid = (st+dr)//2
		if count(mid) < k:
			ans = mid
			st = mid+1
		else:
			dr = mid-1
	k -= count(ans)
	ans += 1
	i = 2
	while i*i <= ans:
		if ans%i == 0:
			k -= 1
			if k == 0:
				return i
			while ans%i == 0:
				ans = ans//i
		i += 1
	return ans

def printans(n):
	'''
	This function takes an integer parameter n and
	interprets the answer of the function 'nth' in a
	natural language(english)
	'''
	if n == -1:
		print('There is no such number, the index is invalid')
	else:
		print('The nth element is ' + str(nth(n)))

n = int(input('Input the value of n '))
printans(nth(n))