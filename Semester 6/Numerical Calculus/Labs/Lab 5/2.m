format long
ans = hermite([1, 2], [0, 0.69], [1, 0.5], 1.5) 
abs(log(1.5) - ans)