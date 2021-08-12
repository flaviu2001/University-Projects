p=input('p=');
sims=input('simulations=');
n=input('trials=');
x=zeros(n+1,1);
b=zeros(n+1,1);
for t=1:sims
  for i=1:n
    k=0;
    u=rand(1,n);
    for j=1:n
      if (u(j) < p)
        k += 1;
      endif
    endfor
    x(k+1) += 1;
  endfor
endfor
for i=0:n
  x(i+1) /= sims*n;
endfor
for i=0:n
  b(i+1)=binopdf(i,n,p);
endfor
subplot(2,1,1)
plot(x)
subplot(2,1,2)
plot(b)