package com.sightlyinc.ratecred.dao;

import com.sightlyinc.ratecred.model.Event;
import org.springframework.stereotype.Repository;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Repository
public class EventDaoImpl extends AbstractDao<Event> implements EventDao {
    public EventDaoImpl() {
        super(Event.class);
    }
}
