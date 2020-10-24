def sleepIn(weekday, vacation):
	return vacation or not weekday
def tobool(x):
	return x == 'yes'
print(sleepIn(tobool(input()), tobool(input())))