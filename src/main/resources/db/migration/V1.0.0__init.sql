CREATE TABLE `Account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `name` varchar(250) NOT NULL,
  `password` varchar(250) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_UN` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;