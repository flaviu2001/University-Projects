format long
t = linspace(-5, 5, 15);
f = sin(2.*t);
fprime = 2.*cos(2.*t);
tt = -5 : 0.01 : 5;
sol = [];
for i = 1:length(tt)
	sol = [sol hermite(t, f, fprime, tt(i))];
end
fn = sin(2.*tt);
plot(tt, fn, tt, sol);
input("");