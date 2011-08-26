alter table patron add facebookId bigint(20);
alter table patron drop foreign key fk_patron_user_principal1;
alter table patron drop index fk_patron_user_principal1;
alter table patron modify user_principal_id bigint(20);

alter table award drop foreign key fk_award_award_offer;
alter table award drop index fk_award_award_offer;
alter table award modify award_offer_id bigint(20);


-- -----------------------------------------------------
-- Table `simple_geo_json_token`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `simple_geo_json_token` ;

CREATE  TABLE IF NOT EXISTS `simple_geo_json_token` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT NOT NULL ,
  `json_token` VARCHAR(255) NOT NULL ,
  `start_assignment_date` BIGINT NULL DEFAULT NULL ,
  `end_assignment_date` BIGINT NULL DEFAULT NULL ,
  `time_created` BIGINT NULL DEFAULT NULL ,
  `time_updated` BIGINT NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

alter table publisher add simple_geo_json_token varchar(255);
alter table publisher add user_principal_id bigint(20);
-- TODO update publisher rows to set values for user_principal_id
-- add foreign key from publisher.user_principal_id to user_principal.id

-- create default network member
insert into network_member (id, version, user_principal_id, name, primary_email, paypal_email, member_key, description)
values(4, 0, 1, 'Oaklandly','clay@ratecred.com','clay@ratecred.com','oaklandly','Default network member for publishers');

insert into simple_geo_json_token (version, json_token, start_assignment_date, end_assignment_date)
values(
0,
'WBTyDUSAXATRBG8wGBeN6TExjv327NZW',
unix_timestamp(str_to_date('8/1/2011','%m/%d/%Y')) * 1000,
unix_timestamp(str_to_date('10/1/2011','%m/%d/%Y')) * 1000
);

insert into simple_geo_json_token (version, json_token, start_assignment_date, end_assignment_date)
values(
0,
'DrRGzTsTdGRCCjYqF2Jh6xQKwPvFTR2J',
unix_timestamp(str_to_date('10/1/2011','%m/%d/%Y')) * 1000,
unix_timestamp(str_to_date('11/1/2011','%m/%d/%Y')) * 1000
);

insert into simple_geo_json_token (version, json_token, start_assignment_date, end_assignment_date)
values(
0,
'u9ZEZ8kvS2nkjv7ekH9Gc4Q9YQQunWxu',
unix_timestamp(str_to_date('11/1/2011','%m/%d/%Y')) * 1000,
unix_timestamp(str_to_date('12/1/2011','%m/%d/%Y')) * 1000
);

insert into simple_geo_json_token (version, json_token, start_assignment_date, end_assignment_date)
values(
0,
'qPNuxP6wz7w9b3FuNCnkUvXte5CJAh2v',
unix_timestamp(str_to_date('12/1/2011','%m/%d/%Y')) * 1000,
unix_timestamp(str_to_date('1/1/2012','%m/%d/%Y')) * 1000
);
