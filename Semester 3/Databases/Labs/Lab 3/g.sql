-- Print chessboards made by companies with at least 100 employees which appeared at games
select t.name
from (
    select CB.name, CB.cbid
    from Chessboard CB inner join ChessboardCompany CC on CC.comp_id = CB.comp_id
    where CC.number_of_employees >= 100
)t
where t.cbid in (
    select G.cbid
    from Game G
)

-- Print chessboards which cost at least 100 which appeared at games
select t.name, t.price
from (
    select *
    from Chessboard CB
    where CB.price >= 100
)t where t.cbid in (
    select G.cbid
    from Game G
)