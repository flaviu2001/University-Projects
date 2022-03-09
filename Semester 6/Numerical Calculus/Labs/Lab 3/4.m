1;

function retval = solve(n)
	x = linspace(-5, 5, n);
	bary_f = 1 ./ (1 + x.^2);
	y = linspace(-5, 5, 101);
	
	error_lhs = 1 ./ (1 + y.^2);
	error_rhs = [];
	for i = 1 : length(y)
		error_rhs = [error_rhs mybary(x, bary_f, y(i))];
	end

	max(abs(error_lhs-error_rhs))

	plot(y, error_lhs, y, error_rhs);
end

subplot(2, 2, 1)
solve(2)
subplot(2, 2, 2)
solve(4)
subplot(2, 2, 3)
solve(6)
subplot(2, 2, 4)
solve(8)
input("");