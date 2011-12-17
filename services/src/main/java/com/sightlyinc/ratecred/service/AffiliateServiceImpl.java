package com.sightlyinc.ratecred.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sightlyinc.ratecred.dao.BaseDao;
import com.sightlyinc.ratecred.dao.AffiliateDao;
import com.sightlyinc.ratecred.model.Affiliate;

/**
 * Class javadoc comment here...
 *
 * @author clay
 * @version $Id$
 */
@Service
public class AffiliateServiceImpl extends AbstractTransactionalService<Affiliate> implements AffiliateService {

    @Autowired
    private AffiliateDao affiliateDao;

    @Override
    public BaseDao<Affiliate> getDao() {
        return affiliateDao;
    }

//	@Override
//	public List<Affiliate> findByUserPrincipal(UserPrincipal up) {
//		return null;
//	}
//    

    @Override
    public Affiliate findByUrl(String url) {
        return affiliateDao.findByUrl(url);
    }
}
