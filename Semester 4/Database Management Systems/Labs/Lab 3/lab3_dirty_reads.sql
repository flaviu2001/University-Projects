-- query console 1
select @@SPID
select @@TRANCOUNT
DBCC USEROPTIONS


delete from Chessboard
delete from ChessboardCompany
insert into ChessboardCompany(comp_id, name, number_of_employees) values (1, 'namecc1', 1)
insert into Chessboard (cbid, comp_id, board_size, price, name) values (1, 1, 1, 2, 'namec1')
insert into Chessboard (cbid, comp_id, board_size, price, name) values (2, 1, 2, 3, 'namec2')

set transaction isolation level read uncommitted
-- set transaction isolation level read committed --solution

begin tran
select * from Chessboard
exec sp_log_locks 'Dirty reads 2: after select'
waitfor delay '00:00:10'
select * from Chessboard
waitfor delay '00:00:10'
select * from Chessboard
commit tran

-- query console 2
select @@SPID
select @@TRANCOUNT
DBCC USEROPTIONS

begin tran
declare @oldData int
declare @newData int
update Chessboard set @oldData=price, price=1000, @newData=price where cbid=1
exec sp_log_changes @oldData, @newData, 'Dirty reads 1: update'
exec sp_log_locks 'Dirty reads 1: after update'
waitfor delay '00:00:10'
update Chessboard set @oldData=price, price=1500, @newData=price where cbid=1
exec sp_log_changes @oldData, @newData, 'Dirty reads 1: update'
exec sp_log_locks 'Dirty reads 1: after update'
waitfor delay '00:00:10'
commit tran
