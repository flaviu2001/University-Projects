function retval = mybary(x, f, t)
	nrNodes = length(x);

	for i = 1 : nrNodes
		if x(i) == t
			retval = f(i);
			return;
		end
	end

	bigA = [];
	for i = 1 : nrNodes
		currentA = 1;
		for j = 1 : nrNodes
			if i != j
				currentA = currentA * (x(i) - x(j));
			end
		end
		bigA = [bigA 1/currentA];
	end

	numerator = 0;
	denominator = 0;
	for i = 1 : nrNodes
		numerator = numerator + bigA(i)/(t-x(i)) * f(i);
		denominator = denominator + bigA(i)/(t-x(i));
	end
	retval = numerator / denominator;
end