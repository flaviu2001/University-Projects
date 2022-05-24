x0 = 1;
epsilon = 10 ^ -4;
N = 6;

func = @(E) E - 0.8 * sin(E) - 2 * pi / 10;
funcD = @(E) 1 - 0.8 * cos(E);

function f = newtonElement(func, funcD, prev)
  f = prev - func(prev) / funcD(prev);
endfunction

function f = solveNewton(func, funcD, x0, N)
  prev = x0;
  current = newtonElement(func, funcD, prev);
  
  for i = 1 : N
     newCurrent = newtonElement(func, funcD, current);
     prev = current;
     printf("%d --> ", i)
     current = newCurrent
  endfor
  
  f = current;
endfunction


solveNewton(func, funcD, x0, N)