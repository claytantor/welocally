package com.sightlyinc.ratecred.dao;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Affiliate;

/**
 * Class javadoc comment here...
 *
 * @author clay
 * @version $Id$
 */
@Repository
public class AffiliateDaoImpl extends AbstractDao<Affiliate> implements AffiliateDao {
    public AffiliateDaoImpl() {
        super(Affiliate.class);
    }
    
    @Override
    public Affiliate findByUrl(String url) {
        return (Affiliate) getCurrentSession().createCriteria(Affiliate.class).add(Restrictions.eq("url", url)).uniqueResult();
    }
}
