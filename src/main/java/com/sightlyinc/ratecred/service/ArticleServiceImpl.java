package com.sightlyinc.ratecred.service;

import com.sightlyinc.ratecred.dao.ArticleDao;
import com.sightlyinc.ratecred.dao.BaseDao;
import com.sightlyinc.ratecred.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
