package com.sightlyinc.ratecred.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.SimpleGeoJsonToken;

/**
 * @author sam
 * @version $Id$
 */
@Repository("simpleGeoJsonTokenDao")
public class SimpleGeoJsonTokenDaoImpl extends AbstractDao<SimpleGeoJsonToken> implements SimpleGeoJsonTokenDao {

    public SimpleGeoJsonTokenDaoImpl() {
        super(SimpleGeoJsonToken.class);
    }

    @Override
    public SimpleGeoJsonToken getCurrentToken() {
        long today = new Date().getTime();
        List<SimpleGeoJsonToken> tokens = findByCriteria(
                Restrictions.and(
                        Restrictions.le("startAssignmentDate", today),
                        Restrictions.gt("endAssignmentDate", today)
                )
        );
        return (tokens.isEmpty() ? null : tokens.get(0));
    }
}
