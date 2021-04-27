create or alter procedure populateTableChessBook (@name varchar(100), @author varchar(100), @price float) as
    DECLARE @maxId INT
	SET @maxId = 0
	SELECT TOP 1 @maxId = bid + 1 FROM ChessBook ORDER BY bid DESC
	IF (@name is null)
	BEGIN
		RAISERROR('Chess book name must not be null', 24, 1);
	END
	IF (@author is null)
	BEGIN
		RAISERROR('Chess book author must not be null', 24, 1);
	END
	IF (@price < 0)
	BEGIN
		RAISERROR('Chess book price must not be negative', 24, 1);
	END
	insert into ChessBook (bid, name, author, price) values (@maxId, @name, @author, @price)
go

create or alter procedure populateTableChessClub (@name varchar(100), @country varchar(100), @city varchar(100)) as
    DECLARE @maxId INT
	SET @maxId = 0
	SELECT TOP 1 @maxId = ccid + 1 FROM ChessClub ORDER BY ccid DESC
	IF (@name is null)
	BEGIN
		RAISERROR('Chess club name must not be null', 24, 1);
	END
	IF (@country is null)
	BEGIN
		RAISERROR('Chess club country must not be null', 24, 1);
	END
	IF (@city is null)
	BEGIN
		RAISERROR('Chess club city must not be null', 24, 1);
	END
    insert into ChessClub (ccid, name, country, city) values (@maxId, @name, @country, @city)
go

CREATE OR ALTER PROCEDURE populateTableChessBookInChessClub(@ChessBookName VARCHAR(50) , @ChessClubName VARCHAR(50))
AS
	IF (@ChessBookName is null)
	BEGIN
		RAISERROR('Chess book name must not be null', 24, 1);
	END
	
	IF (@ChessClubName is null)
	BEGIN
		RAISERROR('Chess book name must not be null', 24, 1);
	END

	DECLARE @ChessBookID INT
	SET @ChessBookID = (SELECT bid FROM ChessBook WHERE name = @ChessBookName)
	DECLARE @ChessClubID INT
	SET @ChessClubID = (SELECT ccid FROM ChessClub WHERE name = @ChessClubName)
	IF (@ChessBookID is null)
	BEGIN
		RAISERROR('Chess book name does not exist', 24, 1);
	END
	IF (@ChessClubID is null)
	BEGIN
		RAISERROR('Chess club does not exist', 24, 1);
	END
	INSERT INTO ChessBookInChessClub VALUES (@ChessBookID, @ChessClubID)
GO

CREATE OR ALTER PROCEDURE addRollbackScenarioNoFail
AS
	BEGIN TRAN
	BEGIN TRY
		EXEC populateTableChessBook 'Chess in 10 easy steps', 'Garry Kasparov', 150
		EXEC populateTableChessClub 'yuri', 'kazakstan', 'nur-sultan'
		EXEC populateTableChessBookInChessClub 'Chess in 10 easy steps', 'yuri'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		RETURN
	END CATCH
	COMMIT TRAN
GO

CREATE OR ALTER PROCEDURE addRollbackScenarioFail
AS
	BEGIN TRAN
	BEGIN TRY
		EXEC populateTableChessBook 'Chess in 10 easy steps', 'Garry Kasparov', 150
		EXEC populateTableChessClub 'yuri gagarin', 'kazakstan', 'nur-sultan'
		EXEC populateTableChessBookInChessClub 'Chess in 10 easy steps', 'yuri'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		RETURN
	END CATCH
	COMMIT TRAN
GO

CREATE OR ALTER PROCEDURE addNoRollbackScenarioManyToMany
AS
	DECLARE @ERRORS INT
	SET @ERRORS = 0
	BEGIN TRY
		EXEC populateTableChessBook 'Chess in 10 easy steps', 'Garry Kasparov', 150
	END TRY
	BEGIN CATCH
		SET @ERRORS = @ERRORS + 1
	END CATCH

	BEGIN TRY
		EXEC populateTableChessClub 'yuri', 'kazakstan', 'nur-sultan'
	END TRY
	BEGIN CATCH
		SET @ERRORS = @ERRORS + 1
	END CATCH

	IF (@ERRORS = 0) BEGIN
		BEGIN TRY
			EXEC populateTableChessBookInChessClub 'Chess in 10 easy steps', 'yuri'
		END TRY
		BEGIN CATCH
		END CATCH
	END
GO

select * from ChessBook
select * from ChessClub
select * from ChessBookInChessClub

DELETE FROM ChessBookInChessClub
DELETE FROM ChessBook
DELETE FROM ChessClub

exec addRollbackScenarioFail
exec addRollbackScenarioNoFail
exec addNoRollbackScenarioManyToMany