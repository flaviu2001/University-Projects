'''
Created on Sep 29, 2016

@author: http://www.python-course.eu/passing_arguments.php
'''

'''
    Example for parameter passing. 
    Use the locals() and globals() functions to better 
    understand what goes on 
'''

def refDemo(x):
    print("2. x=", x, " id=", id(x))
    x = 42
    print("3. x=", x, " id=", id(x))
     
x = 10
print("1. x=", x, " id=", id(x))
 
refDemo(x)
print("4. x=", x, " id=", id(x))
