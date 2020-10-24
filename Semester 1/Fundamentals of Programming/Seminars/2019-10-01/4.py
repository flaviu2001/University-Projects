def calc(x):
	cnt = 0
	for i in x:
		if(i%2 == 0):
			cnt += 1
	return cnt
print(calc([2,3,4]))