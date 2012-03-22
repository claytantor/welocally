package com.sightlyinc.ratecred.service;

import java.util.List;

import com.sightlyinc.ratecred.model.Contact;
import com.sightlyinc.ratecred.model.Publisher;

public interface ContactService extends BaseService<Contact> {
	
    public List<Contact> findByEmailAndPublisher(String email, Publisher p);

}
