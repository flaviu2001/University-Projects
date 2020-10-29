-- Print the names of books which are written by players who have at least 3 fans
select CB.name, CB.author
from ChessBook CB
where author in (
    select P.name
    from Player P inner join FanOfPlayer FOP on P.pid = FOP.pid inner join Fan F on F.fid = FOP.fid
    group by P.name
    having count(*) >= 3
)

-- Print the names of chessboards which have been at games which have been at tournaments
select CB.name
from Chessboard CB
where CB.cbid in (
    select G.cbid
    from Game G
    where G.tid in (
        select T.tid
        from Tournament T
    )
)