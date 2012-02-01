/*-- free product --*/
INSERT INTO `product`(
	`version`,
	`is_active`,
	`status`,
	`product_sku`,
	`production_payment_code`,
	`sandbox_payment_code`,
	`name`,
	`description`,
	`notes`,
	`time_created`,
	`time_updated`)
VALUES
(
	0,
	1,
	'AVAILABLE',
	'f102b14a87a1',
	'',
	'',
	'Welocally Places Basic',
	'Free Welocally Places',
	'',
	1314673289280,
	1314673289280
);
SET @product_id = LAST_INSERT_ID();

INSERT INTO `product_line`
(
`version`,
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
0,
'AVAILABLE',
1,
@product_id,
3,
1314673289280,
1314673289280
);


/*--- 
 * 
 *sandbox 
<form action="https://www.sandbox.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="C6HQJ8CZBM5LY">
<input type="image" src="https://www.sandbox.paypal.com/en_US/i/btn/btn_buynowCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
<img alt="" border="0" src="https://www.sandbox.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
</form>

prod
<form action="https://www.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="S6AM242HFENUS">
<input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_buynowCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
<img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1">
</form>

 * 
 * ---
 */

INSERT INTO `product`(
	`version`,
	`is_active`,
	`status`,
	`product_sku`,
	`production_payment_code`,
	`sandbox_payment_code`,
	`name`,
	`description`,
	`notes`,
	`time_created`,
	`time_updated`)
VALUES
(
	0,
	1,
	'AVAILABLE',
	'6703a12c97a5',
	'S6AM242HFENUS',
	'C6HQJ8CZBM5LYp',
	'Welocally Places Pro',
	'Our value product.',
	'',
	1314673289280,
	1314673289280
);
SET @product_id = LAST_INSERT_ID();

INSERT INTO `product_line`
(
`version`,
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
@product_id,
4,
1314673289280,
1314673289280
);

INSERT INTO `product_line`
(
`version`,
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
3,
'AVAILABLE',
1,
@product_id,
6,
1314673289280,
1314673289280
);


/*--- 
 * 
sandbox 
<form action="https://www.sandbox.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="565E6M77KN8MY">
<input type="image" src="https://www.sandbox.paypal.com/en_US/i/btn/btn_buynowCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
<img alt="" border="0" src="https://www.sandbox.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
</form>

prod
<form action="https://www.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="3PB63CF4VP286">
<input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_buynowCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
<img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1">
</form>

 * 
 * ---
 */

INSERT INTO `product`(
	`version`,
	`is_active`,
	`status`,
	`product_sku`,
	`production_payment_code`,
	`sandbox_payment_code`,
	`name`,
	`description`,
	`notes`,
	`time_created`,
	`time_updated`)
VALUES
(
	0,
	1,
	'AVAILABLE',
	'8d5e6f6587a9',
	'3PB63CF4VP286',
	'565E6M77KN8MY',
	'Welocally Places Premium',
	'All our products and support, with unlimited sites for a full year.',
	'',
	1314673289280,
	1314673289280
);
SET @product_id = LAST_INSERT_ID();

INSERT INTO `product_line`
(
`version`,
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
@product_id,
5,
1314673289280,
1314673289280
);

INSERT INTO `product_line`
(
`version`,
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
100,
'AVAILABLE',
1,
@product_id,
6,
1314673289280,
1314673289280
);

/*--- 
 * 
sandbox 
<form target="paypal" action="https://www.sandbox.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="M9CXL9LYQ8PZY">
<input type="image" src="https://www.sandbox.paypal.com/en_US/i/btn/btn_cart_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
<img alt="" border="0" src="https://www.sandbox.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
</form>

prod
<form target="paypal" action="https://www.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="L4P6RR58T2Z2U">
<input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_cart_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
<img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1">
</form>

 * 
 * ---
 */

INSERT INTO `product`(
	`version`,
	`is_active`,
	`status`,
	`product_sku`,
	`production_payment_code`,
	`sandbox_payment_code`,
	`name`,
	`description`,
	`notes`,
	`time_created`,
	`time_updated`)
VALUES
(
	0,
	1,
	'AVAILABLE',
	'12df206e7ae7',
	'L4P6RR58T2Z2U',
	'M9CXL9LYQ8PZY',
	'Welocally Support Incident',
	'Get the support you need on a pay as you go basis.',
	'',
	1314673289280,
	1314673289280
);
SET @product_id = LAST_INSERT_ID();

INSERT INTO `product_line`
(
`version`,
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
@product_id,
6,
1314673289280,
1314673289280
);

update cust_order set status='CANCELED' where status='subscr_cancel';
update cust_order set status='SUBSCRIBED' where status='subscr_signup';



