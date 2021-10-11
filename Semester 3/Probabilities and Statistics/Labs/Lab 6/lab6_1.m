alpha = input('alpha=');
x = [7 7 4 5 9 9 4 12 8 1 8 7 3 13 2 1 17 7 12 5 6 2 1 13 14 10 2 4 9 11 3 5 12 6 10 7];
n = columns(x);
m0 = input('m0=');
% The null hypothesis H0: mu = m0
% the alt. hypothesis H1: mu < m0. This is a left-tailed test for mu.
sigma = 5;
[H, P, CI, ZVAL] = ztest(x, m0, sigma, 'alpha', alpha, 'tail', 'left');
RR = [-inf, norminv(alpha)]; % rejection region for left-tailed test
fprintf('The confidence interval for mu is (%4.4f,%4.4f)\n', CI)
fprintf('The rejection region is (%4.4f, %4.4f)\n', RR)
fprintf('The value of the test statistic z is %4.4f\n', ZVAL)
fprintf('The P-value of the test is %4.4f\n', P)
if H == 1 % result of the test, h = 0, if H0 is NOT rejected, h = 1, if H0 IS rejected
    fprintf('\nThe null hypothesis is rejected.\n') 
    fprintf('The data suggests that the standard IS NOT met.\n')
    % there exists a computer whose average is < 9
else
    fprintf('\nThe null hypothesis is not rejected.\n')
    fprintf('The data suggests that the standard IS met.\n')
end

m0 = input('m0=');
[H, P, CI, ZVAL] = ttest(x, m0, 'alpha', alpha, 'tail', 'right');
RR = [tinv(1-alpha, n-1), Inf];
fprintf('\nThe confidence interval for mu is (%4.4f,%4.4f)\n', CI)
fprintf('the rejection region is (%4.4f,%4.4f)\n', RR)
fprintf('the value of the test statistic t is %4.4f\n', ZVAL.tstat)
fprintf('the P-value of the test is %4.4f\n', P)
if H==1
    fprintf('The null hypothesis is rejected.\n') 
    fprintf('The data suggests that the average exceeds 5.5.\n')
    % the number of stored files exceeds on average 5.5
else
    fprintf('The null hypothesis is not rejected.\n')
    fprintf('The data suggests that the average DOES NOT exceed 5.5.\n')
end