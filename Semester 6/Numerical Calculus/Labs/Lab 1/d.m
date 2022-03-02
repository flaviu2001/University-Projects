sa = 3;
sb = 3;

x = 0 : 0.01 : 1;
f = exp(10 * x .* (x-1)) .* sin(12 * pi * x);
subplot(sa, sb, 1);
plot(x, f)
f = 3 * exp(5 * x .* x - 1) .* cos(12 * pi * x);
subplot(sa, sb, 2);
plot(x, f)
t = 0 : 0.01 : 10*pi;
a = 1;
b = 2;
x = (a + b) * cos(t) .- b * cos ((a/b+1).*t);
y = (a + b) * sin(t) .- b * sin((a/b+1).*t);
subplot(sa, sb, 3);
plot(x, y)
x = 0 : 0.01 : 2*pi;
f1 = cos(x);
f2 = sin(x);
f3 = cos(2*x);
subplot(sa, sb, 4);
plot(x, f1, x, f2, x, f3)
x1 = -1 : 0.01 : 0;
x2 = 0 : 0.01 : 1;
f1 = x1.^3 .+ sqrt(1 - x1);
f2 = x2.^3 .- sqrt(1 - x2);
subplot(sa, sb, 5);
plot(x1, f1, x2, f2)
subplot(sa, sb, 6);
x1 = 0 : 2 : 50;
x2 = 1 : 2 : 49;
f1 = x1/2;
f2 = 3 * x2 + 1;
plot(x1, f1, "*", x2, f2, "*")
k = 5;
g = 1 + 1/2;
G = [g];
for c = 1:k
	g = 1 + 1/g;
	G = [G g];
end
G
subplot(sa, sb, 7);
plot(G, "*")
subplot(sa, sb, 8);
x = -2 : 0.01 : 2;
y = -4 : 0.01 : 4;
[x, y] = meshgrid(x, y);
g = exp(-((x - 1/2).^2 .+ (y - 1/2).^2));
mesh(x, y, g)
input("");