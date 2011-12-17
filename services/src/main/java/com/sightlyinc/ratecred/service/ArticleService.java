package com.sightlyinc.ratecred.service;

import com.sightlyinc.ratecred.model.Article;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface ArticleService extends BaseService<Article> {
	
//	public List<Article> findByUserPrincipal(UserPrincipal up);

    public Article findByUrl(String url);
}
