create table event_log (id int(10) auto_increment primary key, 
						node_id varchar(20) not null, 
						data_type varchar(20) not null, 
						old_value varchar(100), 
						new_value varchar(100), 
						time datetime not null);
delimiter |
create trigger event_node_status after insert on node_status for each row
begin
if(new.data_type = 'STATUS') then
insert into event_log (node_id, data_type, old_value, new_value, time) values(new.node_id, new.data_type, null,new.value,now());
end if;
end;
|
create trigger event_node_status2 after update on node_status for each row
begin
if(new.data_type = 'STATUS' and new.value != old.value) then
insert into event_log (node_id, data_type, old_value, new_value, time) values(new.node_id, new.data_type, old.value,new.value,now());
end if;
end;
|
delimiter ;	
