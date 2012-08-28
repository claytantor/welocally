
package com.sightlyinc.ratecred.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.NetworkMember;

@Repository("networkMemberDao")
public class NetworkMemberDaoDefaultImpl 
extends AbstractDao<NetworkMember>  
	implements NetworkMemberDao {

	static Logger logger = Logger.getLogger(NetworkMemberDaoDefaultImpl.class);
    
    public NetworkMemberDaoDefaultImpl() {
		super(NetworkMember.class);
	}

//	@Override
//	public NetworkMember findByUserPrincipal(final UserPrincipal up) {
//		
//		return (NetworkMember)getHibernateTemplateOverride().execute(new HibernateCallback() {
//			public Object doInHibernate(Session session)
//			throws HibernateException, SQLException 
//			{
//
//				Query query = session.createQuery(
//					"select distinct entityimpl from "+NetworkMember.class.getName()+
//					" as entityimpl where entityimpl.userPrincipal = :userPrincipal");
//				
//				query.setEntity("userPrincipal", up);
//				
//				NetworkMember t = (NetworkMember)query.uniqueResult();
//							
//				return t;
//	
//			}
//		});		
//	}	
    
    
	  
}
