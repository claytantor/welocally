ALTER TABLE award_offer 
ADD COLUMN external_id VARCHAR(45) AFTER status,
ADD COLUMN external_source VARCHAR(45) AFTER external_id, 
ADD COLUMN program_id VARCHAR(45) AFTER external_source,
ADD COLUMN program_name VARCHAR(128) AFTER program_id,
ADD COLUMN code VARCHAR(45) AFTER program_name,
ADD COLUMN url LONGTEXT AFTER code,
ADD COLUMN begin_millis BIGINT(20) AFTER url,
ADD COLUMN expire_millis BIGINT(20) AFTER begin_millis;

