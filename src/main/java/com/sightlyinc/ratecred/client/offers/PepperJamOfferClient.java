package com.sightlyinc.ratecred.client.offers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

/**
 * URL url = new URL("http", "feeds.pepperjamnetwork.com", 80, "/coupon/download/?affiliate_id=59161&program_ids=259-1687-1801-2110-2708-2803-3456-4750-4826-5173");			
 * @author claygraham
 *
 */
public class PepperJamOfferClient implements OfferClient {
	
	static Logger logger = 
		Logger.getLogger(PepperJamOfferClient.class);
	
	private String protocol;
	
	private Integer port;
	
	private String host;
	
	private String endpoint;
	
	private String sourceName;

	@Override
	public List<Offer> getOffers() throws OfferFeedException {
		try {
			URL url = new URL(protocol, host, port, endpoint);
			java.net.URLConnection c = url.openConnection();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(c.getInputStream(), baos);
			StringReader sreader = new StringReader(new String(baos.toByteArray()));
			CSVReader reader = new CSVReader(sreader);
			//skip the first line
			reader.readNext();
			ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
			strat.setType(Offer.class);
			String[] columns = new String[] {"externalId","programId","programName","name","couponCode","description","url","beginDateString","expireDateString"}; // the fields to bind do in your JavaBean		
			strat.setColumnMapping(columns);
			CsvToBean csv = new CsvToBean();
			List<Offer> list = csv.parse(strat, reader);
			for (Offer offer : list) 
				offer.setExternalSource(sourceName);
			return list;
			
		} catch (MalformedURLException e) {
			logger.error("problem",e);
			throw new OfferFeedException(e);
		} catch (IOException e) {
			logger.error("problem",e);
			throw new OfferFeedException(e);
		}
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
	
	

}
