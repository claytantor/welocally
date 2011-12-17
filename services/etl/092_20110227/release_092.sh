#!/bin/sh
# environment set externally
# DUMPS_DIR - where dumps will go
# ETL_DIR - where all the etl scripts live
#
# args
# $1 = original db
# $2 = target db
# $3 = db user
# $4 = db password
# $5 = client host
# $6 = app_user

original_db=$1
target_db=$2
db_user=$3
db_password=$4
client_host=$5
app_user=$6

#dump all databases and systems
suffix=$(date +%s)  # The "+%s" option to 'date' is GNU-specific.

#backup the source db
db_backups=$DUMPS_DIR
db_dump=$db_backups/$original_db-$suffix.sql
mysqldump -h localhost -u $db_user -p$db_password $original_db>$db_dump


#create the target db 
echo "create the target db"
qry1="DROP DATABASE IF EXISTS $target_db; create database $target_db;"
mysql -h localhost -u $db_user -p$db_password << eof
$qry1
eof

#now load the dump
echo "now load the dump"
mysql -h localhost -u $db_user -p$db_password $target_db<$db_dump

#fix the tx id for gowalla
echo "fix the tx id for gowalla"
qry2="ALTER TABLE rating MODIFY COLUMN checkin_gowalla VARCHAR(254) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL;"
mysql -h localhost -u $db_user -p$db_password $target_db<< eof
$qry2
eof

#give the app user the proper authority to the new database
echo "give the app user the proper authority to the new database"
qry3="GRANT ALL ON $target_db.* TO '$app_user'@'$client_host'; FLUSH PRIVILEGES;"
mysql -h localhost -u $db_user -p$db_password $target_db<< eof
$qry3
eof

#run the simple geo change on target
echo "run the simple geo change on target"
mysql -h localhost -u $db_user -p$db_password $target_db<$ETL_DIR/sg_model_changes_20110403.sql

#this is NOT adility, basic offers fields
echo "generic offers fields"
mysql -h localhost -u $db_user -p$db_password $target_db<$ETL_DIR/update_20110220_adility.sql

