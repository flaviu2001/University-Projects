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
declare @oldData int
declare @newData int
waitfor delay '00:00:10'
update Chessboard set @oldData=price, price=1000, @newData=price where cbid=1
exec sp_log_changes @oldData, @newData, 'Non-Repeatable Reads 1: update'
exec sp_log_locks 'Non-Repeatable Reads 1: after update'
commit tran

-- query console 2
select @@SPID
select @@TRANCOUNT
DBCC USEROPTIONS

-- set transaction isolation level read committed
set transaction isolation level repeatable read --solution
begin tran
select * from Chessboard
exec sp_log_locks 'Non-Repeatable Reads 2: between selects'
waitfor delay '00:00:10'
select * from Chessboard
exec sp_log_locks 'Non-Repeatable Reads 2: after both selects'
commit tran 
