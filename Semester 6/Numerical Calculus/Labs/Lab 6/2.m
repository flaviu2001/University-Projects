[x, y] = ginput(5);
t = 0:0.01:1;
i = spline(x, y, t);
plot(t, i, x, y, "*");
input("");