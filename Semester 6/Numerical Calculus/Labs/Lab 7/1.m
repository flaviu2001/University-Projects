x = [1 2 3 4 5 6 7];
temp = [13 15 20 14 15 13 10];
m = length(x);
a = (sum(m * x .* temp) - sum(x) * sum(temp)) / (m * sum(x.*x) - sum(x)^2)
b = (sum(x.^2) * sum(temp) - sum(x.*temp) * sum(x)) / (m * sum(x.^2) - sum(x)^2)
eight = a * 8 + b
values = a .* x + b;
sum((temp .- values) .^ 2)
t = 1 : 0.01 : 7;
ft = a .* t + b;
plot(x, temp, "*", t, ft);
input("");
