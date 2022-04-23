t = [0 10 20 30 40 60 80 100];
p = [0.0061 0.0123 0.0234 0.0424 0.0738 0.1992 0.4736 1.0133];
f2 = polyfit(t, p, 2)
f3 = polyfit(t, p, 3)
p2 = polyval(f2, 45)
p3 = polyval(f3, 45)
true_val = 0.095848;
abs(p2-true_val)
abs(p3-true_val)
x = 0 : 0.01 : 100;
pt1 = polyval(f2, x);
pt2 = polyval(f3, x);
plot(x, pt1, x, pt2, t, p, "*")
legend
input("")