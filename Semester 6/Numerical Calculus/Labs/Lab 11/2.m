A = [3 1 1 ; -2 4 0 ; -1 2 -6];
b = [12; 2; -5];

function f = calculateElementJacobi(D, L, U, b, prev) 
  f = -inv(D) * (L + U) * prev + inv(D) * b;
  endfunction

function [f, it] = jacobi(A, b, epsilon)
  D = diag(diag(A));
  L = tril(A, -1);
  U = triu(A, 1);
  
  iterations = 1;
  prev = zeros(length(b), 1);
  current = calculateElementJacobi(D, L, U, b, prev);
  
  while norm(current - prev) > epsilon
    new_current = calculateElementJacobi(D, L, U,  b, current);
    prev = current;
    current = new_current;
    iterations = iterations + 1;
  endwhile
  
  f = current;
  it = iterations;
endfunction

epsilon = 10 ^ -5;
[r1, it1] = jacobi(A, b, epsilon)

function f = calculateElementGauss(D, L, U, b, prev) 
  f = -inv(D+L) * U * prev + inv(D + L) * b;
  endfunction

function [f, it] = gauss(A, b, epsilon)
  D = diag(diag(A));
  L = tril(A, -1);
  U = triu(A, 1);
  
  iterations = 1;
  prev = zeros(length(b), 1);
  current = calculateElementGauss(D, L, U, b, prev);
  
  while norm(current - prev) > epsilon
    new_current = calculateElementGauss(D, L, U,  b, current);
    prev = current;
    current = new_current;
    iterations = iterations + 1;
  endwhile
  
  f = current;
  it = iterations;
endfunction

[r2, it2] = gauss(A, b, epsilon)

function f = calculateElementSOR(D, L, U, b, prev, omega) 
  f = inv(D+ omega * L) * ((1 - omega) * D - omega * U ) * prev + inv(D+ omega * L) * omega * b;
  endfunction

function [f, it] = SOR(A, b, epsilon, omega)
  D = diag(diag(A));
  L = tril(A, -1);
  U = triu(A, 1);
  
  iterations = 1;
  prev = zeros(length(b), 1);
  current = calculateElementSOR(D, L, U, b, prev, omega);
  
  while norm(current - prev) > epsilon
    new_current = calculateElementSOR(D, L, U,  b, current, omega);
    prev = current;
    current = new_current;
    iterations = iterations + 1;
  endwhile
  
  f = current;
  it = iterations;
endfunction

[r3, it3] = SOR(A, b, epsilon, 0.5)

