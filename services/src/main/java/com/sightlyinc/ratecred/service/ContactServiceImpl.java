package com.sightlyinc.ratecred.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sightlyinc.ratecred.dao.BaseDao;
import com.sightlyinc.ratecred.dao.ContactDao;
import com.sightlyinc.ratecred.model.Contact;
import com.sightlyinc.ratecred.model.Publisher;

@Service
public class ContactServiceImpl extends AbstractTransactionalService<Contact>
		implements ContactService {
	
	@Autowired
	private ContactDao contactDao; 

	@Override
	public BaseDao<Contact> getDao() {
		return contactDao;
	}

	

    @Override
    public List<Contact> findByEmailAndPublisher(String email, Publisher p) {
        return contactDao.findByEmailAndPublisher(email,p);
    }






}
