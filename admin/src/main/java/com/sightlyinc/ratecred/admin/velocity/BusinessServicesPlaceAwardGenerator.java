package com.sightlyinc.ratecred.admin.velocity;

import java.util.Map;

import org.apache.log4j.Logger;

import com.noi.utility.velocity.ModelBasedGenerator;

public class BusinessServicesPlaceAwardGenerator extends ModelBasedGenerator {
	static Logger logger = Logger.getLogger(BusinessServicesPlaceAwardGenerator.class);

	public BusinessServicesPlaceAwardGenerator(Map model) {
		
		
		try{
			super.initVelocityContext(this.getClass());
			super.setModel(model);
		}	
		catch(Exception e)
		{
			logger.error("cannot intialize template engine", e);
		}	
	}

}
