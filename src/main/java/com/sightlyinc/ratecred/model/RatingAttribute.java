package com.sightlyinc.ratecred.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.noi.utility.string.StringUtils;

public class RatingAttribute {
	
	private Long id;
	private String name;
	private String type;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		try {
			buf.append("[");
			Map<String,String> description = 
				BeanUtils.describe(this);
			
			for (String key : description.keySet()) {
				buf.append("[");
				buf.append(key);
				buf.append("=");
				if(!key.equals("ratings"))
				{					
					if(description.get(key) != null && 
							StringUtils.isNotEmpty(description.get(key).toString()))
						buf.append(description.get(key).toString());
				}
				buf.append("]");
			}
			buf.append("]");
			
		} catch (IllegalAccessException e) {
			
		} catch (InvocationTargetException e) {
			
		} catch (NoSuchMethodException e) {
			
		}
		
		return buf.toString();
	}

}
