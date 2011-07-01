package com.sightlyinc.ratecred.dao;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Merchant;

/**
 * Class javadoc comment here...
 *
 * @author clay
 * @version $Id$
 */
@Repository
public class MerchantDaoImpl extends AbstractDao<Merchant> implements MerchantDao {
    public MerchantDaoImpl() {
        super(Merchant.class);
    }
    
    @Override
    public Merchant findByUrl(String url) {
        return (Merchant) getCurrentSession().createCriteria(Merchant.class).add(Restrictions.eq("url", url)).uniqueResult();
    }
}
