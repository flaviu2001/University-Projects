(defun even(l) 
	(cond
		((null l) t)
		((even (cdr l)) nil)
		(t t)
))