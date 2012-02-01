package com.sightlyinc.ratecred.etl;


public class CustomClassLoader extends ClassLoader {
	private static CustomClassLoader _instance;
	
	private CustomClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	public Package[] getPackages(){
		return super.getPackages();
	}
	
	public static CustomClassLoader getInstance(){
		if(_instance==null)
			_instance = new CustomClassLoader(ClassLoader.getSystemClassLoader());
		return _instance;
	}
}
