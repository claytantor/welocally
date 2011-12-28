package com.welocally.geodb.services.spatial;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mongodb.MongoException;

@Component
public class SpatialExamples {
	
	@Value("${SpatialExamples.examplesFile:/data/placesAU.geojson}")
	private String examplesFile;
	
	static Logger logger = 
		Logger.getLogger(SpatialExamples.class);
	
	public static final Point SCHWAMMENDINGEN_8051 = new Point(47.4008593, 8.5781373, "Schwammedingen");
    public static final Point SEEBACH_8052 = new Point(47.4232860, 8.5422655, "Seebach");
    public static final Point KNONAU_8934 = new Point(47.2237640, 8.4611790, "Knonau");
    public static final Point ZUERICH_8000 = new Point(47.3690239, 8.5380326, "ZŸrich in 8000");
    public static final Point EBIKON_6030_6031 = new Point(47.0819237, 8.3415740, "Ebikon");
    public static final Point ADLISWIL_8134 = new Point(47.3119892, 8.5256064, "Adliswil");
    public static final Point BAAR_6341 = new Point(47.1934110, 8.5230670, "Baar");
    
    public static void createExampleLocations(final IndexWriter w)
        throws Exception {
        final SpatialHelper s = new SpatialHelper(10.0d, 20.0d);
        s.addLoc(w, EBIKON_6030_6031.getName(), EBIKON_6030_6031);
        s.addLoc(w, SEEBACH_8052.getName(), SEEBACH_8052);
        s.addLoc(w, ZUERICH_8000.getName(), ZUERICH_8000);
        s.addLoc(w, SCHWAMMENDINGEN_8051.getName(), SCHWAMMENDINGEN_8051);
        s.addLoc(w, ADLISWIL_8134.getName(), ADLISWIL_8134);
        s.addLoc(w, KNONAU_8934.getName(), KNONAU_8934);
        s.addLoc(w, BAAR_6341.getName(), BAAR_6341);
        
        // uncomment these lines to generate 10'000 locations near by Zuerich
		for (int i = 0; i < 10; i++) {
			s.addLoc(w, "ZŸrich_" + i, new Point(ZUERICH_8000.getLat() + i
					/ 100000D, ZUERICH_8000.getLon() + i / 100000D));
		}
               
        w.commit();
                
    }
    
	public void createExampleLocationsGeo(SpatialHelper spatialHelper, IndexWriter writer)
			throws Exception {
		try {
			logger.debug(examplesFile);

			InputStream i4 = SpatialExamples.class.getResourceAsStream(
			"/data/placesAU.geojson");		
			
			InputStreamReader reader = 
				new InputStreamReader(i4);
			
			BufferedReader br = new BufferedReader(reader); 
			String str = null; 
			while((str = br.readLine()) != null) { 
				
				JSONObject place = 
					new JSONObject(str);
				
				JSONObject properties = place.getJSONObject("properties");
				JSONObject geom = place.getJSONObject("geometry");
				JSONArray coords = geom.getJSONArray("coordinates");
				Point coord = 
					new Point(
							Double.parseDouble(coords.getString(1)), 
							Double.parseDouble(coords.getString(0)));
				spatialHelper.addLoc(writer, properties.getString("name"), coord);
				
			} 
			reader.close(); 
				
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		
		

	}
	
	


}
