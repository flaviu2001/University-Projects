a = 1;
b = 2;

epsilon = 10 ^ -4;
N = 100;

f = @(x) (x - 2) ^ 2 - log(x);

function f = solveBisection(func, a, b, eps, N)
  c = 0;
  for i = 0 : N
    c = (a + b) / 2;
    if (abs(func(c)) >= eps)
      
      if (func(a) * func(c) < 0)
        b = c;
       else
        a = c;
      endif
      
   else
      f = c;
      return;
   endif
  endfor
  
  f = c;
  endfunction

 
function f = solveFalsePosition(func, a, b, eps, N)
  c = 0;
  for i = 0 : N
    c = (func(b) * a - func(a) * b) / (func(b) - func(a));
    if abs(func(c)) >= eps
      
      if func(a) * func(c) < 0
        b = c;
       else
        a = c;
      endif
      
    else
      f = c;
      return;
    endif
  endfor
  
  f = c;
 
  endfunction

solveBisection(f, a, b, epsilon, N)
solveFalsePosition(f, a, b, epsilon, N)