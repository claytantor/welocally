package com.welocally.geodb.services.db;

public class DbException extends Exception {

	public DbException() {
		super();
	}

	public DbException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DbException(String arg0) {
		super(arg0);
	}

	public DbException(Throwable arg0) {
		super(arg0);
	}

}
