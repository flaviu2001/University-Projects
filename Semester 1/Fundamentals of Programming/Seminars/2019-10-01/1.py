def f(x, y):
	if(x == 10 or y == 10 or x+y == 10):
		return True
	return False
a = int(input('input a '))
b = int(input('input b '))
print(f(a, b))