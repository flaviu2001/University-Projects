format long;

function solve(n)
t = linspace(1, 1.5, n);
I = 0;
for i = 2:n
	a = t(i-1);
	b = t(i);
	I = I + (b-a) * exp(-(((a+b)/2)^2));
end
I
end

solve(2);
solve(150);
solve(500);
t = 0.5 : 0.01 : 2;
f = exp(-(t.^2));
plot(t, f, [1, 1, 1.5, 1.5, 1], [0 exp((-1 + -(1.5^2))/2) exp((-1 + -(1.5^2))/2) 0 0]);
input("");