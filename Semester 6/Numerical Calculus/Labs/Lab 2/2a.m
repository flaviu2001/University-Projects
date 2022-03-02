t = -1 : 0.01 : 1;
T1 = cos(acos(t));
T2 = cos(2*acos(t));
T3 = cos(3*acos(t));
%subplot(1, 3, 1)
plot(t, T1, t, T2, t, T3);
input("")