package com.sightlyinc.ratecred.etl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.noi.utility.velocity.ModelBasedGenerator;
import com.sightlyinc.ratecred.service.PublisherService;

@Component
public class PublisherToSiteMigrate_20120127191918 {
	
	static Logger logger = 
		Logger.getLogger(PublisherToSiteMigrate_20120127191918.class);

	
	@Autowired PublisherService publisherService; 

	
	public String migrate(){
		logger.debug("migrate");
		
		Map<String, Object> model = new HashMap<String, Object>();

		try {
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
				rows.add(row);				
			}		
			model.put("rows", rows);
			
			con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		PublisherToSiteGenerator_20120127191918 generator = 
			new PublisherToSiteGenerator_20120127191918(model);
		return generator.makeDisplayString();
	}
	
	class PublisherToSiteGenerator_20120127191918 extends ModelBasedGenerator {
		

		public PublisherToSiteGenerator_20120127191918(Map model) {
				
			try{
				super.initVelocityContext(this.getClass());
				super.setModel(model);
			}	
			catch(Exception e)
			{
				logger.error("cannot intialize template engine", e);
			}	
		}

	}

}
