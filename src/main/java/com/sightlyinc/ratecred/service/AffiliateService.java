package com.sightlyinc.ratecred.service;

import com.sightlyinc.ratecred.model.Affiliate;

/**
 * Class javadoc comment here...
 *
 * @author clay
 * @version $Id$
 */
public interface AffiliateService extends BaseService<Affiliate> {

    public Affiliate findByUrl(String url);
}
