%append_front(E: Integer, L: List, S: List)
%append_front(i,i,o)
append_front(E, L, [E|L]).

%increment_aux(L: List, E: Integer, S: List)
%increment_aux(i,i,o)
increment_aux([H], C, [R]) :-
	(
		CHK1 is H+1,
		CHK1 == 10,
		R is 0,
		C is 1,
		!
	);(
		R is H+1,
		C is 0,
		!
	),
	!.

increment_aux([H|T], C, [R1|R2]) :-
	increment_aux(T, C1, R2),
	((
		CHK2 is C1+H,
		CHK2 == 10,
		R1 is 0,
		C is 1,
		!
	);(
		R1 is C1+H,
		C is 0,
		!
	)).

%increment(L: List, S: List)
%increment(i,o)
increment(L, R) :-
	increment_aux(L, C, R1),
	(
		(
			C == 1,
			append_front(1, R1, R),
			!
		);(
			R=R1,
			!
		)
	),
	!.

%increment_sublists(L: List, S: List)
%increment_sublists(i,o)
increment_sublists([H], R) :-
	(
		number(H),
		append_front(H, [], R),
		!
	);(
		increment(H, R1),
		append_front(R1, [], R),
		!
	).

increment_sublists([H|T], R) :-
	increment_sublists(T, R1),
	(
		(
			number(H),
			append_front(H, R1, R),
			!
		);(
			increment(H, R2),
			append_front(R2, R1, R),
			!
		)
	).