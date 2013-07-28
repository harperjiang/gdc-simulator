insert into seq_table (name,count) values ('migration_log',0);
create table migration_log(id int(10) primary key, from_machine varchar(20),to_machine varchar(20), vm_name varchar(40),begin_time datetime, end_time datetime);
