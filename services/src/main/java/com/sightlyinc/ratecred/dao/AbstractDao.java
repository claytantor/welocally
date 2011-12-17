package com.sightlyinc.ratecred.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sightlyinc.ratecred.model.BaseEntity;


public abstract class AbstractDao<T> extends HibernateDaoSupport implements  BaseDao<T> {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Class<T> persistentClass;
	
    public AbstractDao(Class<T> persistentClass) {
        this.persistentClass = persistentClass;        
    }
    
    @PostConstruct
    private void initSessionFactory() {
    	super.setSessionFactory(sessionFactory);
    }

    public HibernateTemplate getHibernateTemplateOverride() {
        HibernateTemplate template = getHibernateTemplate();
        template.setFlushMode(HibernateTemplate.FLUSH_AUTO);
        return template;
    }
    
    
    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    public T findByPrimaryKey(Long id) {
        return findByPrimaryKey(id, false);
    }

    public T findByPrimaryKey(Long id, boolean lock) {
        T entity;
        if (lock) {
            entity = (T) getSession().get(getPersistentClass(), id, LockMode.UPGRADE);
        } else {
            entity = (T) getSession().get(getPersistentClass(), id);
        }

        return entity;
    }

    public List<T> findAllData() {
        return findByCriteria();
    }
    
    public List<T> findAll() {
        return getHibernateTemplateOverride().loadAll(this.persistentClass);
    }

    public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }
    
    /**
     * we are having to do a little work to get this to behave the way we want
     * kindof feel like this should not be required
     */
    public Long save(T entity) {
    	
    	if(entity instanceof BaseEntity) {
    		       	
        	if(((BaseEntity)entity).getId() == null) {
        		getSession().persist(entity);
        		return ((BaseEntity)entity).getId();
        	}
        	else
        		return ((BaseEntity)getSession().merge(entity)).getId();
    		
    	}
    	else
    		return (Long)getSession().save(entity);
    	
    	
    }
    
    public void delete(T entity) {
    	getSession().delete(entity);
    }

    public T makePersistent(T entity) {
        getSession().saveOrUpdate(entity);
        return entity;
    }

    public void makeTransient(T entity) {
        getSession().delete(entity);
    }

    public void flush() {
        getSession().flush();
    }

    public void clear() {
        getSession().clear();
    }

    public T merge(T entity) {
        return (T) getSession().merge(entity);
    }
    
    public void persist(T entity) {
        getSession().persist(entity);
    }

    /**
     * Use this inside subclasses as a convenience method.
     */
    @SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        Set<T> objs = new LinkedHashSet(crit.list());
        return new ArrayList<T>(objs);
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
	
	

}
