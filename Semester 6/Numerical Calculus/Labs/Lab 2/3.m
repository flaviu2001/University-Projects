t = -1 : 0.01 : 3;
n = 6;
prev_f = 1;
hold on
plot(t, prev_f);
for i = 1:n-1
	f = prev_f + t.^i / factorial(i);
	plot(t, f);
	prev_f = f;
end 
hold off
input("")