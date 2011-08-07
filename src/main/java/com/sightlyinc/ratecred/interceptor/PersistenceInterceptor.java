package com.sightlyinc.ratecred.interceptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.sightlyinc.ratecred.client.geo.GeoPersistable;
import com.sightlyinc.ratecred.client.geo.GeoPersistenceException;
import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import com.sightlyinc.ratecred.model.BaseEntity;

public class PersistenceInterceptor extends EmptyInterceptor {
	
	protected List<PersistenceActivity> createAudits = new ArrayList<PersistenceActivity>();
	protected List<PersistenceActivity> updateAudits = new ArrayList<PersistenceActivity>();
	protected List<PersistenceActivity> deleteAudits = new ArrayList<PersistenceActivity>();

	
	/**
	 * serializable
	 * 
	 */
	private static final long serialVersionUID = 8916039542608648118L;

	static Logger logger = Logger.getLogger(PersistenceInterceptor.class);
	
	private int deletes;
	private int updates;
    private int creates;
    private int loads;
    
    private PersistenceObserver persistenceInformerListener;
    

    public PersistenceInterceptor() {
		super();
		logger.debug("contructor");
	}

	public void onDelete(Object entity,
                         Serializable id,
                         Object[] state,
                         String[] propertyNames,
                         Type[] types) {
        // do nothing for now
        if ( entity instanceof BaseEntity ) {
            
            //inform?
            if(entity.getClass().isAnnotationPresent(PersistenceObservable.class)){
                PersistenceActivity activity = new PersistenceActivity();
                activity.setActivity(PersistenceActivity.ACTIVITY_DELETE);
                activity.setClazzName(entity.getClass().getName());
                activity.setEntityId(((BaseEntity) entity).getId());
                activity.setEntity((BaseEntity) entity);
                if (entity instanceof GeoPersistable) {
                    // need to set member key to construct layer in listener to delete record from simplegeo
                    try {
                        activity.setMemberKey(((GeoPersistable)entity).getMemberKey());
                    } catch (GeoPersistenceException e) {
                        logger.error("Could not get member key of geo persistable entity when intercepting delete", e);
                    }
                }
                deleteAudits.add(activity);
            }

            deletes++;
        }
    }

    public boolean onFlushDirty(Object entity,
                                Serializable id,
                                Object[] currentState,
                                Object[] previousState,
                                String[] propertyNames,
                                Type[] types) {
    	if ( entity instanceof BaseEntity ) {
    		
    		//inform?
    		if(entity.getClass().isAnnotationPresent(PersistenceObservable.class)){
    			PersistenceActivity activity = new PersistenceActivity();
        		activity.setActivity(PersistenceActivity.ACTIVITY_UPDATE);
        		activity.setClazzName(entity.getClass().getName());
        		//activity.setEntityId(((BaseEntity)entity).getId());
        		activity.setEntity((BaseEntity)entity);
        		updateAudits.add(activity);
    		}
    		
    		updates++;
    		for (int i = 0; i < propertyNames.length; i++) {
				if ("timeUpdated".equals(propertyNames[i])) {
					currentState[i] = Calendar.getInstance().getTimeInMillis();
					return true;
				}
			}
    		   		
    	}
    	
        return false;
    }

    public boolean onLoad(Object entity,
                          Serializable id,
                          Object[] state,
                          String[] propertyNames,
                          Type[] types) {
        if ( entity instanceof BaseEntity ) {
            loads++;
        }
        return false;
    }

    public boolean onSave(Object entity,
                          Serializable id,
                          Object[] state,
                          String[] propertyNames,
                          Type[] types) {

    	if ( entity instanceof BaseEntity ) {
    		
    		//inform?
    		if(entity.getClass().isAnnotationPresent(PersistenceObservable.class)){
    			PersistenceActivity activity = new PersistenceActivity();
        		activity.setActivity(PersistenceActivity.ACTIVITY_CREATE);
        		activity.setClazzName(entity.getClass().getName());
        		activity.setEntity((BaseEntity)entity);
        		createAudits.add(activity);
    		}
    		
    	
			creates++;
			for (int i = 0; i < propertyNames.length; i++) {
				if ("timeCreated".equals(propertyNames[i])) {
					state[i] = Calendar.getInstance().getTimeInMillis();
					return true;
				}
			}

    	}
    	
        return false;
    }

    public void afterTransactionCompletion(Transaction tx) {
        if ( tx.wasCommitted() && (creates >0 || updates>0 || loads>0 || deletes>0) ) {
            logger.debug("Creations: " + creates + ", Updates: " + updates+ " Loads: " + loads + ", Deletes: " + deletes);
        }
        deletes=0;
        updates=0;
        creates=0;
        loads=0;
    }

	public void setPersistenceInformerListener(
			PersistenceObserver persistenceInformerListener) {
		this.persistenceInformerListener = persistenceInformerListener;
	}
	
	/**
	 * if saving you should do it here so it will be transaction based, was
	 * seriously thinking of making this final but I am not a code nazi but you
	 * should think hard before overriding this method, this commit is within
	 * its own transaction. there is some consideration on if the audits should
	 * be within the transaction or after of it
	 * 
	 */
	@Override
	public void postFlush(Iterator entities) {


		// creates, we need to do just a little 
		// gymnastics because the id is assigned on flush
		for (PersistenceActivity audit : createAudits) {
			if(audit.getEntity() != null) {
				audit.setEntityId(((BaseEntity)audit.getEntity()).getId());
			}
			persistenceInformerListener.inform(audit);
			//saveAudit(audit);
		}

		// updates
		for (PersistenceActivity audit : updateAudits) {
			if(audit.getEntity() != null) {
				audit.setEntityId(((BaseEntity)audit.getEntity()).getId());
			}
			persistenceInformerListener.inform(audit);
		}
		// deletes
		for (PersistenceActivity audit : deleteAudits) {
			if(audit.getEntity() != null) {
				audit.setEntityId(((BaseEntity)audit.getEntity()).getId());
			}
			persistenceInformerListener.inform(audit);
		}

		updateAudits.clear();
		createAudits.clear();
		deleteAudits.clear();
		

	}



    
    

}
