CREATE SCHEMA IF NOT EXISTS `java_database` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE ON java_database.* TO 'www-data'@'localhost' IDENTIFIED BY 'technopark';
USE java_database;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `init_database`()
BEGIN
CREATE TABLE IF NOT EXISTS `Session_User` (
  `session` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `user` int(10) unsigned NOT NULL,
  PRIMARY KEY (`session`),
  UNIQUE KEY `index2` (`session`,`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
TRUNCATE Session_User;
CREATE TABLE IF NOT EXISTS `User` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `login` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login` (`login`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
INSERT IGNORE INTO User (id, login, password, email) VALUES (1, 'admin', 'admin', 'admin@admin.com');
INSERT IGNORE INTO User (id, login, password, email) VALUES (2, 'guest', '12345', 'guest@guest.com');
END ;;
DELIMITER ;