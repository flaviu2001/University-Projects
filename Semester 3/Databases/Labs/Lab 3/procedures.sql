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