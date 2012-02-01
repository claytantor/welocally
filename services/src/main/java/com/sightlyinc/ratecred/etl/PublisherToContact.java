package com.sightlyinc.ratecred.etl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Table publisher
===============
id, version, url, site_name, key_value, description, summary, 
icon_url, map_icon_url, monthly_pageviews, network_member_id, 
time_created, time_updated, json_token, user_principal_id, 
service_end_date, subscription_status


---------------

 * @author claygraham
 *
 */
public class PublisherToContact {
	
	static Logger logger = 
		Logger.getLogger(PublisherToContact.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			Document doc = newDocumentFromInputStream(
					PublisherToContact.class.getResourceAsStream(
							"/etl_log4j.xml"));
			
			DOMConfigurator.configure(doc.getDocumentElement());			
			
			Connection con = DriverManager.getConnection(
			    "jdbc:mysql://localhost/ratecred", "root",
			    "root23");

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM publisher");
			List<String[]> rows = new ArrayList<String[]>();
			
			while (rs.next()) {
				
				String[] row = new String[] {
						rs.getString("site_name"),
						rs.getString("description"),
						rs.getString("url"),
						rs.getString("id")	
				};
				
				String line = 
					"INSERT INTO `site` ( `version`, `name`,  `description`, `url`, `is_active`, " +
					"`publisher_id`, `time_created`, `time_updated` ) "+
					"VALUES ( 0, '[site_name]', '[description]', '[url]', 1, [publisher_id], 1314673289280, 1314673289280 );";
				
				line = line.replace("[site_name]", row[0]);
				line = line.replace("[description]", row[1]);
				line = line.replace("[url]", row[2]);
				line = line.replace("[publisher_id]", row[3]);

				System.out.println(line);

				rows.add(row);				
			}		
		
			
			con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static Document newDocumentFromInputStream(InputStream in) {
	    DocumentBuilderFactory factory = null;
	    DocumentBuilder builder = null;
	    Document ret = null;

	    try {
	      factory = DocumentBuilderFactory.newInstance();
	      builder = factory.newDocumentBuilder();
	    } catch (ParserConfigurationException e) {
	      e.printStackTrace();
	    }

	    try {
	      ret = builder.parse(new InputSource(in));
	    } catch (SAXException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    return ret;
	  }

}
