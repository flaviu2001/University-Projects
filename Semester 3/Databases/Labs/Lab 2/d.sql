-- Print for each chessboard its company it was made by
select C.name, CC.name from Chessboard C inner join ChessboardCompany CC on CC.comp_id = C.[comp_id]

-- Select players that are authors of at least a book in at least one of their chess club
select distinct P.name -- distinct
from Player P
    inner join ChessClubPlayer CCP on P.pid = CCP.pid
    inner join ChessClub CC on CC.ccid = CCP.ccid
    inner join ChessBookInChessClub CBICC on CC.ccid = CBICC.ccid
    inner join ChessBook CB on CBICC.bid = CB.bid and CB.author = P.name

-- Print for each game its beginning date of the tournament (if any) and its table (if any)
select T.date_begin, G.game_date, C.name from Game G left join Tournament T on T.tid = G.gid left join Chessboard C on G.cbid = C.cbid

-- Print all player-fan relationships, and if one of them doesn't have the other print it anyway
select P.name, F.name from Player P full join FanOfPlayer FOP on P.pid = FOP.pid full join Fan F on F.fid = FOP.fid order by P.name -- Order

-- Print for each tournament games held on its opening day or if there's no game print the tournament anyway
select T.date_begin, G.gid from Game G right join Tournament T on T.tid = G.tid and T.date_begin = G.game_date