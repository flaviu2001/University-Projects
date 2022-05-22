format long;

n = 11;
t = linspace(1, 2, n);
I = 0;
for i = 2:n
	a = t(i-1);
	b = t(i);
	p1 = a * log(a);
	p2 = b * log(b);
	I = I + (b-a) * (p1 + p2) / 2;
end
I
