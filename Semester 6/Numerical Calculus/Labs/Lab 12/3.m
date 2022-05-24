x0 = 1;
x1 = 2;

epsilon = 10 ^ -4;
N = 100;

func = @(x) x ^ 3 - x ^ 2 - 1;


function f = secantElement(func, prev, preprev)
  f = prev - func(prev) * (prev - preprev) / (func(prev) - func(preprev));
endfunction

function f = solveSecant(func, x0, x1, eps, N)
  preprev = x0;
  prev = x1;
  current = secantElement(func, prev, preprev)
  
  for i = 0 : N
    if abs(current - prev) > eps
      newCurrent = secantElement(func, current, prev);
      preprev = prev;
      prev = current;
      printf("%d --> ", i)
      current = newCurrent
    else
      f = current;
      return;
    endif
  endfor
  
  f = current;
endfunction

solveSecant(func, x0, x1, eps, N)