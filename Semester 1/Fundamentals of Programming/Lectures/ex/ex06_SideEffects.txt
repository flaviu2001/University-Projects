'''
Created on Sep 29, 2016

@author: http://www.python-course.eu/passing_arguments.php
'''

'''
    Example for function side effects. Run the code below calling each of
    the functions in turn. Examine the result
'''

def noSideEffect(lst):
    print(lst)
    lst = [0, 1, 2, 3]
    print(lst)

def sideEffect(lst):
    print(lst)
    lst += [0, 1, 2, 3]
    print(lst)

fib = [0, 1, 1, 2, 3, 5, 8]
noSideEffect(fib)
# sideEffect(fib)
print(fib)