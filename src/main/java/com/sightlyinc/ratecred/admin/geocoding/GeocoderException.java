package com.sightlyinc.ratecred.admin.geocoding;

public class GeocoderException extends Exception{

	public GeocoderException() {
		super();
	}

	public GeocoderException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeocoderException(String message) {
		super(message);
	}

	public GeocoderException(Throwable cause) {
		super(cause);
	}

}
