SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

INSERT INTO `user_principal` (
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
	1307330419279,
	1307330419279
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

--sam

INSERT INTO `user_principal` (
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
	0,
	'sam',
	'foobar',
	'sam@ratecred.com',
	0,
	0,
	0,
	1,
	'3cc0b3a7-d47f-4b1a-9a35-d63634a8fa1c',
	'',
	102,
	'sam',
	'twitter_token',
	'twitter_secret',
	'twitter_verify',
	'',
	1307330419279,
	1307330419279
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
	2
);

INSERT INTO `role`(
	`version`,
	`role`,
	`role_group`,
	`principal_id`
) VALUES (
	0,
	'ROLE_MEMBER',
	'GROUP_MEMBER',
	2
);

INSERT INTO `role`(
	`version`,
	`role`,
	`role_group`,
	`principal_id`
) VALUES (
	0,
	'ROLE_PUBLISHER',
	'GROUP_PUBLISHER',
	2
);

INSERT INTO `network_member`
(
`version`,
`user_principal_id`,
`name`,
`primary_email`,
`paypal_email`,
`member_key`,
`description`,
`icon_url`,
`map_icon_url`,
`time_created`,
`time_updated`)
VALUES
(
0,
2,
'sams site',
'sam@ratecred.com',
'sam@ratecred.com',
'4ba6a09',
'sam is good',
'',
'',
1307330419279,
1307330419279
);

INSERT INTO `publisher`
(
`version`,
`url`,
`site_name`,
`description`,
`summary`,
`monthly_pageviews`,
`network_member_id`,
`time_created`,
`time_updated`)
VALUES
(
0,
'http://samsite.com',
'sams site',
'great site for free stuff',
'great site for free stuff',
1000,
1,
1307330419279,
1307330419279
);

--places
INSERT INTO `place`
(
`version`,
`name`,
`address1`,
`city`,
`state`,
`zip`,
`twitter_id`,
`simple_geo_id`,
`phone`,
`latitude`,
`longitude`,
`description`,
`type`,
`flag`,
`url`,
`email`,
`time_created`,
`time_updated`)
VALUES
(
0,
'Lukas Tap Room',
'2221 Broadway',
'Oakland',
'CA',
'94612',
'lukas',
'',
'(510) 451-4677',
37.811359, -122.36701,
'great place for bears',
'',
'',
'',
'',
1307330419279,
1307330419279
);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
