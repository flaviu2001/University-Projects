def stringSplosion(s):
	x = ''
	for i in range(len(s)):
		x += s[0:i+1]
	return x
print(stringSplosion('pascal'))