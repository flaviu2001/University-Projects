'''
Created on Dec 20, 2016

@author: Arthur
'''

'''
    1. Lambda examples for sorting a list
'''
pairs = [('Joanna', 6), ('Anna', 10), ('Carl', 9), ('Betty', 8), ('Xavier', 5), ('Sophia', 7), ('Richard', 7)]

# Sort by name
pairs.sort(key=lambda pair:pair[0])
print(pairs)
 
# Sort by the integer
pairs.sort(key=lambda pair:pair[1])
print(pairs)

'''
    2. The square of a number, using a named function and a lambda
'''
def p(n):
    return n ** 2

g = lambda x : x ** 2

print(p(8))
print(g(8))

'''
    3. Filter a list using some criteria
'''
print('Student ''Anna''')
for p in filter(lambda p:p[0] == 'Anna', pairs):
    print('\t',p)

print('Where integer is >6')
for p in filter(lambda p:p[1] > 6, pairs):
    print('\t',p)