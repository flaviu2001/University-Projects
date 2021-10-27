exec addToTables 'ChessboardCompany'
exec addToTables 'Chessboard'

create or alter view getBoardsFromBigCompanies as
    select CB.name, CB.cbid
        from Chessboard CB inner join ChessboardCompany CC on CC.comp_id = CB.comp_id
        where CC.number_of_employees >= 100

exec addToViews 'getBoardsFromBigCompanies'
exec addToTests 'test2'
exec connectTableToTest 'Chessboard', 'test2', 1200, 2
exec connectTableToTest 'ChessboardCompany', 'test2', 600, 1
exec connectViewToTest 'getBoardsFromBigCompanies', 'test2'

create or alter procedure populateTableChessboard (@rows int) as
    while @rows > 0 begin
        insert into Chessboard(cbid, comp_id, board_size, price, name) values
            (@rows,
             (select top 1 comp_id from ChessboardCompany order by newid()),
             floor(100*rand()),
             floor(150*rand()),
             'Name')
        set @rows = @rows - 1
    end

create or alter  procedure populateTableChessboardCompany (@rows int) as
    while @rows > 0 begin
        insert into ChessboardCompany(comp_id, name, number_of_employees) values
            (@rows,
             'CompName',
             floor(200*rand()))
        set @rows = @rows - 1
    end

execute runTest 'test2'
