-- query console 1
select @@SPID
select @@TRANCOUNT
DBCC USEROPTIONS


delete from Chessboard
delete from ChessboardCompany
insert into ChessboardCompany(comp_id, name, number_of_employees) values (1, 'namecc1', 1)

alter database lab3 set allow_snapshot_isolation on

begin tran
declare @oldData varchar(100)
declare @newData varchar(100)
update ChessboardCompany set @oldData=name, name='name2', @newData=name where comp_id=1
waitfor delay '00:00:10'
exec sp_log_changes @oldData, @newData, 'Update Conflict 1: update'
exec sp_log_locks 'Update Conflict 1: after update'
select * from ChessboardCompany
commit tran

-- query console 2
select @@SPID
select @@TRANCOUNT
DBCC USEROPTIONS

-- set transaction isolation level read uncommitted --solution
set transaction isolation level snapshot

begin tran
declare @oldData varchar(100)
declare @newData varchar(100)
update ChessboardCompany set @oldData=name, name='name3', @newData=name where comp_id=1
exec sp_log_changes @oldData, @newData, 'Update Conflict 2: update'
exec sp_log_locks 'Update Conflict 2: after update'
select * from ChessboardCompany
commit tran
 
