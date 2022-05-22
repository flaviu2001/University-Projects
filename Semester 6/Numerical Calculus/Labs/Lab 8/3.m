format long;

n = 10;
t = linspace(0, 2*pi, n);
I = 0;
r = 110;
p = 75;
multiplier = 60*r/(r^2-p^2);
for i = 2:n
	a = t(i-1);
	b = t(i);
	p1 = (1 - (p/r)^2 * sin(a)) ^ (1/2);
	p2 = (1 - (p/r)^2 * sin(b)) ^ (1/2);
	I = I + (b-a) * (p1 + p2) / 2;
end
I*multiplier

n = 100;
t = linspace(0, 2*pi, n);
I = 0;
r = 110;
p = 75;
multiplier = 60*r/(r^2-p^2);
for i = 2:n
	a = t(i-1);
	b = t(i);
	p1 = (1 - (p/r)^2 * sin(a)) ^ (1/2);
	p2 = (1 - (p/r)^2 * sin(b)) ^ (1/2);
	I = I + (b-a) * (p1 + p2) / 2;
end
I*multiplier
