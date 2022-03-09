x = linspace(0, 10, 21);
t = 0 : 0.01 : 10;
true_f = (1 + cos(pi*t)) ./ (1 + t);
bary_f = (1 + cos(pi*x)) ./ (1 + x);
sol = [];
for i = 1 : length(t)
	sol = [sol mybary(x, bary_f, t(i))];
end
subplot(1, 2, 1);
plot(t, true_f);
ylim([0, 2]);
title("true");
subplot(1, 2, 2);
plot(t, sol);
ylim([0, 2]);
title("lagrange");
input("");