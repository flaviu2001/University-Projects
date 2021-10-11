p = input("p=");
for i = 1 : 10
  n = i*5;
  v = 0 : n;
  plot(v, binopdf(v, n, p)
  pause(1);
endfor
