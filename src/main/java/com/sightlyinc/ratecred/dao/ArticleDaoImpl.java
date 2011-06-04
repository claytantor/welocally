package com.sightlyinc.ratecred.dao;

import com.sightlyinc.ratecred.model.Article;
import org.springframework.stereotype.Repository;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Repository
public class ArticleDaoImpl extends AbstractDao<Article> implements ArticleDao {
    public ArticleDaoImpl() {
        super(Article.class);
    }
}
