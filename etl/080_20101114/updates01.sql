INSERT INTO user (Host,User,Password) VALUES('10.210.37.193','ratecred',PASSWORD('Rammy56Tally'));
FLUSH PRIVILEGES;
INSERT INTO user (Host,User,Password) VALUES('10.254.98.161','ratecred',PASSWORD('Rammy56Tally'));
FLUSH PRIVILEGES;
INSERT INTO user (Host,User,Password) VALUES('10.192.67.175','ratecred',PASSWORD('Rammy56Tally'));
FLUSH PRIVILEGES;
GRANT ALL ON ratecred_080_etl.* TO 'ratecred'@'10.210.37.193';
FLUSH PRIVILEGES;
GRANT ALL ON ratecred_080_etl.* TO 'ratecred'@'10.254.98.161';
FLUSH PRIVILEGES;
GRANT ALL ON ratecred_080_etl.* TO 'ratecred'@'10.192.67.175';
FLUSH PRIVILEGES;




