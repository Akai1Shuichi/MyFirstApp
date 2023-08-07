CREATE DATABASE app ;
use app;

Create table `user` (
  `id` int(11) not null auto_increment,
  `name` varchar(255) default null,
  `email` varchar(255) unique default null ,
  `password` varchar(255) default null,
  primary key (`id`)
);

drop table `user`;

-- Create table `token` (
--   `id_token` int(11) not null auto_increment,
--   `id_user` int(11) ,
--   `token` varchar(255) default null,
--    primary key (`id_token`),
--    CONSTRAINT `FK_token.id_token`
--     FOREIGN KEY (`id_user`)
--       REFERENCES `user`(`id`)
-- );token

Create table `token` (
  `id_token` int(11) not null auto_increment,
  `id_user` int(11) ,
  `token` varchar(255) default null,
   primary key (`id_token`),
    FOREIGN KEY (`id_user`)
      REFERENCES `user`(`id`)
);


drop table `token` ;

insert into `user` values(1,'admin','admin','admin');
insert into `token` (`id_user`,`token`) values(2,'abc');