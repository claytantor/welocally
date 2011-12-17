
delete from award_type where keyname='tattlerati';

ALTER TABLE award_type ADD COLUMN previous VARCHAR(45) AFTER value;
ALTER TABLE award_type ADD COLUMN next VARCHAR(45) AFTER previous;
ALTER TABLE award_type ADD COLUMN category VARCHAR(45) AFTER next;

DELETE from award_type where id>80 and id<106;
INSERT INTO `award_type` VALUES 
(90,'Starter','all',0,'2010-09-26 00:00:00',1,'Has taken the first step','starter',NULL, 25, NULL, 'novice', 'rank'),
(100,'Novice','all',0,'2010-09-26 00:00:00',1,'Has rated at least ten times','novice',NULL, 25,'starter','advisor', 'rank'),
(101,'Advisor','all',0,'2010-09-26 00:00:00',1,'Has rated at least twenty times','advisor',NULL, 30,'novice','leader', 'rank'),
(102,'Leader','all',0,'2010-09-26 00:00:00',1,'Has rated at least 40 times','leader',NULL, 50,'advisor','expert', 'rank'),
(103,'Expert','all',0,'2010-09-26 00:00:00',1,'Has rated at least 100 times','expert',NULL, 75,'leader','elite', 'rank'),
(104,'Elite','all',0,'2010-09-26 00:00:00',1,'Has rated at least 150 times','elite',NULL, 100,'expert','raterati', 'rank'),
(105,'Raterati','all',0,'2010-09-26 00:00:00',1,'Has rated at least 200 times','raterati',NULL, 200,'elite',NULL, 'rank');

update award_type set category='basic' where keyname='explorer';
update award_type set category='basic' where keyname='fot';
update award_type set category='basic' where keyname='kicker';
update award_type set category='basic' where keyname='magellan';
update award_type set category='basic' where keyname='boe';
update award_type set category='basic' where keyname='star';
update award_type set category='basic' where keyname='superstar';
update award_type set category='basic' where keyname='citykey';
update award_type set category='basic' where keyname='beta';
update award_type set category='rating_rank' where keyname='starter';
update award_type set category='rating_rank' where keyname='novice';
update award_type set category='rating_rank' where keyname='advisor';
update award_type set category='rating_rank' where keyname='leader';
update award_type set category='rating_rank' where keyname='expert';
update award_type set category='rating_rank' where keyname='elite';
update award_type set category='rating_rank' where keyname='raterati';
