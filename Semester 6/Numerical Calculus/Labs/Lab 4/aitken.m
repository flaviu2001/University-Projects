function sol = aitken(x, f, t, eps)
	sol = [x ; f];
	sortrows(sol')';
	x = sol(1, :);
	f = sol(2, :);
	m = length(x);
	for i = 2:m
		newLine = [zeros(1, i-1)];
		last = sol(i, :);
		for j = i:m
			determ = last(i-1) * (x(j) - t) - last(j) * (x(i-1)-t);
			newLine = [newLine determ/(x(j) - x(i-1))];
		end
		sol = [sol ; newLine];
	end
	sol = sol(m+1, m);
end 
