exec addToTables 'ChessBook'
exec addToTables 'ChessClub'
exec addToTables 'Player'
exec addToTables 'ChessBookInChessClub'
exec addToTables 'ChessClubPlayer'

create or alter view getPlayersWhoPublishedInTheirClub as
    select distinct P.name -- distinct
    from Player P
        inner join ChessClubPlayer CCP on P.pid = CCP.pid
        inner join ChessClub CC on CC.ccid = CCP.ccid
        inner join ChessBookInChessClub CBICC on CC.ccid = CBICC.ccid
        inner join ChessBook CB on CBICC.bid = CB.bid and CB.author = P.name

exec addToViews 'getPlayersWhoPublishedInTheirClub'
exec addToTests 'test3'
exec connectTableToTest 'ChessBook', 'test3', 100, 1
exec connectTableToTest 'ChessClub', 'test3', 100, 2
exec connectTableToTest 'Player', 'test3', 100, 3
exec connectTableToTest 'ChessBookInChessClub', 'test3', 100, 4
exec connectTableToTest 'ChessClubPlayer', 'test3', 100, 5
exec connectViewToTest 'getPlayersWhoPublishedInTheirClub', 'test3'

create or alter procedure populateTableChessBook (@rows int) as
    while @rows > 0 begin
        insert into ChessBook (bid, name, author, price) values (@rows, 'book', 'player', floor(rand()*100))
        set @rows = @rows - 1
    end

create or alter procedure populateTableChessClub (@rows int) as
    while @rows > 0 begin
        insert into ChessClub (ccid, name, country, city) values (@rows, 'club', 'country', 'city')
        set @rows = @rows - 1
    end

create or alter procedure populateTablePlayer (@rows int) as
    while @rows > 0 begin
        insert into Player (pid, name, fide_rating, country, is_teacher, age) values (@rows, 'player', floor(rand()*2000) + 500, 'country', 0, floor(rand()*100))
        set @rows = @rows - 1
    end

create or alter procedure populateTableChessBookInChessClub (@rows int) as
    declare @bid int
    declare @ccid int
    while @rows > 0 begin
        set @bid = (select top 1 bid from ChessBook order by newid())
        set @ccid = (select top 1 ccid from ChessClub order by newid())
        while exists(select * from ChessBookInChessClub where bid = @bid and ccid = @ccid) begin
            set @bid = (select top 1 bid from ChessBook order by newid())
            set @ccid = (select top 1 ccid from ChessClub order by newid())
        end
        insert into ChessBookInChessClub (bid, ccid) values (@bid, @ccid)
        set @rows = @rows - 1
    end

create or alter procedure populateTableChessClubPlayer (@rows int) as
    declare @pid int
    declare @ccid int
    while @rows > 0 begin
        set @pid = (select top 1 pid from Player order by newid())
        set @ccid = (select top 1 ccid from ChessClub order by newid())
        while exists(select * from ChessClubPlayer where pid = @pid and ccid = @ccid) begin
            set @pid = (select top 1 pid from Player order by newid())
            set @ccid = (select top 1 ccid from ChessClub order by newid())
        end
        insert into ChessClubPlayer (pid, ccid) values (@pid, @ccid)
        set @rows = @rows - 1
    end

exec runTest 'test3'