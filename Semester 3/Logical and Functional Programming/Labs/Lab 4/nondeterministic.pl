revert([], C, C) :-!.
revert([H|T], C, R) :-
	revert(T, [H|C], R).

%sublists_aux(L: List, E: Integer, S: List, C: List)
%sublists_aux(i,i,o,i)
sublists_aux([], _, C, C).
sublists_aux([H|T], E, R, C) :-
	E < H,
	sublists_aux(T, H, R, [H|C]).
sublists_aux([H|T], E, R, C) :-
	sublists_aux(T, E, R, C).
	
%sublists(L: List, S: List)
%sublists(i,o)
sublists([], []).
sublists([H|T], R) :-
	sublists_aux(T, H, R1, [H]),
	revert(R1, [], R).
sublists([H|T], R) :-
	sublists(T, R).

%all_sublists(L: List, B: List)
all_sublists(L, B) :-
	findall([R1,R2|T], sublists(L, [R1,R2|T]), B).
