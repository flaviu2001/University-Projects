subplot(2, 2, 1);
x = 0 : 0.01 : 1;
f = x;
plot(x, f);
title('Subplot 1: l1(x) = x');

subplot(2, 2, 2);
x = 0 : 0.01 : 1;
f = 3 * x.^2 / 2 - 1 / 2;
plot(x, f);
title('Subplot 2: l2(x) = (3/2) * x^2 - (1/2)');

subplot(2, 2, 3);
x = 0 : 0.01 : 1;
f = 5/2 * x.^3 - 3/2 * x.^2;
plot(x, f);
title('Subplot 3: (5/2) * x^3 - (3/2) * x');

subplot(2, 2, 4);
x = 0 : 0.01 : 1;
f = 35/8 * x.^4 - 15/4 * x.^2 + 3/8;
plot(x, f);
title('Subplot 4: (35/8) * x^4 - (15/4) * x^2 + (3/8)');

input("")