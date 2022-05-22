format long;
n = 5;
t = linspace(0, 0.5, n);
I = 0;
multiplier = 2/sqrt(pi);
for i = 2:n
	a = t(i-1);
	b = t(i);
	I = I + (b-a) * ( exp(-(a^2)) + exp(-(b^2)) + 4 * exp(-(((a+b)/2)^2))) / 6;
end
p1 = I*multiplier
 
n = 11;
t = linspace(0, 0.5, n);
I = 0;
multiplier = 2/sqrt(pi);
for i = 2:n
	a = t(i-1);
	b = t(i);
	I = I + (b-a) * ( exp(-(a^2)) + exp(-(b^2)) + 4 * exp(-(((a+b)/2)^2))) / 6;
end
p2 = I*multiplier

ans = 0.520499876;
abs(ans-p1)
abs(ans-p2)