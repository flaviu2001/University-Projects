[x, y] = ginput(10);
x = x * 3;
y = y * 5;
t = 0:0.01:3;
i = polyfit(x, y, 2);
iv = polyval(i, t);
plot(t, iv, x, y, "*");
input(""); 
