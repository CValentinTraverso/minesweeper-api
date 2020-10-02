CREATE TABLE `game_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO game_status (id,status) VALUES
(1,'ACTIVE')
,(2,'WON')
,(3,'LOST')
;

CREATE TABLE `game` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `owner` bigint(20) NOT NULL,
  `revealed_fields` int(11) NOT NULL DEFAULT '0',
  `status` bigint(20) NOT NULL,
  `start_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_time` datetime DEFAULT NULL,
  `board` json NOT NULL,
  PRIMARY KEY (`id`),
  KEY `Game_Account_FK` (`owner`),
  CONSTRAINT `Game_Account_FK` FOREIGN KEY (`owner`) REFERENCES `account` (`id`)  ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;