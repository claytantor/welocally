delete from ratecred_080_etl.award_offer c where c.id not in ( SELECT b.id FROM ratecred_080_etl.award a,ratecred_080_etl.award_offer b where a.award_offer_id=b.id   ) 

#expire old style offers
update ratecred_080_etl.award_offer set expire_millis=1288915200000 where award_offer.id < 2600
