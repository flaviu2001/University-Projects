format long;

a = 0;
b = 1;
eps = 0.0001;

function ans = f(x)
	ans = 2./(1.+x.^2);
end

q = [(b-a)/2 * (f(a) + f(b))];
k = 0;
while true
	k = k + 1;
	sum = 0;
	for j = 1 : 2^(k-1)
		sum = sum + f(a + (2 * j - 1) / (2 ^ k) * (b-a));
	end
	new_q = q(end) / 2 + (b-a)/(2^k) * sum;
	if abs(q(end) - new_q) < eps
		q = [q new_q];
		break
	end
	q = [q new_q];
end

k
q(end)

for i = 2 : k+1
	new_q = [zeros(1, i-1)];
	for j = i : k+1
		t = (4 ^ (-j + 1) * q(i-1, j-1) - q(i-1, j)) / (4 ^ (-j+1) - 1);
		new_q = [new_q t];
	end
	q = [q ; new_q];
end

q(end, end)