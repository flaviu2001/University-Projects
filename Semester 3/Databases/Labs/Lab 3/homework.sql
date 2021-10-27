create procedure setIsTeacherFromPlayerTinyint as
    alter table Player alter column is_teacher tinyint

create procedure setIsTeacherFromPlayerBit as
    alter table Player alter column is_teacher bit

create procedure addCountryToTournmanet as
    alter table Tournament add country varchar(max)

create procedure removeCountryFromTournament as
    alter table Tournament drop column country

create procedure addDefaultToIsTeacherFromPlayer as
    alter table Player add constraint DEFAULT0 default(0) for is_teacher

create procedure removeDefaultFromIsTeacherFromPlayer as
    alter table Player drop constraint DEFAULT0

create procedure addEmployee as
    create table Employee (
        name varchar(100) not null,
        salary int,
        role varchar(100) not null,
        comp_id int not null,
        constraint EMPLOYEE_PRIMARY_KEY primary key(name)
    )

create procedure dropEmployee as
    drop table Employee

create procedure addRolePrimaryKeyEmployee as
    alter table Employee
        drop constraint EMPLOYEE_PRIMARY_KEY
    alter table Employee
        add constraint EMPLOYEE_PRIMARY_KEY primary key (name, role)

create procedure removeRolePrimaryKeyEmployee as
    alter table Employee
        drop constraint EMPLOYEE_PRIMARY_KEY
    alter table Employee
        add constraint EMPLOYEE_PRIMARY_KEY primary key (name)

create procedure newCandidateKeyFan as
    alter table Fan
        add constraint FAN_CANDIDATE_KEY_1 unique (name, age, country)

create procedure dropCandidateKeyFan as
    alter table Fan
        drop constraint FAN_CANDIDATE_KEY_1

create procedure newForeignKeyEmployee as
    alter table Employee
        add constraint EMPLOYEE_FOREIGN_KEY_1 foreign key(comp_id) references ChessboardCompany(comp_id)

create procedure dropForeignKeyEmployee as
    alter table Employee drop constraint EMPLOYEE_FOREIGN_KEY_1

create table versionTable (
    version int
)

insert into versionTable values (1) -- initial version

create table proceduresTable (
    fromVersion int,
    toVersion int,
    primary key (fromVersion, toVersion),
    nameProc varchar(max)
)

insert into proceduresTable values (1, 2, 'setIsTeacherFromPlayerTinyint')
insert into proceduresTable values (2, 1, 'setIsTeacherFromPlayerBit')
insert into proceduresTable values (2, 3, 'addCountryToTournmanet')
insert into proceduresTable values (3, 2, 'removeCountryFromTournament')
insert into proceduresTable values (3, 4, 'addDefaultToIsTeacherFromPlayer')
insert into proceduresTable values (4, 3, 'removeDefaultFromIsTeacherFromPlayer')
insert into proceduresTable values (4, 5, 'addEmployee')
insert into proceduresTable values (5, 4, 'dropEmployee')
insert into proceduresTable values (5, 6, 'addRolePrimaryKeyEmployee')
insert into proceduresTable values (6, 5, 'removeRolePrimaryKeyEmployee')
insert into proceduresTable values (6, 7, 'newCandidateKeyFan')
insert into proceduresTable values (7, 6, 'dropCandidateKeyFan')
insert into proceduresTable values (7, 8, 'newForeignKeyEmployee')
insert into proceduresTable values (8, 7, 'dropForeignKeyEmployee')

create procedure goToVersion(@newVersion int) as
    declare @curr int
    declare @var varchar(max)
    select @curr=version from versionTable

    if @newVersion > (select max(toVersion) from proceduresTable)
        raiserror ('Bad version', 10, 1)

    while @curr > @newVersion begin
        select @var=nameProc from proceduresTable where fromVersion=@curr and toVersion=@curr-1
        exec (@var)
        set @curr=@curr-1
    end

    while @curr < @newVersion begin
        select @var=nameProc from proceduresTable where fromVersion=@curr and toVersion=@curr+1
        exec (@var)
        set @curr=@curr+1
    end

    update versionTable set version=@newVersion

execute goToVersion 1
