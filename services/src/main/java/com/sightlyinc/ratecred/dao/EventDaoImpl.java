package com.sightlyinc.ratecred.dao;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Event;

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

    @Override
    public Event findByUrl(String url) {
        return (Event) getCurrentSession().createCriteria(Event.class).add(Restrictions.eq("url", url)).uniqueResult();
    }
}
