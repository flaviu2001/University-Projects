exec addToTables 'Fan'

create or alter view getAgeGroups as
    select age, count(*) as number_of_fans
    from Fan
    group by age

exec addToViews 'getAgeGroups'
exec addToTests 'test1'
exec connectTableToTest 'Fan', 'test1', 1000, 1
exec connectViewToTest 'getAgeGroups', 'test1'

create or alter procedure populateTableFan (@rows int) as
    while @rows > 0 begin
        insert into Fan(fid, age, country, name) values (@rows, floor(rand()*100), 'Testing', 'Name')
        set @rows = @rows-1
    end

execute runTest 'test1'