p = input('p=');
n = input('n=');
v = rand(1,n);
v(v>=p) = 0;
v(v!=0) = 1;
unique(v); %?
[x, m] = hist(v, 2);
for i=1:2
  printf("Value %d has %d probability\n", i-1, x(i)/n)
endfor
