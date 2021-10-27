create table user
(
    userID   int          not null primary key AUTO_INCREMENT,
    name     varchar(100) not null,
    username varchar(100) not null,
    password varchar(100) not null,
    age      int          not null,
    role     varchar(100) not null,
    email    varchar(100) not null,
    webpage  varchar(100) not null
);