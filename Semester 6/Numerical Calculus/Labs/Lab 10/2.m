n = 10 : 15;

function f = findCondMatrix(n)
  k = 1 : n;
  
  tk = 1 ./ k ;
  
  matrix = zeros(n, n);
  
  for i = 1 : n
    for j = 1 : n
      matrix(i, j) = tk(i) ^ (j - 1);
    endfor
  endfor
  
  f = matrix;
  
endfunction

for i = 10 : 15
  matrix = findCondMatrix(i);
  printf("Conditioning number for n = %d is %d\n", i, cond(matrix))
  endfor