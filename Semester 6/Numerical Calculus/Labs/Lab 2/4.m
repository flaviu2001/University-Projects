h = 0.25;
x = [];
n = 6;
for i = 0 : n
	x = [x 1 + i * h];
endfor
x
f = sqrt(5 * x.^2 + 1);
f
t = [f]
prev_t = f;
for i = 1:n
	new_t = [];
	for j = 1:n-i+1
		new_t = [new_t prev_t(j+1) - prev_t(j)];
	endfor
	new_t = [new_t zeros(1, i)];
	t = [t ; new_t];
	prev_t = new_t;
endfor
t'