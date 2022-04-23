function sol = hermite(x, f, fprime, point)
	n = length(x);
	new_x = [];
	new_f = [];
	for i = 1:n
		new_x = [new_x x(i) x(i)];
		new_f = [new_f f(i) f(i)];
	end
	x = new_x;
	f = new_f;
	n = length(x);

	t = [x ; f];
	new_t = [];
	for j = 1:n-1
		if mod(j, 2) == 1
			new_t = [new_t fprime((j+1)/2)];
		else
			new_t = [new_t (t(2, j+1) - t(2, j)) / (t(1, j+1) - t(1, j))];
		end
	end
	t = [t ; new_t 0];
	for i = 3:n
		new_t = [];
		for j = 1:n-i+1
			new_t = [new_t (t(i, j+1) - t(i, j)) / (t(1, j+i-1) - t(1, j))];
		endfor
		new_t = [new_t zeros(1, i-1)];
		t = [t ; new_t];
		prev_t = new_t;
	endfor
	t = t';
	
	p = 1;
	sol = 0;

	for i = 1:n
		sol += p * t(1, i+1);
		p = p * (point-x(i));
	end
end
