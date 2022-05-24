A = [3 -1 0 0 0 0 ; -1 3 -1 0 0 0 ; 0 -1 3 -1 0 0 ; 0 0 -1 3 -1 0 ; 0 0 0 -1 3 -1 ; 0 0 0 0 -1 3];
b = [2 ; 1 ; 1 ; 1 ; 1 ; 2];


function g = calculateElement(A, b, prev)
  element = zeros(length(b), 1);
  
  for i = 1 :  length(b)
    suma = 0;
    for j = 1 : length(b)
      if i ~= j
        suma = suma + A(i, j) * prev(j);
      endif
    endfor
    element(i) = (b(i) - suma) / A(i, i);
  endfor
  
  g = element;
  
endfunction

function g = calculateElementGauss(A, b, prev)
  element = zeros(length(b), 1);
  
  for i = 1 :  length(b)
    suma1 = 0;
    for j = 1 : i - 1
        suma1 = suma1 + A(i, j) * element(j);
    endfor
    for j = i+1 : length(b)
        suma1 = suma1 + A(i, j) * prev(j);
    endfor
    element(i) = (b(i) - suma1) / A(i, i);
  endfor
  
  g = element;
  
endfunction

function g = calculateElementSOR(A, b, prev, omega)
  element = zeros(length(b), 1);
  
  for i = 1 :  length(b)
    suma = 0;
    for j = 1 : i - 1
        suma = suma + A(i, j) * element(j);
    endfor
    for j = i+1 : length(b)
        suma = suma + A(i, j) * prev(j);
    endfor
    element(i) = omega * (b(i) - suma) / A(i, i) + (1 - omega) * prev(i);
  endfor
  
  g = element;
  
  endfunction

function [f, it] = jacobi(A, b, epsilon)
  iterations = 1;
  prev = zeros(length(b), 1);
  current = calculateElement(A, b, prev);
  
  while norm(current - prev) > epsilon
    new_current = calculateElement(A, b, current);
    prev = current;
    current = new_current;
    iterations = iterations + 1;
  endwhile
  
  f = current;
  it = iterations;
endfunction


epsilon = 10 ^ -3;
[r1, it1] = jacobi(A, b, epsilon)

function [f, it] = gauss(A, b, epsilon)
  iterations = 1;
  prev = zeros(length(b), 1);
  current = calculateElementGauss(A, b, prev);
  
  while norm(current - prev) > epsilon
    new_current = calculateElementGauss(A, b, current);
    prev = current;
    current = new_current;
    iterations = iterations + 1;
  endwhile
  
  f = current;
  it = iterations;
endfunction

[r2, it2] = gauss(A, b, epsilon)


function [f, it] = sor(A, b, epsilon, omega)
  iterations = 1;
  prev = zeros(length(b), 1);
  current = calculateElementSOR(A, b, prev, omega);
  
  while norm(current - prev) > epsilon
    new_current = calculateElementSOR(A, b, current, omega);
    prev = current;
    current = new_current;
    iterations = iterations + 1;
  endwhile
  
  f = current;
  it = iterations;
endfunction

[r3, it3] = sor(A, b, epsilon, 1.5)
