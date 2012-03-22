
package com.sightlyinc.ratecred.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Contact;
import com.sightlyinc.ratecred.model.Publisher;

@Repository
public class ContactDaoImpl 
	extends AbstractDao<Contact> 
	implements ContactDao {

	static Logger logger = Logger.getLogger(ContactDaoImpl.class);
    
   
    
	public ContactDaoImpl() {
		super(Contact.class);
	}



    @Override
    public List<Contact> findByEmailAndPublisher(String email, Publisher p) {
        Query q = getCurrentSession().createQuery(
                "select entityimpl from "+Contact.class.getSimpleName()+" as entityimpl " +
                        "where entityimpl.email = :email " +
                        "and entityimpl.publisher.id = :publisherId");
        q.setString("email", email);
        q.setLong("publisherId", p.getId());
        return q.list();
    }



 
    	
	  
}
