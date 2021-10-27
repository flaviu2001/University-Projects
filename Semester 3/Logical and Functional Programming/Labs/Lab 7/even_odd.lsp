(defun calc(x) (cond
	((evenp x) x)
	(t (- x))
))

(defun evenodd(l) (cond 
	((numberp l) (calc l))
	((atom l) 0)
	(t (apply #'+ (mapcar #'evenodd l)))
))