-- Print players better than all players in Russia
select P.name, P.fide_rating
from Player P
where P.fide_rating > all (
    select P2.fide_rating
    from Player P2
    where P2.country = 'Russia'
)

-- Print players better than all players in Russia (aggregation operator)
select P.name, P.fide_rating
from Player P
where P.fide_rating > (
    select max(P2.fide_rating)
    from Player P2
    where P2.country = 'Russia'
)

-- Print players with fans 3 times younger than them
select P.name   -- Arithmetic expression
from Player P
where P.age/3 > any (
    select F.age
    from FanOfPlayer inner join Fan F on F.fid = FanOfPlayer.fid inner join Player P2 on P2.pid = FanOfPlayer.pid
    where P2.pid = P.pid
)

-- Print players with fans 3 times younger than them (aggregation operator)
select P.name
from Player P
where P.age/3 > (
    select min(F.age)
    from FanOfPlayer inner join Fan F on F.fid = FanOfPlayer.fid inner join Player P2 on P2.pid = FanOfPlayer.pid
    where P2.pid = P.pid
)

-- Print players who are from neither of the countries of their clubs, but are in at least one club
select P.name
from Player P
where P.country <> all(
    select CC.country
    from ChessClubPlayer CCP join ChessClub CC on CC.ccid = CCP.ccid join Player P2 on CCP.pid = P2.pid
    where CCP.pid = P.pid
) and P.pid in (
    select CCP2.pid
    from ChessClubPlayer CCP2 join ChessClub CC2 on CC2.ccid = CCP2.ccid join Player P3 on P3.pid = CCP2.pid
)

-- Print players who are from neither of the countries of their clubs, but are in at least one club (not in)
select P.name
from Player P
where P.country not in(
    select CC.country
    from ChessClubPlayer CCP join ChessClub CC on CC.ccid = CCP.ccid join Player P2 on CCP.pid = P2.pid
    where CCP.pid = P.pid
) and P.pid in (
    select CCP2.pid
    from ChessClubPlayer CCP2 join ChessClub CC2 on CC2.ccid = CCP2.ccid join Player P3 on P3.pid = CCP2.pid
)

-- Print books written by players
select CB.name
from ChessBook CB
where CB.author = any(
    select P.name
    from Player P
)

-- Print books written by players (in)
select CB.name
from ChessBook CB
where CB.author in(
    select P.name
    from Player P
)