package com.sightlyinc.ratecred.service;

import com.sightlyinc.ratecred.model.Article;
import com.sightlyinc.ratecred.model.Merchant;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface MerchantService extends BaseService<Merchant> {

    public Merchant findByUrl(String url);
}
