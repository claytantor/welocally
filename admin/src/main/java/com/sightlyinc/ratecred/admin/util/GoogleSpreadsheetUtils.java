package com.sightlyinc.ratecred.admin.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

import com.noi.utility.net.ClientResponse;
import com.noi.utility.net.SimpleHttpClient;
import com.noi.utility.string.StringUtils;

public class GoogleSpreadsheetUtils {
	
	public static List<String[]> getSpreadsheetData(String spreadsheetUrl) throws IOException {
		
		List<String[]> lines = new ArrayList<String[]>();
		
		ClientResponse response = 
			SimpleHttpClient.get(spreadsheetUrl, null, null);
		StringReader sreader = new StringReader(new String(response.getResponse()));
		CSVReader reader = new CSVReader(sreader);
		reader.readNext();				
		
		String [] offerLine;
	    while ((offerLine = reader.readNext()) != null) {
	    	if(!StringUtils.isEmpty(offerLine[0]))
	    		lines.add(offerLine);
	    }
	    return lines;
	    
	}

}
