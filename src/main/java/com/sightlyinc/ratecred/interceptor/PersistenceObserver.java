package com.sightlyinc.ratecred.interceptor;

import com.sightlyinc.ratecred.model.BaseEntity;

public interface PersistenceObserver {
	public void inform(PersistenceActivity activity); 
}
