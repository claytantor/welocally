SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

--user_pricipal
LOCK TABLES `user_principal` WRITE;
/*!40000 ALTER TABLE `user_principal` DISABLE KEYS */;
INSERT INTO `user_principal` VALUES 
(1,0,'claytantor','foobar','clay@ratecred.com',0,0,1,'c0a7120a-2847-43b1-bdbb-dbfcf027bc84','USER',101,'claytantor','twitter_token','twitter_secret','twitter_verify','',1307330419279,1307330419279),
(2,0,'sam','foobar','sam@ratecred.com',0,0,1,'1e5bbd8e-ffd0-4ba6-a9b6-bf37f1f6af63','USER',102,'sam','twitter_token','twitter_secret','twitter_verify','',1307330419279,1307330419279),
(3,0,'erin','foobar','erin@oaklandgrown.org',0,0,1,'a15721e0-e19a-40cb-a13a-c1da06473823','USER',NULL,NULL,NULL,NULL,NULL,NULL,1307330419279,1307330419279),
(4,0,'stephanie','foobar','showymilkweed@gmail.com',0,0,1,'929edd2d-db62-4ca5-8a9f-44f18501c6fe','USER',NULL,NULL,NULL,NULL,NULL,NULL,1307330419279,1307330419279);
/*!40000 ALTER TABLE `user_principal` ENABLE KEYS */;
UNLOCK TABLES;

--role
LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES 
(1602,0,'ROLE_USER','GROUP_USER',1),
(1603,0,'ROLE_ADMIN','GROUP_ADMIN',1),
(1604,0,'ROLE_USER','GROUP_USER',2),
(1605,0,'ROLE_MEMBER','GROUP_MEMBER',2),
(1606,0,'ROLE_PUBLISHER','GROUP_PUBLISHER',2),
(1607,0,'ROLE_MEMBER','ROLE_MEMBER',3),
(1608,0,'ROLE_USER','ROLE_USER',3),
(1609,0,'ROLE_PUBLISHER','ROLE_PUBLISHER',3);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--network_member
LOCK TABLES `network_member` WRITE;
/*!40000 ALTER TABLE `network_member` DISABLE KEYS */;
INSERT INTO `network_member` VALUES 
(1,0,2,'samssite llc','samsite@bozos.com','samspay@bozos.com','dbfcf027bc84','sam is good',1307330419279,1307330419279),
(2,0,3,'oakland merchants council','council@oaklandgrown.org','payment@oaklandgrown.org','c1da06473823','good baby,good baby do',1307641613145,NULL),
(3,0,4,'stephanie','showymilweed@gmail.com','showymilweed@gmail.com','c1da06473823','good baby,good baby do',1307641613145,NULL);
/*!40000 ALTER TABLE `network_member` ENABLE KEYS */;
UNLOCK TABLES;

--publisher
LOCK TABLES `publisher` WRITE;
/*!40000 ALTER TABLE `publisher` DISABLE KEYS */;
INSERT INTO `publisher` VALUES (1,0,'http://samsite.com','sams site','great site for free stuff','great site for free stuff','','',1000,1,1307330419279,1307330419279),
(2,0,'http://oaklandgrown.org','oakland grown','Oakland Grown is a movement celebrating and supporting Oakands locally-owned, independent businesses and artists.','Its about making your hard-earned money go further here at home, creating a sustainable economy, and helping to preserve the unique Oakland you love. Individuals, businesses, artists and organizations can all join the movement.','http://media.welocally.com.s3.amazonaws.com/assets/c1da06473823/oaklandgrown/icon256.png','http://media.welocally.com.s3.amazonaws.com/assets/c1da06473823/oaklandgrown/icon64.png',1000,2,1307641613005,1307641613005);
/*!40000 ALTER TABLE `publisher` ENABLE KEYS */;
UNLOCK TABLES;

--places
LOCK TABLES `place` WRITE;
/*!40000 ALTER TABLE `place` DISABLE KEYS */;
INSERT INTO `place` VALUES (1,0,'Lukas Tap Room','2221 Broadway','Oakland','CA','94612',NULL,'SG_53l3Ylfbpp2jqyYi0zqoxX_37.810938_-122.267166@1293134755','(510) 451-4677',37.811359,-122.26701,'great place for beers','','','','',NULL,NULL,NULL,1307330419279,1307330419279);
/*!40000 ALTER TABLE `place` ENABLE KEYS */;
UNLOCK TABLES;

--
--  `review`
--
LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (1,0,2,1,'Getting Your Beer On','Duh. I dont think I have seen so many beers spread out on a menu. Now im not quite the beer connoisseur just yet (although it may be very likely I will never be), but Im quite certain the beer lover would have a blast at this place. I went with the safe hefeweizen, and it was delish as always.',NULL,'Gone once for casual drinks, and felt like I was being led through airport security to get a booth. Gone once for lunch and had a pleasant time and an awesome burger.  This time, was an after-work birthday celebration -- probably 20 of us at a table at the side room. A gathering that large challenges even the best service. But the staff was on-point. Drinks, eats came. Conviviality flowed. Risque tales were divulged.','http://bit.ly/dkfKp5',1307641658917,NULL);
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES (1,0,2,1,'College Bound Brotherhood Event','The young men who participated in the ceremony were given a $100 stipend along with a first class celebration. Karen Bevels catered the banquet portion of the event and soul food was definitely on the menu. There were chicken strips, greens and macaroni and cheese. The vibe was extremely positive as predominately young black people milled around the room in business attire and dress clothes. The scene stood in stark contrast to the murderous war torn Oakland, which is consistently depicted in the media. ','College Bound Brotherhood recognizes its latest graduates at Oakland event (Community Voices)','http://bit.ly/dkfKp5','\0',1307725776209,NULL);
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (1,0,2,1,'Beer Dinner',NULL,'1307330419279','http://bit.ly/i1xwgC',40,'Come down to Lukas and have drinks with the beer master.','','',1307330419279,1307330419279,NULL,NULL,NULL,100000,'foobar',1357330419279,NULL,1307729656356,NULL);
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
