x = [0, pi/2, pi, 3*pi/2, 2*pi];
y = sin(x);
t = 0 : 0.01 : 2*pi;
given_x = pi/4;
sin(pi/4)
spline(x, y, given_x)
ppval(spline(x, [1 y 1]), given_x)

i1 = spline(x, y, t);
i2 = ppval(spline(x, [1 y 1]), t);
f = sin(t);
plot(t, f, t, i1, t, i2, pi/4, sin(pi/4), "*");
legend;
input("");
