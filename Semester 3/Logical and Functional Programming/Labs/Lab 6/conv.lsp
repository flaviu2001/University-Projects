;nrchildren(l: List)
(defun nrchildren(l) (cond
	((null (cdr l)) 0)
	((null (cddr l)) 1)
	((null (cdddr l)) 2)
))

;conv(l: List)
(defun conv(l) (cond 
	((null l) nil)
	(t (append (list (car l) (nrchildren l)) (conv (cadr l)) (conv (caddr l))))
))
