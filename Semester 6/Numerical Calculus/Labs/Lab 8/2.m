format long;
n1 = 2;
n2 = 2;
t2 = linspace(1, 1.5, n2);
t1 = linspace(1.4, 2, n1);
I = 0;
for i1 = 2:n1
	for i2 = 2:n2
		a = t1(i1-1);
		b = t1(i1);
		c = t2(i2-1);
		d = t2(i2);
		sum = 0;
		sum = sum + log(a + 2 * c);
		sum = sum + log(a + 2 * d);
		sum = sum + log(b + 2 * c);
		sum = sum + log(b + 2 * d);
		sum = sum + 2 * log((a+b)/2 + 2 * c);
		sum = sum + 2 * log((a+b)/2 + 2 * d);
		sum = sum + 2 * log(a + c + d);
		sum = sum + 2 * log(b + c + d);
		sum = sum + 4 * log((a+b)/2 + c + d);
		I = I + sum * (b-a) * (d-c) / 16;
	end
end
I