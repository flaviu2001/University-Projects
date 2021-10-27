-- Print players that have fans
select P.name
from Player P
where exists(
    select *
    from FanOfPlayer FP
    where FP.pid = P.pid
)

-- Print chess clubs with members in them
select CC.name
from ChessClub CC
where exists(
    select *
    from ChessClubPlayer CCP
    where CCP.ccid = CC.ccid
)
