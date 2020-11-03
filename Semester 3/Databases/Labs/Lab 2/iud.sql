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
insert into Fan values (1, 'Jack', 21, 'Romania')
insert into Fan values (3, 'Craciun Flaviu', 19, 'Romania')
insert into Fan values (2, 'John', 24, 'UK')
insert into Fan values (4, 'Johnny', 33, 'USA')
insert into FanOfPlayer values (1, 3)
insert into Tournament values (1, '20100311', '20100314')
insert into Tournament values (2, '20201020', '20201023')
update Game set tid = 1 where tid is null
update Player set fide_rating = 2950 where pid = 3 -- usage of =
update ChessClub set city = 'Moscow' where ccid = 1
update Game set game_date = '20201020' where gid = 1
update Fan set age=age+1 where country in ('Romania', 'Belarus') and age between 10 and 20 --AND, IN, BETWEEN
delete from Fan where name like 'John%' -- like
delete from Player where pid = 5
delete from Game where gid = 3
insert into ChessBook values (1, 'How to become grandmaster', 'Garry Kasparov', 100)
insert into ChessBook values (2, 'Best games of chess', 'Bobby Fischer', 50)
insert into ChessboardCompany values (1, 'Chessboards inc', 124)
insert into ChessboardCompany values (2, 'Boards of Chess llc', 99)
insert into Chessboard values (1, 1, 150, 160, 'Big table')
insert into Chessboard values (2, 1, 50, 80, 'Small table')
insert into Game values (4, 3, 4, 4, null, 1, '20201019')
update Game set cbid = 1 where cbid is null -- is null
update Game set game_date = '20100311' where gid = 1
insert into Fan values (5, 'Jimmy', 19, 'UK')
insert into Fan values (6, 'Timmy', 34, 'USA')
insert into FanOfPlayer values (5, 2)
insert into FanOfPlayer values (6, 2)
insert into FanOfPlayer values (1, 2)
insert into ChessClub values (3, 'Newest chess club', 'New Zeeland', 'Auckland')
update Game set cbid = 2 where gid = 2
insert into Player values (7, 'Anatoly Karpov', 2500, 'Russia', 0, 69)
insert into Game values (4, 6, 7, 7, null, 2, '20141030')
insert into ChessBookInChessClub values (1, 1)
update Player set is_teacher=1 where pid=1
update Player set is_teacher=1 where pid=3