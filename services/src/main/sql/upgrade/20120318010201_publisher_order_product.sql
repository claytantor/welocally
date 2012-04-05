
update cust_order set product_id=1 where product_id is null;


UPDATE `product` SET `production_payment_code`='XWWLNJ7UET2MQ' WHERE `id`='2';
UPDATE `product` SET `production_payment_code`='LQW9L2MVGU5V4' WHERE `id`='3';
UPDATE `product` SET `production_payment_code`='C665AFFGNQSZ8' WHERE `id`='4';

UPDATE `item_sku` SET `price`='5.00' WHERE `id`='6';
UPDATE `item_sku` SET `price`='249.00' WHERE `id`='5';
UPDATE `item_sku` SET `price`='59.00' WHERE `id`='4';

UPDATE `product_line` SET `quantity`=10 WHERE `id`='3';
UPDATE `product_line` SET `quantity`=1000 WHERE `id`='5';

ALTER TABLE `product` ADD COLUMN `roles` VARCHAR(255) NULL  AFTER `notes` ;
UPDATE `product` SET `roles`='WL_PLACES_BASIC' WHERE `id`='1';
UPDATE `product` SET `roles`='WL_PLACES_PRO' WHERE `id`='2';
UPDATE `product` SET `roles`='WL_PLACES_PREMIUM' WHERE `id`='3';


INSERT INTO `item_sku`
(
	`version`,
	`item_type`,
	`status`,
	`sku_code`,
	`price`,
	`currency_code`,
	`name`,
	`description`,
	`notes`,
	`is_active`,
	`period`,
	`time_created`,
	`time_updated`)
VALUES
(
	0,
	'LICENSE',
	'AVAILABLE',
	'35a51050',
	0.0,
	'USD',
	'Site License',
	'Allows a single site to use the subscription.',
	'',
	1,
	31104000000,
	1314673289280,
	1314673289280
);

set @site_sku = LAST_INSERT_ID();

INSERT INTO `product_line`
(`version`,
`quantity`,
`status`,
`is_active`,
`product_id`,
`item_sku_id`,
`time_created`,
`time_updated`)
VALUES
(
0,
1,
'AVAILABLE',
1,
1,
@site_sku,
1314673289280,
1314673289280
);

INSERT INTO `product_line`
(`version`,
`quantity`,
`status`,
`is_active`,
`product_id`,
`item_sku_id`,
`time_created`,
`time_updated`)
VALUES
(
0,
2,
'AVAILABLE',
1,
2,
@site_sku,
1314673289280,
1314673289280
);

INSERT INTO `product_line`
(`version`,
`quantity`,
`status`,
`is_active`,
`product_id`,
`item_sku_id`,
`time_created`,
`time_updated`)
VALUES
(
0,
1000,
'AVAILABLE',
1,
3,
@site_sku,
1314673289280,
1314673289280
);

update `cust_order` set status = 'CANCELLED' where status = 'CANCELED';
