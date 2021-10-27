n = input("Input number of values ");
p = input('"Input probability "');

v = 0 : 1 : n;
y = binopdf(v, n, p);
subplot(4,1,1)
plot(v, y)

w = 0 : 0.001 : n;
z = binocdf(w, n, p);
subplot(4,1,2)
plot(w, z);

n = 3;
p = 0.5;
v = 0 : 1 : n;
y = binopdf(v, n, p);
subplot(4,1,3)
plot(v, y)
w = 0 : 0.001 : n;
z = binocdf(w, n, p);
subplot(4,1,4)
plot(w, z)

N = 3;
X = 0;
for i = 1 : N
   toss = rand;
   if toss > 0.5
     X = X + 1;
    endif
endfor
X