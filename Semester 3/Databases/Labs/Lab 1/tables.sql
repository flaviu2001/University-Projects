create table Player
(
    pid int primary key,
    name varchar(max),
    fide_rating int,
    country varchar(max),
    is_teacher bit,
    age int
)

create table Tournament
(
    tid int primary key,
    date_begin date,
    date_end date
)

create table ChessboardCompany
(
    comp_id int primary key,
    name varchar(max),
    number_of_employees int
)

create table Chessboard --one(ChessboardCompany) to many(chessboard)
(
	cbid int primary key,
	comp_id int references ChessboardCompany(comp_id),
	board_size int,
	price int,
	name varchar(max)
)

create table Game --one(Tournament, Chessboard) to many(Game)
(
    gid int primary key,
    white_pid int references Player(pid),
    black_pid int references Player(pid),
    winner_pid int references Player(pid),
    tid int references Tournament(tid) null,
    cbid int references Chessboard(cbid) null,
    game_date date,
)

create table ChessClub
(
    ccid int primary key,
    name varchar(max),
    country varchar(max),
    city varchar(max)
)

create table ChessClubPlayer --many to many
(
    pid int references Player(pid),
    ccid int references ChessClub(ccid),
    primary key (pid, ccid)
)

create table ChessBook
(
    bid int primary key,
    name varchar(max),
    author varchar(max),
    price int
)

create table ChessBookInChessClub --many to many. A book can be in multiple clubs and a club can have multiple books
(
    bid int references ChessBook(bid),
    ccid int references ChessClub(ccid),
    primary key (bid, ccid)
)

create table Fan
(
    fid int primary key,
    name varchar(max),
    age int,
    country varchar(max)
)

create table FanOfPlayer --many to many
(
    fid int references Fan(fid),
    pid int references Player(pid),
    primary key (fid, pid)
)