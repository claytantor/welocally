package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.authentication.UserPrincipal;
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

//	@Override
//	public List<Article> findByUserPrincipal(UserPrincipal up) {
//		// TODO Auto-generated method stub
//		return null;
//	}
    
    
}
