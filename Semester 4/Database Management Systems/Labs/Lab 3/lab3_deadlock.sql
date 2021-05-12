-- query console 1
select @@TRANCOUNT
select @@SPID


delete from Chessboard
delete from ChessboardCompany
insert into ChessboardCompany(comp_id, name, number_of_employees) values (1, 'namecc1', 1)
insert into Chessboard (cbid, comp_id, board_size, price, name) values (1, 1, 1, 2, 'namec1')
insert into Chessboard (cbid, comp_id, board_size, price, name) values (2, 1, 2, 3, 'namec2')

begin tran
declare @oldData int
declare @newData int
update Chessboard set @oldData=price, price=1000, @newData=price where cbid=1
exec sp_log_changes @oldData, @newData, 'Deadlock 1: Update 1'
exec sp_log_locks 'Deadlock 1: between updates'
waitfor delay '00:00:10'
update Chessboard set @oldData=price, price=1000, @newData=price where cbid=2
exec sp_log_changes @oldData, @newData, 'Deadlock 1: Update 2'
commit tran

-- query console 2
select @@TRANCOUNT
select @@SPID

begin tran
declare @oldData int
declare @newData int
update Chessboard set @oldData=price, price=1000, @newData=price where cbid=2
exec sp_log_changes @oldData, @newData, 'Deadlock 2: Update 1'
exec sp_log_locks 'Deadlock 2: between updates'
waitfor delay '00:00:05'
update Chessboard set @oldData=price, price=1000, @newData=price where cbid=1
exec sp_log_changes @oldData, @newData, 'Deadlock 2: Update 2'
commit tran
