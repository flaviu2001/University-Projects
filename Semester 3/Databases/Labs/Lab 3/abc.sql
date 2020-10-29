-- In a Russian tournament all russian players and the best players of the world are invited.
-- The list of allowed attendants also includes russian fans
select name from Player where country = 'Russia' or fide_rating > 2700
union
select name from Fan where country = 'Russia'

-- All players from Russia or USA (set union with or)
select name from Player where country = 'Russia' or country = 'USA' -- OR IN WHERE

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