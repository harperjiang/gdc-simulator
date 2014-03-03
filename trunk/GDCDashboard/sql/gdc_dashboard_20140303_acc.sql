insert into seq_table (name,count) values('migration_history',1);

CREATE TABLE `migration_history` (
  `id` int(10) NOT NULL,
  `from_machine` varchar(20) DEFAULT NULL,
  `to_machine` varchar(20) DEFAULT NULL,
  `vm_name` varchar(40) DEFAULT NULL,
  `begin_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

