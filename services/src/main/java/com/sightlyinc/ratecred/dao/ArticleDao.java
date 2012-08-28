package com.sightlyinc.ratecred.dao;

import com.sightlyinc.ratecred.model.Article;

public interface ArticleDao extends BaseDao<Article> {
	
	//public List<Article> findByUserPrincipal(UserPrincipal up);

    public Article findByUrl(String url);
}
