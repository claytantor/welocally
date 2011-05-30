SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

INSERT INTO `user_principal` (
	`id`,
	`version`,
	`user_name`,
	`password`,
	`email`,
	`expired`,
	`credentials_expired`,
	`locked`,
	`enabled`,
	`guid`,
	`user_class`,
	`twitter_id`,
	`twitter_username`,
	`twitter_token`,
	`twitter_secret`,
	`twitter_verify`,
	`twitter_profile_img`,
	`time_created`,
	`time_updated`
) VALUES (
	1,
	0,
	'claytantor',
	'foobar',
	'clay@ratecred.com',
	0,
	0,
	0,
	1,
	'2cc0b3a7-d47f-4b1a-9a35-d63634a8fa1c',
	'USER',
	101,
	'claytantor',
	'twitter_token',
	'twitter_secret',
	'twitter_verify',
	'',
	1,
	1
);

INSERT INTO `role`(
	`version`,
	`role`,
	`role_group`,
	`principal_id`
) VALUES (
	0,
	'ROLE_USER',
	'GROUP_USER',
	1
);

INSERT INTO `role`(
	`version`,
	`role`,
	`role_group`,
	`principal_id`
) VALUES (
	0,
	'ROLE_ADMIN',
	'GROUP_ADMIN',
	1
);



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
