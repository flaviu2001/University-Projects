function sol = neville(x, f, t)
	sol = [x ; f];
	m = length(x);
	for i = 2:m
		newLine = [zeros(1, i-1)];
		last = sol(i, :);
		for j = i:m
			determ = (x(j)-t)*last(j-1) + (t - x(j-i+1))*last(j);
			newLine = [newLine determ/(x(j) - x(j-i+1))];
		end
		sol = [sol ; newLine];
	end
	sol = sol(m+1, m);
end 
