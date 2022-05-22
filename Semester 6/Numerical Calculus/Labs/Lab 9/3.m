format long;

function ans = f(x)
	ans = 100/(x^2) * sin(10/x);
end

function ans2 = simpson(a, b)
	ans2 = (b-a) / 6 * (f(a) + 4 * f((a+b)/2) + f(b));
end

function ans3 = adquad(a, b, eps)
	I1 = simpson(a, b);
	I2 = simpson(a, (a+b)/2) + simpson((a+b)/2, b);

	if abs(I1 - I2) < 15 * eps
		ans3 = I2;
		return;
	end

	ans3 = adquad(a, (a+b)/2, eps/2) + adquad((a+b)/2, b, eps/2);
end

function ans4 = repeatedSimpson(a, b, n)
	t = linspace(a, b, n+1);
	ans4 = 0;
	for i = 2 : n+1
		ans4 = ans4 + simpson(t(i-1), t(i));
	end
end

adquad(1, 3, 0.0001)
repeatedSimpson(1, 3, 51)
repeatedSimpson(1, 3, 101)