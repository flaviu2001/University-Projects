a = [10 7 8 7 ; 7 5 6 5 ; 8 6 10 9 ; 7 5 9 10];
b = [32 ; 23 ; 33 ; 31];


## a)
x = linsolve(a, b)

printf("Conditionung number ----> %d\n", cond(a));

## b)

b2 = [32.1 ; 22.9 ; 33.1 ; 30.9 ];

x2 = linsolve(a, b2)

input_relative_error = norm(b - b2) / norm(b)
output_relative_error = norm(x - x2) / norm(x)
error = output_relative_error / input_relative_error

## c)
a2 = [10 7 8.1 7.2 ; 7.08 5.04 6 5 ; 8 5.98 9.89 9 ; 6.99 4.99 9 9.98]

x3 = linsolve(a2, b)

input_relative_error2 = norm(a - a2) / norm(a)
output_relative_error2 = norm(x - x3) / norm(x)

error2 = output_relative_error2 / input_relative_error2