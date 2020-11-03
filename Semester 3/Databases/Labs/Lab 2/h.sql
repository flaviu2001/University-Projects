-- Print countries with at least 2 players
select P.country, count(*) as players
from Player P
group by P.country
having count(*) > 1

-- Print the players with the most fans
select P.pid, P.name, count(*) as fans
from Player P inner join FanOfPlayer FOP on P.pid = FOP.pid inner join Fan F on F.fid = FOP.fid
group by P.pid, P.name
having count(*) = (
    select max(t.C)
    from (select count(*) C
        from Player P inner join FanOfPlayer FOP on P.pid = FOP.pid inner join Fan F on F.fid = FOP.fid
        group by P.pid, P.name
    )t
)

-- Print the clubs with most teachers inside them
select CC2.name
from ChessClub CC2
where CC2.ccid in (
    select CCP.ccid
    from ChessClubPlayer CCP
    where CCP.pid in (
        select P.pid
        from Player P
        where P.is_teacher=1
    )
    group by CCP.ccid
    having count(*) in (
        select count(*)
        from ChessClubPlayer CCP2
        where CCP2.pid in (
            select P.pid
            from Player P
            where P.is_teacher=1
        )
        group by CCP2.ccid
    )
)

-- Print the ages of fans together with the number of fans of each age
select age, count(*) as number_of_fans
from Fan
group by age
