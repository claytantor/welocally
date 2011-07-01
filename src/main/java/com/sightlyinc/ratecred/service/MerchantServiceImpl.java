package com.sightlyinc.ratecred.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sightlyinc.ratecred.dao.BaseDao;
import com.sightlyinc.ratecred.dao.MerchantDao;
import com.sightlyinc.ratecred.model.Merchant;

/**
 * Class javadoc comment here...
 *
 * @author clay
 * @version $Id$
 */
@Service
public class MerchantServiceImpl extends AbstractTransactionalService<Merchant> implements MerchantService {

    @Autowired
    private MerchantDao merchantDao;

    @Override
    public BaseDao<Merchant> getDao() {
        return merchantDao;
    }

//	@Override
//	public List<Merchant> findByUserPrincipal(UserPrincipal up) {
//		return null;
//	}
//    

    @Override
    public Merchant findByUrl(String url) {
        return merchantDao.findByUrl(url);
    }
}
