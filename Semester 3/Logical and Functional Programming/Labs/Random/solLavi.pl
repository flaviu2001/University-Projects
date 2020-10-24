count_elem([], _, O) :-
	O is 0.
count_elem([H|T], E, O) :- 
	E==H,
	!,
	count_elem(T, H, O1),
	O is O1 + 1.
count_elem([H|T], E, O) :-
	count_elem(T, E, O).

rem_rep([], []) :-
	!.
rem_rep([H|T], [H|R1]) :-
	count_elem([H|T], H, O),
	O == 1, 
	rem_rep(T,R1),
	!.
rem_rep([H|T],R) :-
	rem_rep(T,R).