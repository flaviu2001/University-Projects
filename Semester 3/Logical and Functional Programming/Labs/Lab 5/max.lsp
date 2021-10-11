(defun maxrec(l) 
	(cond 
		((null l) -1000000)
		((numberp (car l)) (max (car l) (maxrec (cdr l))))
		((atom (car l)) (maxrec (cdr l)))
		(t (max (maxrec (car l)) (maxrec (cdr l))))
))