package com.sightlyinc.ratecred.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.dao.BaseDao;
import com.sightlyinc.ratecred.dao.EventDao;
import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.Publisher;
import com.simplegeo.client.types.Feature;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Service
public class EventServiceImpl extends AbstractTransactionalService<Event> implements EventService {

    @Autowired
    private EventDao eventDao;


    @Override
    public BaseDao<Event> getDao() {
        return eventDao;
    }


//	@Override
//	public List<Event> findByUserPrincipal(UserPrincipal up) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//    

    @Override
    public Event findByUrl(String url) {
        return eventDao.findByUrl(url);
    }
    
	
}
