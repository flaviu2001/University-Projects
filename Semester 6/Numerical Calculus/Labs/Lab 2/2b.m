n = 8
t = 0 : 0.01 : 1;
T0 = 1;
T1 = t;
hold on
plot(t, T0, t, T1);
for i = 2:n 
	T2 = 2 * t .* T1 .- T0;
	plot(t, T2);
	T0 = T1;
	T1 = T2;
end
hold off
input("")