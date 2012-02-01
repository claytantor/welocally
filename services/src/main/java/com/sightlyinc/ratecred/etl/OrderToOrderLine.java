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

public class OrderToOrderLine {
	
	static Logger logger = 
		Logger.getLogger(OrderToOrderLine.class);

	/**
	 * 
	 * Table cust_order
		================
		id, version, offer_id, patron_id, publisher_id, external_id, external_txid, 
		channel, buyer_key, external_payer_id, buyer_email, shipping_name, address_one, 
		address_two, city, state, postal_code, country_code, external_orderitem, sku, 
		title, description, price, quantity, status, time_created, time_updated
		---------------

	 * 
	 * Table cust_order_line
		=====================
		id, version, qty_orig, qty_used, start_time, end_time, status, cust_order_id, 
		item_sku_id, time_created, time_updated
		---------------------

	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			Document doc = newDocumentFromInputStream(
					OrderToOrderLine.class.getResourceAsStream(
							"/etl_log4j.xml"));
			
			DOMConfigurator.configure(doc.getDocumentElement());			
			
			Connection con = DriverManager.getConnection(
			    "jdbc:mysql://localhost/ratecred", "root",
			    "root23");

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM cust_order");
			
			while (rs.next()) {
				Long orderId = rs.getLong("id");
				Long timeCreated = 1314673289280l;
				Long timeEnding = rs.getLong("time_created");
				String status = rs.getString("status");

				//if the have a cancelled or key assigned give them the free product
				Long itemSkuId = null;
				if(status.equals("subscr_cancel")){
					itemSkuId = new Long(3);
				} else if(status.equals("subscr_signup")){
					itemSkuId = new Long(4);
					timeEnding = timeCreated+31104000000l;
				}
				
		
				String line = "INSERT INTO `cust_order_line` ( `version`, `qty_orig`, " +
						"`qty_used`, `start_time`, `end_time`, `status`, `cust_order_id`, " +
						"`item_sku_id`, `time_created`, `time_updated`) "+
						"VALUES ( 0, 1, 1, [start_time], " +
						"[end_time], 'AVAILABLE', [cust_order_id], [item_sku_id], " +
						"[time_created], [time_updated] );";
				
				
				line = line.replace("[start_time]", timeCreated.toString());
				line = line.replace("[end_time]", timeEnding.toString());
				line = line.replace("[cust_order_id]", orderId.toString());
				line = line.replace("[item_sku_id]", itemSkuId.toString());
				line = line.replace("[time_created]", timeCreated.toString());
				line = line.replace("[time_updated]", timeCreated.toString());

				System.out.println(line);
				
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
