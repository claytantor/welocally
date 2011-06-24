package com.sightlyinc.ratecred.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sightlyinc.ratecred.dao.ArticleDao;
import com.sightlyinc.ratecred.dao.BaseDao;
import com.sightlyinc.ratecred.model.Article;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Service
public class ArticleServiceImpl extends AbstractTransactionalService<Article> implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Override
    public BaseDao<Article> getDao() {
        return articleDao;
    }

//	@Override
//	public List<Article> findByUserPrincipal(UserPrincipal up) {
//		return null;
//	}
//    

    @Override
    public Article findByUrl(String url) {
        return articleDao.findByUrl(url);
    }
}
