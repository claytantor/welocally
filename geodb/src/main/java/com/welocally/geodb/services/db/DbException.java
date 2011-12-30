package com.welocally.geodb.services.db;

public class DbException extends Exception {
	
	public enum Type { OBJECT_NOT_FOUND, DB_ERROR };
	
	private Type exceptionType;

	public DbException(Type t) {
		super();
		this.exceptionType = t;
	}

	public DbException(Type t,Throwable arg1) {
		super(arg1);
		this.exceptionType = t;
	}

	public Type getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(Type exceptionType) {
		this.exceptionType = exceptionType;
		
	}

	@Override
	public String getMessage() {
		switch(exceptionType){
		case OBJECT_NOT_FOUND:{
			return "the requested object was not found";
		}
		case DB_ERROR:{
			return "there was an error in the database";
		}
		}
		
		return super.getMessage();
	}

	

}
