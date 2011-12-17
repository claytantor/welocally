package com.sightlyinc.ratecred.dao;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Merchant;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Place;

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
        return (Merchant) getCurrentSession().createCriteria(Merchant.class).add(
        		Restrictions.eq("url", url)).uniqueResult();
    }

	@Override
	public Merchant findByNameAndMember(String name, NetworkMember member) {
		
		Query q = getCurrentSession().createQuery(
				"select distinct entityimpl from "+Merchant.class.getSimpleName()+
				" as entityimpl" +
				" where entityimpl.networkMember = :member and" +
				" entityimpl.name = :name");
		q.setEntity("member", member);
		q.setString("name", name);
		return (Merchant)q.uniqueResult();
	}
    
    
}
