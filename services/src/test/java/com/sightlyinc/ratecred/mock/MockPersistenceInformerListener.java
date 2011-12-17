package com.sightlyinc.ratecred.mock;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.sightlyinc.ratecred.interceptor.PersistenceActivity;
import com.sightlyinc.ratecred.interceptor.PersistenceObserver;

@Component("persistenceInformerListener")
public class MockPersistenceInformerListener implements PersistenceObserver {
	
	static Logger logger = 
		Logger.getLogger(MockPersistenceInformerListener.class);

	@Override
	public void inform(PersistenceActivity activity) {
		logger.debug("mock send:"+activity.getClazzName());		
	}


}
