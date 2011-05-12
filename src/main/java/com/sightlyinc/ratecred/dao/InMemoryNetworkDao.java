package com.sightlyinc.ratecred.dao;

import java.io.IOException;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import au.com.bytecode.opencsv.CSVReader;

import com.noi.utility.data.DigestUtils;
import com.noi.utility.net.ClientResponse;
import com.noi.utility.net.SimpleHttpClient;
import com.sightlyinc.ratecred.model.Network;



@Component
public class InMemoryNetworkDao implements NetworkDao {

	@Value("${networks.spreadsheetUrl}")
	private String sitesUrl="http://spreadsheets.google.com/tq?tqx=out:csv&key=0Au9a580BQZPYdFl2YU5nRkJhNFZhZmJaLWlkc1F0Nnc&hl=en";

	private List<Network> networks;
	
	@PostConstruct
	private void initData() {
		try {
			ClientResponse responseSites = 
				SimpleHttpClient.get(sitesUrl, null, null);
			String hash = DigestUtils.makeDigest(responseSites.getResponse());

			networks = new ArrayList<Network>();
			StringReader sreader = new StringReader(new String(responseSites.getResponse()));
			CSVReader reader = new CSVReader(sreader);
			reader.readNext();   				
			String [] nextLine;
			
			//clear sites
			networks = new ArrayList<Network>();
			while ((nextLine = reader.readNext()) != null) {
				//networks.add(convertSite(nextLine));
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
