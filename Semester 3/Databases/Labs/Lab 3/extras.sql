-- Print boards larger than 100 and cheaper than 120
select *
from Chessboard
where price < 120 and board_size > 100

-- Print players from Norway younger than 30
select *
from Player
where country = 'Norway' and not age >= 30

-- Print countries which have players
select distinct country
from Player

-- Print the 3 youngest fans
select top 3 *
from Fan
order by age

-- Print all authors of books
select distinct author
from ChessBook

-- Print the 10 percent most expensive books
select top 10 percent name, price
from ChessBook
order by price

-- Print chessboards 2 times cheaper than all books written by Garry Kasparov
select C.name, C.price  -- Arithmetic expression
from Chessboard C
where 2*C.price < all (
    select CB.price
    from ChessBook CB
    where CB.author = 'Garry Kasparov'
)

-- Print players twice older than the youngest player
select P1.name, P1.age -- Arithmtic expression
from Player P1
where P1.age/2 > any (
    select P2.age
    from Player P2
)