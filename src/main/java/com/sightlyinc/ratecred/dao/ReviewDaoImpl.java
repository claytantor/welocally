package com.sightlyinc.ratecred.dao;

import com.sightlyinc.ratecred.model.Review;
import org.springframework.stereotype.Repository;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Repository
public class ReviewDaoImpl extends AbstractDao<Review> implements ReviewDao {
    public ReviewDaoImpl() {
        super(Review.class);
    }
}
