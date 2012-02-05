
package com.sightlyinc.ratecred.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Contact;

@Repository
public class ContactDaoImpl 
	extends AbstractDao<Contact> 
	implements ContactDao {

	static Logger logger = Logger.getLogger(ContactDaoImpl.class);
    
   
    
	public ContactDaoImpl() {
		super(Contact.class);
	}


    	
	  
}
