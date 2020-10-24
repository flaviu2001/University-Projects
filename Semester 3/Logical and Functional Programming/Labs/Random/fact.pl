fact1(0, 1) :-!.
fact1(N, F) :- N > 0,
	N1 is N-1,
	fact1(N1, F1),
	F is N * F1.

suma(_, 0, 0) :- !.
suma([], _, 0) :- !.
suma([A|L], N, S) :-
	N1 is N - 1,
	suma(L, N1, S1),
	S is A + S1.

power(_, 0, 1) :- !.
power(X, N, R) :-
	N < 0,
	!,
	N1 is -N,
	X1 is 1/X,
	power(X1, N1, R).
power(X, N, R) :-
	N1 is N-1,
	power(X, N1, R1),
	R is R1*X.
