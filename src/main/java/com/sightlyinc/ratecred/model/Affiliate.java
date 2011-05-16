package com.sightlyinc.ratecred.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 



 * 
 * @author claygraham
 *
 */
@Entity
@Table(name="affiliate")
public class Affiliate extends BaseEntity {
	
	 @ManyToMany
	 @JoinTable(name="affiliate_has_business", joinColumns=@JoinColumn(name="affiliate_id"), inverseJoinColumns=@JoinColumn(name="business_id"))
	 private Set<Business> businesses;
	 
	 @ManyToOne
	 @JoinColumn(name = "network_member_id")
	 private NetworkMember networkMember;
	 
	 @OneToMany(mappedBy = "affiliate")
	 private Set<OfferEconomics> offerEconomics;
}
