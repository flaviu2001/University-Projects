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


select * from Player
select * from Game
select * from Fan
select * from Chessboard
select * from Tournament
select * from ChessBook

insert into Player values (1, 'Garry Kasparov', 2800, 'Russia', 0, 57)
insert into Player values (2, 'Bobby Fischer', 2900, 'USA', 0, 77)
insert into Player values (3, 'Magnus Carlsen', 2900, 'Norway', 0, 29)
insert into Player values (4, 'Craciun Flaviu', 1100, 'Romania', 0, 19)
insert into Player values (5, 'No one', 100, 'Narnia', 0, 190)
insert into Player values (6, 'Vasyl Ivanchuk', 2787, 'Ukraine', 0, 51)
insert into Game values (1, 1, 2, 1, null, null, '19720226')
insert into Game values (2, 1, 3, 3, null, null, '20100130')
insert into Game values (3, 1, -4, 1, null, null, '19900101') -- Error, no matching player for black_pid
insert into Game (gid, white_pid, black_pid, game_date, winner_pid) values (3, 2, 3, '19781013', 2)
insert into ChessClub values (1, 'Best chess club', 'Russia', 'Petrozavodsk')
insert into ChessClub values (2, 'Second chess club', 'USA', 'Washington DC')
insert into ChessClubPlayer values (1, 1)
insert into ChessClubPlayer values (2, 2)
insert into Fan values (3, 'Craciun Flaviu', 19, 'Romania')
insert into Fan values (2, 'John', 24, 'UK')
insert into FanOfPlayer values (1, 3)
insert into Tournament values (1, '20100311', '20100314')
insert into Tournament values (2, '20201020', '20201023')
update Game set tid = 1 where tid is null
update Player set fide_rating = 2950 where pid = 3
update ChessClub set city = 'Moscow' where ccid = 1
update Game set game_date = '20201020' where gid = 1
update Fan set age=age+1 where country in ('Romania', 'Belarus') and age between 10 and 20
delete from Player where pid = 5
delete from Game where gid = 3
insert into ChessBook values (1, 'How to become grandmaster', 'Garry Kasparov', 100)
insert into ChessBook values (2, 'Best games of chess', 'Bobby Fischer', 50)
insert into ChessboardCompany values (1, 'Chessboards inc', 124)
insert into Chessboard values (1, 1, 150, 160, 'Big table')
insert into Game values (4, 3, 4, 4, null, 1, '20201019')


-- In a Russian tournmanent all russian players and the best players of the world are invited.
-- The list of allowed players includes fans
select name from Player where country = 'Russia' or fide_rating > 2700
union
select name from Fan where country = 'Russia'

-- All players from Russia or USA (set union with or)
select name from Player where country = 'Russia' or country = 'USA'

-- All players who are also fans
select name from Player
intersect
select name from Fan

-- All players who are also fans (alternative)
select name from Player where name in (select name from Fan)

-- Countries with players but not clubs
select country from Player
except
select country from ChessClub

-- Countries with players but not clubs (alternative)
select country from Player where country not in (select country from ChessClub)