CREATE TABLE LogLocks(
	currentTime DATETIME,
	info VARCHAR(100),

	resource_type VARCHAR(100),
	request_mode VARCHAR(100),
	request_type VARCHAR(100),
	request_status VARCHAR(100),
	request_session_id INT
)

CREATE TABLE LogChanges(
	currentTime DATETIME,
	info VARCHAR(100),
	oldData VARCHAR(100),
	newData VARCHAR(100),
	error VARCHAR(1000)
)

GO
CREATE OR ALTER PROCEDURE sp_log_locks
	@info VARCHAR(100)
AS
BEGIN
	INSERT INTO LogLocks (currentTime, info, resource_type, request_mode, request_type, request_status, request_session_id)
	SELECT GETDATE(), @info, resource_type, request_mode, request_type, request_status, request_session_id
	FROM sys.dm_tran_locks
	WHERE request_owner_type = 'TRANSACTION'

END
GO

CREATE OR ALTER PROCEDURE sp_log_changes
	@oldData VARCHAR(100),
	@newData VARCHAR(100),
	@info VARCHAR(100),
	@error VARCHAR(100)
AS
BEGIN
	INSERT INTO LogChanges (currentTime, oldData, newData, error, info) VALUES
	(GETDATE(), @oldData, @newData, @error, @info)
END
GO

select * from LogChanges
select * from LogLocks