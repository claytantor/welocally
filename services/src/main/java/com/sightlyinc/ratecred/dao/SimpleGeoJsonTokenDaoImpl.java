package com.sightlyinc.ratecred.dao;

import com.sightlyinc.ratecred.model.SimpleGeoJsonToken;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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
