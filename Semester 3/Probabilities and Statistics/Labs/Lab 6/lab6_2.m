alpha = input('alpha=');

pr = [22.4 21.7 24.5 23.4 21.6 23.3 22.4 21.6 24.8 20.0];
rg = [17.7 14.8 19.6 19.6 12.1 14.8 15.4 12.6 14.0 12.2];

n1 = columns(pr);
n2 = columns(rg); %lengths of each array

m1 = mean(pr);
m2 = mean(rg); %means of each set

v1 = var(pr);
v2 = var(rg); %variances

f1 = finv(alpha/2, n1-1, n2-1);
f2 = finv(1-alpha/2, n1-1, n2-1); %quantiles for the rejection region of the two tailed test

[H, P, CI, ZVAL] = vartest2(pr, rg, "alpha", alpha);

if H==0
    fprintf('The null hypothesis is not rejected.\n')
    fprintf('The variances seem to be equal.\n')
    fprintf('The rejection region for F is (%6.4f, %6.4f) U (%6.4f, %6.4f)\n', -inf, f1, f2, inf)
    fprintf('The value of the test statistic F is %6.4f\n', ZVAL.fstat)
    fprintf('The P-value for the variances test is %6.4f\n', P)

    n = n1 + n2 - 2;
    t2 = tinv(1 - alpha, n); % quantile for right-tailed test (for rejection region)
    [H2, P2, CI2, ZVAL2] = ttest2(pr, rg, "alpha", alpha, "tail", "right");
    if H2==1
        fprintf('The null hypothesis is rejected.\n') 
        fprintf('Gas mileage IS higher with premium gasoline\n')
    else
        fprintf('The null hypothesis is not rejected.\n')
        fprintf('Gas mileage IS NOT higher with premium gasoline\n')
    end
    fprintf('the rejection region for T is (%6.4f,%6.4f)\n', t2, inf)
    fprintf('the value of the test statistic T is %6.4f\n', ZVAL2.tstat)
    fprintf('the P-value of the test for diff. of means is %e\n', P2)
else
    fprintf('The null hypothesis is rejected.\n')
    fprintf('The variances seem to be different.\n')
    fprintf('The rejection region for F is (%6.4f,%6.4f)U(%6.4f,%6.4f)\n', -inf, f1, f2, inf)
    fprintf('The value of the test statistic F is %6.4f\n', ZVAL.fstat)
    fprintf('The P-value for the variances test is %6.4f\n', P)

    c = (v1/n1)/(v1/n1 + v2/n2);
    n = c^2/(n1 - 1) + (1 - c)^2/(n2 - 1);
    n = 1/n;
    t2 = tinv(1 - alpha, n); % quantile for right-tailed test (for rejection region)
    [H2, P2, CI2, ZVAL2] = ttest2(pr, rg, 'alpha', alpha, 'tail', 'right'); % option "unequal" if variances are not equal
    if H2==1
        fprintf('The null hypothesis is rejected.\n') 
        fprintf('Gas mileage IS higher with premium gasoline\n')
    else
        fprintf('The null hypothesis is not rejected.\n')
        fprintf('Gas mileage IS NOT higher with premium gasoline\n')
    end    
    fprintf('The rejection region for T is (%6.4f,%6.4f)\n', t2, inf)
    fprintf('the value of the test statistic T is %6.4f\n', ZVAL2.tstat)
    fprintf('the P-value of the test for diff. of means is %e\n', P2)
end