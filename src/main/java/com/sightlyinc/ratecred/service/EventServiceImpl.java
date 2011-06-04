package com.sightlyinc.ratecred.service;

import com.sightlyinc.ratecred.dao.BaseDao;
import com.sightlyinc.ratecred.dao.EventDao;
import com.sightlyinc.ratecred.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
