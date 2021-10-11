x = [7 7 4 5 9 9 4 12 8 1 8 7 3 13 2 1 17 7 12 5 6 2 1 13 14 10 2 4 9 11 3 5 12 6 10 7];
n = columns(x);
alpha = input ('alpha=');
stv = 5;
xbar = mean(x);
u1 = xbar - stv/sqrt(n) * norminv(1-alpha/2)
u2 = xbar - stv/sqrt(n) * norminv(alpha/2)

stv2 = sqrt(cov(x));

u1 = xbar - stv2/sqrt(n) * tinv(1-alpha/2, n-1)
u2 = xbar - stv2/sqrt(n) * tinv(alpha/2, n-1)

o1 = (n-1)*stv2*stv2/chi2inv(1-alpha/2, n-1)
o2 = (n-1)*stv2*stv2/chi2inv(alpha/2, n-1)

s1 = sqrt(o1)
s2 = sqrt(o2)