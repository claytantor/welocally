/*
 * make sure every order has a product associated;
 */
update cust_order set product_id=1 where product_id is null;

/*
 * pro is product.id = 2
 * premium is product.id = 3
 */
UPDATE `product` SET `production_payment_code`='XWWLNJ7UET2MQ' WHERE `id`='2';
UPDATE `product` SET `production_payment_code`='LQW9L2MVGU5V4' WHERE `id`='3';
UPDATE `product` SET `production_payment_code`='C665AFFGNQSZ8' WHERE `id`='4';

UPDATE `item_sku` SET `price`='5.00' WHERE `id`='6';
UPDATE `item_sku` SET `price`='249.00' WHERE `id`='5';
UPDATE `item_sku` SET `price`='59.00' WHERE `id`='4';

UPDATE `product_line` SET `quantity`=10 WHERE `id`='3';
UPDATE `product_line` SET `quantity`=1000 WHERE `id`='5';

/* roles for products  */
ALTER TABLE `product` ADD COLUMN `roles` VARCHAR(255) NULL  AFTER `notes` ;
UPDATE `product` SET `roles`='WL_PLACES_BASIC' WHERE `id`='1';
UPDATE `product` SET `roles`='WL_PLACES_PRO' WHERE `id`='2';
UPDATE `product` SET `roles`='WL_PLACES_PREMIUM' WHERE `id`='3';