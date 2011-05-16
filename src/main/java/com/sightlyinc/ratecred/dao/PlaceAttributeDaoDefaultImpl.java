
package com.sightlyinc.ratecred.dao;

import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.PlaceAttribute;

@Repository("placeAttributeDao")
public class PlaceAttributeDaoDefaultImpl 
extends AbstractDao<PlaceAttribute> 
	implements PlaceAttributeDao {    
      
	public PlaceAttributeDaoDefaultImpl() {
		super(PlaceAttribute.class);
	}	  
}
