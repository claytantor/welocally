
delete from award_type where keyname='tattlerati';

ALTER TABLE award_type ADD COLUMN previous VARCHAR(45) AFTER value;
ALTER TABLE award_type ADD COLUMN next VARCHAR(45) AFTER previous;

DELETE from award_type where id>80 and id<106;
INSERT INTO `award_type` VALUES 
(90,'Starter','all',0,'2010-09-26 00:00:00',1,'Has taken the first step','starter',NULL, 25, NULL, 'novice'),
(100,'Novice','all',0,'2010-09-26 00:00:00',1,'Has rated at least ten times','novice',NULL, 25,'starter','advisor'),
(101,'Advisor','all',0,'2010-09-26 00:00:00',1,'Has rated at least twenty times','advisor',NULL, 30,'novice','leader'),
(102,'Leader','all',0,'2010-09-26 00:00:00',1,'Has rated at least 40 times','leader',NULL, 50,'advisor','expert'),
(103,'Expert','all',0,'2010-09-26 00:00:00',1,'Has rated at least 100 times','expert',NULL, 75,'leader','elite'),
(104,'Elite','all',0,'2010-09-26 00:00:00',1,'Has rated at least 150 times','elite',NULL, 100,'expert','raterati'),
(105,'Raterati','all',0,'2010-09-26 00:00:00',1,'Has rated at least 200 times','raterati',NULL, 200,'elite',NULL);
