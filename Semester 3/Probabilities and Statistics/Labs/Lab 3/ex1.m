%Normal Distribution
mu=input('mu(in R)= ');
sigma=input('sigma(>0)= ');
alpha=input('alpha(in (0,1)= ');
beta=input('beta(in (0,1)= ');

%a) P(x<=0),P(x>=0)
pa1=normcdf(0,mu,sigma);
pa2=1-normcdf(0,mu,sigma);
%pa2=1-pa1;

%b) P(-1<=x<=1),P(x<=-1 or x>=1)
pb1=normcdf(1,mu,sigma)-normcdf(-1,mu,sigma);
pb2=1-pb1;

%c)
answ=norminv(alpha,mu,sigma);

%d)xbeta=? P(X>xbeta)=beta
ansd=norminv(1-beta,mu, sigma);

fprintf('Prob1. in part a) is %1.4f\n',pa1);
fprintf('Prob2. in part a) is %1.4f\n',pa2);
fprintf('Prob1. in part b) is %1.4f\n',pb1);
fprintf('Prob2. in part b) is %1.4f\n',pb2);
fprintf('Prob1. in part c) is %1.4f\n',answ);
fprintf('Prob1. in part d) is %1.4f\n',ansd);