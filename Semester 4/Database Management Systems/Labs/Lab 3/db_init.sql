create table ChessBook
(
    bid int primary key,
    name varchar(max),
    author varchar(max),
    price int
)

create table ChessClub
(
    ccid int primary key,
    name varchar(max),
    country varchar(max),
    city varchar(max)
)

create table ChessBookInChessClub
(
    bid int references ChessBook(bid),
    ccid int references ChessClub(ccid),
    primary key (bid, ccid)
)

create table ChessboardCompany
(
    comp_id int primary key,
    name varchar(max),
    number_of_employees int
)

create table Chessboard
(
	cbid int primary key,
	comp_id int references ChessboardCompany(comp_id),
	board_size int,
	price int,
	name varchar(max)
)
