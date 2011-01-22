ALTER TABLE `ratecred`.`rating` ADD COLUMN `referal_url` LONGTEXT AFTER `flag`,
 ADD COLUMN `referal_token` VARCHAR(45) AFTER `referal_url`;