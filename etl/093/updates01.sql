alter table patron add facebookId bigint(20);
alter table patron drop foreign key fk_patron_user_principal1;
alter table patron drop index fk_patron_user_principal1;
alter table patron modify user_principal_id bigint(20);

alter table award drop foreign key fk_award_award_offer;
alter table award drop index fk_award_award_offer;
alter table award modify award_offer_id bigint(20);
