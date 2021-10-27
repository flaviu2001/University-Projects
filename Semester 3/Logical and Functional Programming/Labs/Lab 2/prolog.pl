%3.A

%count_elem(L: Lista, E: Int, O: Int)
%count_elem(i,i,o)
%rem_rep_aux(L: Lista, OG: Original, R: Raspuns)
%rem_rep_aux(i,i,o)
%rem_rep(L: Lista, R: Raspuns)
%rem_rep(i,o)


count_elem([], _, O) :-
	O is 0.
count_elem([H|T], E, O) :- 
	E==H,
	!,
	count_elem(T, H, O1),
	O is O1 + 1.
count_elem([_|T], E, O) :-
	count_elem(T, E, O).

rem_rep_aux([], _, []) :- !.
rem_rep_aux([H|T], L, [H|R]) :-
	count_elem(L, H, O),
	O == 1,
	!,
	rem_rep_aux(T, L, R).
rem_rep_aux([_|T], L, R) :-
	rem_rep_aux(T, L, R).

rem_rep(L, R) :-
	rem_rep_aux(L, L, R).


%3.B

%maximum(L: List, M: MaxValue)
%maximum(i,o)
%rem_max_aux(L: List, M: MaxValue, R: Raspuns)
%rem_max_aux(i,i,o)
%rem_max(L: List, R: Raspuns)
%rem_max(i,o)

maximum([M], M) :- !.
maximum([H|T], M) :-
	maximum(T, M1),(
		(H > M1, !, M is H);
		M is M1).

rem_max_aux([], _, []) :- !.
rem_max_aux([H|T], H, R) :-
	!,
	rem_max_aux(T, H, R).
rem_max_aux([H|T], M, [H|R]) :-
	rem_max_aux(T, M, R).

rem_max(L, R) :-
	maximum(L, M),
	rem_max_aux(L, M, R).