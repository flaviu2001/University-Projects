time = [0, 3, 5, 8, 13];
distance = [0, 225, 383, 623, 993]; 
speed = [75, 77, 80, 74, 72];
t = 10;
eps = 0.01;
i = spline(time, [75 distance 72]);
v1 = ppval(i, t);
v2 = ppval(i, t+eps);
v1
(v2-v1)/eps
