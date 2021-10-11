pr = [22.4 21.7 24.5 23.4 21.6 23.3 22.4 21.6 24.8 20.0];
rg = [17.7 14.8 19.6 19.6 12.1 14.8 15.4 12.6 14.0 12.2];

alpha = input("alpha = ");

mpr = mean(pr);
mrg = mean(rg);
n1 = columns(pr);
n2 = columns(rg);
s1 = cov(pr);
s2 = cov(rg);
sp = sqrt(((n1-1) * s1 + (n2-1) * s2)/(n1+n2-2));

u1 = mpr-mrg-tinv(1-alpha/2, n1+n2-2)*sp*sqrt(1/n1+1/n2)
u2 = mpr-mrg+tinv(1-alpha/2, n1+n2-2)*sp*sqrt(1/n1+1/n2)

c = s1/n1/(s1/n1 + s2/n2);
n = 1/(c*c/(n1-1) + (1-c)*(1-c)/(n2-1));
u12 = mpr-mrg-tinv(1-alpha/2, n)*sqrt(s1/n1+s2/n2)
u22 = mpr-mrg+tinv(1-alpha/2, n)*sqrt(s1/n1+s2/n2)

a1 = 1/finv(1-alpha/2, n1-1, n2-1) * s1/s2
a2 = 1/finv(alpha/2, n1-1, n2-1) * s1/s2