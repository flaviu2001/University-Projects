a = [1 1 1 1 ; 2 3 1 5 ; -1 1 -5 3 ; 3 1 7 -2];
b = [10 ; 31 ; -2 ; 18];

function f = solveGauss(a, b)
  [m, n] = size(a);
  
  for column = 1 : n - 1
    [value, q] = max(abs(a(column:n, column)));
    
    q = q + column - 1;
    
    if column != q
      a([column, q], :) = a([q, column], :);
      b([column, q]) = b([q, column]);
    endif
   
    for row = column + 1 : n
      value = a(row, column) / a(column, column);
      b(row) = b(row) - value * b(column);
      a(row, column : n) = a(row, column : n) - value * a(column, column : n);
    endfor

  endfor

  if a(n, n) == 0
      f = -1
      return
  endif
   
   result = zeros(size(b));
   
   for i = n : -1 : 1
     result(i) = (b(i) - a(i, i + 1 : n) * result(i + 1 : n)) / a(i,i);
   endfor
   
   f = result;
    
endfunction


my_solution  = solveGauss(a, b)
acc  = linsolve(a, b)