p = input("p=");
n = input("n=");
v = 0 : n;
plot(v, binopdf(v, n, p), 'm', v, poisspdf(v, n*p), 'b')