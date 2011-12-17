UPDATE `user_principal` SET `password`='3858f62230ac3c915f300c664312c63f' WHERE `user_name`='claytantor';
UPDATE `user_principal` SET `password`='3858f62230ac3c915f300c664312c63f' WHERE `user_name`='sam';
UPDATE `user_principal` SET `password`='3858f62230ac3c915f300c664312c63f' WHERE `user_name`='erin';
UPDATE `user_principal` SET `password`='3858f62230ac3c915f300c664312c63f' WHERE `user_name`='stephanie';

INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_MEMBER', 'ROLE_MEMBER', 1 );

DELETE FROM `user_principal` where `user_principal`.id>5;
DELETE FROM `role` where `principal_id`>5;

INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'oaklandly',  MD5('1a98efca9bf3493d9724c9868fe528d0'), 0, 0, 1, 1322771497259, 1322771497259 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='oaklandly';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'baypies',  MD5('89e13578ca6f4b0bb01a8cb02306eb3f'), 0, 0, 1, 1322771497260, 1322771497260 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='baypies';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'ef0e59fdf633',  MD5('9e15b4fccfd74d62ad4bc1c02c395e9b'), 0, 0, 1, 1322771497261, 1322771497261 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='ef0e59fdf633';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'c4e89e7c48dc',  MD5('990d323e0fa64f9f8a95ce79a70e7128'), 0, 0, 1, 1322771497261, 1322771497261 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='c4e89e7c48dc';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'f3770cd54068',  MD5('password'), 0, 0, 0, 1322771497261, 1322771497261 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='f3770cd54068';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'af04ea74fa43',  MD5('password'), 0, 0, 0, 1322771497261, 1322771497261 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='af04ea74fa43';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'dffd1c47fb53',  MD5('422637e658544ddda936addfe35a5dd0'), 0, 0, 1, 1322771497262, 1322771497262 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='dffd1c47fb53';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'dff3794a01ef',  MD5('password'), 0, 0, 0, 1322771497262, 1322771497262 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='dff3794a01ef';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'dece3fb0f1ee',  MD5('password'), 0, 0, 0, 1322771497262, 1322771497262 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='dece3fb0f1ee';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '1c56c6db6f74',  MD5('1a64205290b047e29e13520507cf3c32'), 0, 0, 1, 1322771497263, 1322771497263 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='1c56c6db6f74';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '95639a7f6ee2',  MD5('password'), 0, 0, 0, 1322771497263, 1322771497263 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='95639a7f6ee2';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'ff5c1dae1da1',  MD5('password'), 0, 0, 0, 1322771497263, 1322771497263 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='ff5c1dae1da1';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'ac99900081cd',  MD5('password'), 0, 0, 0, 1322771497263, 1322771497263 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='ac99900081cd';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '07643c3d38b2',  MD5('password'), 0, 0, 0, 1322771497264, 1322771497264 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='07643c3d38b2';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'e84d282fe95f',  MD5('password'), 0, 0, 0, 1322771497264, 1322771497264 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='e84d282fe95f';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '5f424a5a47c0',  MD5('password'), 0, 0, 0, 1322771497264, 1322771497264 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='5f424a5a47c0';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'eb4148ea66e5',  MD5('password'), 0, 0, 0, 1322771497265, 1322771497265 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='eb4148ea66e5';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '8f8c78f0a1ca',  MD5('b0bc5df877c9498ba608ee4dc60ff8f6'), 0, 0, 1, 1322771497265, 1322771497265 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='8f8c78f0a1ca';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '0c96199b78f5',  MD5('password'), 0, 0, 0, 1322771497265, 1322771497265 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='0c96199b78f5';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'c2edff11eb97',  MD5('cd5060ef689440aa8a03d8374100a852'), 0, 0, 1, 1322771497265, 1322771497265 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='c2edff11eb97';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'cb909b1458f3',  MD5('282d6d2248574a24ad1075512db2dd32'), 0, 0, 1, 1322771497266, 1322771497266 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='cb909b1458f3';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '9fbb3924955a',  MD5('093a2a9b99b94f65af7a499159f60bb5'), 0, 0, 1, 1322771497266, 1322771497266 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='9fbb3924955a';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '256024ba31cd',  MD5('password'), 0, 0, 0, 1322771497266, 1322771497266 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='256024ba31cd';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'f7ba856a4881',  MD5('password'), 0, 0, 0, 1322771497267, 1322771497267 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='f7ba856a4881';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'ad686c91629e',  MD5('password'), 0, 0, 0, 1322771497267, 1322771497267 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='ad686c91629e';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'getoutmagazine',  MD5('eddcf3f2402b4df4a9bdbffd284b8ea8'), 0, 0, 1, 1322771497267, 1322771497267 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='getoutmagazine';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '282ce8d71af3',  MD5('password'), 0, 0, 0, 1322771497268, 1322771497268 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='282ce8d71af3';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '02cbd22571ae',  MD5('password'), 0, 0, 0, 1322771497268, 1322771497268 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='02cbd22571ae';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'ea35d85e638a',  MD5('password'), 0, 0, 0, 1322771497268, 1322771497268 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='ea35d85e638a';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '54777b07c9d5',  MD5('7cc7c9b437c54f97b42aeb54cff6d929'), 0, 0, 1, 1322771497269, 1322771497269 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='54777b07c9d5';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'c75f3aa98ad2',  MD5('password'), 0, 0, 0, 1322771497269, 1322771497269 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='c75f3aa98ad2';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '476e17f935f8',  MD5('password'), 0, 0, 0, 1322771497269, 1322771497269 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='476e17f935f8';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'e66f83351116',  MD5('password'), 0, 0, 0, 1322771497270, 1322771497270 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='e66f83351116';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '03c9eecd6f70',  MD5('3a3b2e52fbab4872b0360038bbf034c1'), 0, 0, 1, 1322771497270, 1322771497270 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='03c9eecd6f70';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '5f03cbc99e81',  MD5('password'), 0, 0, 0, 1322771497270, 1322771497270 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='5f03cbc99e81';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'onthegreenroad',  MD5('80721deda5884eca8f8a31b1b405df57'), 0, 0, 1, 1322771497270, 1322771497270 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='onthegreenroad';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'b6fd2439d464',  MD5('password'), 0, 0, 0, 1322771497271, 1322771497271 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='b6fd2439d464';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '4da81312d2fe',  MD5('password'), 0, 0, 0, 1322771497271, 1322771497271 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='4da81312d2fe';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'f047a9677061',  MD5('password'), 0, 0, 0, 1322771497271, 1322771497271 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='f047a9677061';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '6fe6f9845d6f',  MD5('password'), 0, 0, 0, 1322771497272, 1322771497272 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='6fe6f9845d6f';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '54503203ae69',  MD5('password'), 0, 0, 0, 1322771497272, 1322771497272 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='54503203ae69';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '5f79ea33df0e',  MD5('08424a0b9a04401582dede8188b1232c'), 0, 0, 1, 1322771497272, 1322771497272 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='5f79ea33df0e';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'welocallywpdev',  MD5(''), 0, 0, 1, 1322771497272, 1322771497272 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='welocallywpdev';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '60a2a7c834ba',  MD5('password'), 0, 0, 0, 1322771497273, 1322771497273 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='60a2a7c834ba';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'bf46ba13ba51',  MD5('password'), 0, 0, 0, 1322771497273, 1322771497273 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='bf46ba13ba51';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '8326f4cc9eeb',  MD5('password'), 0, 0, 0, 1322771497273, 1322771497273 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='8326f4cc9eeb';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, 'ea566038a2c8',  MD5('password'), 0, 0, 0, 1322771497274, 1322771497274 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='ea566038a2c8';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '47d75bb7b5ba',  MD5('454ea505162d470e8d453dad6ed3df03'), 0, 0, 1, 1322771497274, 1322771497274 );
set @userPrincipalInsert = last_insert_id();
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_USER', 'ROLE_USER', @userPrincipalInsert );
INSERT INTO `role` (`version`,`role`,`role_group`,`principal_id`) VALUES ( 0, 'ROLE_PUBLISHER', 'ROLE_PUBLISHER', @userPrincipalInsert );

update `publisher` set user_principal_id=@userPrincipalInsert where key_value='47d75bb7b5ba';
INSERT INTO `user_principal` ( `version`, `user_name`, `password`, `credentials_expired`, `locked`, `enabled`, `time_created`, `time_updated`) VALUES ( 0, '5dacb8b8e493',  MD5('password'), 0, 0, 0, 1322771497274, 1322771497274 );
set @userPrincipalInsert = last_insert_id();
update `publisher` set user_principal_id=@userPrincipalInsert where key_value='5dacb8b8e493';
