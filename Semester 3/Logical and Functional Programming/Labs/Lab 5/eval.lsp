(defun expression (l)
    (cond
        ((null l) nil)
        (
         (and (not (numberp (car l))) (numberp (cadr l)) (numberp (caddr l)))
         (cons (eval (list (car l) (cadr l) (caddr l))) (expression (cdddr l)))
        )
        (T (cons (car l) (expression (cdr l))))
    )
)

(defun solve (l)
    (cond
        ((null (cdr l)) (car l))
        (t (solve (expression l)))
    )
)