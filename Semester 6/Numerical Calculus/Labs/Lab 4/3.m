x = linspace(0, 6, 13);
int_points = exp(sin(x));
t = 0 : 0.01 : 6;
f = exp(sin(t));
sol = [];
for i = 1 : length(t)
	sol = [sol cutzu(x, int_points, t(i))];
end

subplot(3, 1, 1);
plot(x, int_points);
subplot(3, 1, 2);
plot(t, f);
subplot(3, 1, 3);
plot(t, sol);
input("");
