package com.sightlyinc.ratecred.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sightlyinc.ratecred.dao.BaseDao;
import com.sightlyinc.ratecred.dao.ReviewDao;
import com.sightlyinc.ratecred.model.Review;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Service
public class ReviewServiceImpl extends AbstractTransactionalService<Review> implements ReviewService {
    @Autowired
    private ReviewDao reviewDao;

    @Override
    public BaseDao<Review> getDao() {
        return reviewDao;
    }

//	@Override
//	public List<Review> findByUserPrincipal(UserPrincipal up) {
//		// TODO Auto-generated method stub
//		return null;
//	}
    
    
}
