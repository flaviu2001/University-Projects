format long
x = [1, 2, 3, 4, 5];
y = [22, 23, 25, 30, 28];
cutzu(x, y, 2.5)
t = 1 : 0.01 : 5;
sol = [];
for i = 1 : length(t)
	sol = [sol cutzu(x, y, t(i))];
end
plot(x, y, "*", t, sol);
input("");
