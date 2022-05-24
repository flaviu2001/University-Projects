x0 = pi / 4
epsilon = 10 ^ -4
N = 100

func = @(x) x - cos(x);
funcD = @(x) 1 + sin(x);

function f = newtonElement(func, funcD, prev)
  f = prev - func(prev) / funcD(prev);
endfunction

function f = solveNewton(func, funcD, x0, eps, N)
  prev = x0;
  current = newtonElement(func, funcD, prev);
  
  for i = 0 : N
    if abs(current - prev) > eps
      newCurrent = newtonElement(func, funcD, current);
      prev = current;
      current = newCurrent;
    else
      f = current;
      return;
    endif
  endfor
  
  f = current;
endfunction

res = solveNewton(func, funcD, x0, epsilon, N)