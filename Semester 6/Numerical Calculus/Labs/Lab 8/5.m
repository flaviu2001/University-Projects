format long;
n = 10;
t = linspace(0, pi, n);
I = 0;
for i = 2:n
	a = t(i-1);
	b = t(i);
	I = I + (b-a) * ( 1/(4 + sin(20*a)) + 1/(4 + sin(20*b)) + 4 / (4 + sin(20*(a+b)/2))) / 6;
end
I
 
n = 30;
t = linspace(0, pi, n);
I = 0;
for i = 2:n
	a = t(i-1);
	b = t(i);
	I = I + (b-a) * ( 1/(4 + sin(20*a)) + 1/(4 + sin(20*b)) + 4 / (4 + sin(20*(a+b)/2))) / 6;
end
I