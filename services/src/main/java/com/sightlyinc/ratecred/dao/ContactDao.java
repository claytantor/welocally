package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.Contact;
import com.sightlyinc.ratecred.model.Publisher;

public interface ContactDao extends BaseDao<Contact> {
    public List<Contact> findByEmailAndPublisher(String email, Publisher p);
}
