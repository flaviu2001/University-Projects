-- query console 1
select * from Chessboard
sp_helpindex Chessboard
select @@SPID
select @@TRANCOUNT


begin tran
update Chessboard set price=1000 where cbid=1
update Chessboard set price=1000 where cbid=2
commit tran


-- query console 2
select @@TRANCOUNT

begin tran
update Chessboard set price=1000 where cbid=2
update Chessboard set price=1000 where cbid=1
commit tran


--query console 3
select resource_type, request_mode, request_type, request_status, request_session_id 
from sys.dm_tran_locks 
where request_owner_type = 'TRANSACTION'
