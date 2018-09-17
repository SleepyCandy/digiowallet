create table account
(
  username varchar(10) default '' not null
    primary key,
  password varchar(10)            not null,
  name     varchar(10)            not null,
  email    varchar(50)            not null,
  money    double                 null
);

create table historypayment
(
  datetime datetime    null,
  action   varchar(10) null,
  username varchar(10) null,
  Receiver varchar(10) null,
  addmoney double      null
);


