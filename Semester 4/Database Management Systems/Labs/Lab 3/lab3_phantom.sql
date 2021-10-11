-- query console 1
select @@SPID
select @@TRANCOUNT
DBCC USEROPTIONS


delete from Chessboard
delete from ChessboardCompany
insert into ChessboardCompany(comp_id, name, number_of_employees) values (1, 'namecc1', 1)
insert into Chessboard (cbid, comp_id, board_size, price, name) values (1, 1, 1, 2, 'namec1')
insert into Chessboard (cbid, comp_id, board_size, price, name) values (2, 1, 2, 3, 'namec2')


begin tran
waitfor delay '00:00:10'
insert into Chessboard (cbid, comp_id, board_size, price, name) values (3, 1, 3, 4, 'namec3')
exec sp_log_changes null, 3, 'Phantom 1: insert'
exec sp_log_locks 'Phantom 1: after insert'
commit tran

-- query console 2
select @@SPID
select @@TRANCOUNT
DBCC USEROPTIONS

-- set transaction isolation level repeatable read
set transaction isolation level serializable --solution
begin tran
select * from Chessboard
exec sp_log_locks 'Phantom 2: between selects'
waitfor delay '00:00:10'
select * from Chessboard
exec sp_log_locks 'Phantom 2: after both selects'
commit tran 
