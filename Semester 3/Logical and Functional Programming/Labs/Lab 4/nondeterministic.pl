%append(L1: List, L2: List, S: List)
%append(i,i,o)
append([], X, X).
append([X|Y], Z, [X|W]) :- 
	append( Y, Z, W).

%sublists_aux(L: List, E: Integer, S: List, C: List)
%sublists_aux(i,i,o,i)
sublists_aux([], _, C, C).
sublists_aux([H|T], E, R, C) :-
	E < H,
	append(C, [H], C1),
	sublists_aux(T, H, R, C1).
sublists_aux([H|T], E, R, C) :-
	sublists_aux(T, E, R, C).
	
%sublists(L: List, S: List)
%sublists(i,o)
sublists([], []).
sublists([H|T], R) :-
	sublists_aux(T, H, R, [H]).
sublists([H|T], R) :-
	sublists(T, R).