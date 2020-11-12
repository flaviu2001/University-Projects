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
        add comp_id int constraint EMPLOYEE_FOREIGN_KEY_1 references ChessboardCompany(comp_id)

create procedure dropForeignKeyEmployee as
    alter table Employee drop constraint EMPLOYEE_FOREIGN_KEY_1
    alter table Employee drop column comp_id