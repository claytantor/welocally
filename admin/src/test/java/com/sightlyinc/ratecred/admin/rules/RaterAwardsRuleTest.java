package com.sightlyinc.ratecred.admin.rules;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.drools.FactException;
import org.drools.IntegrationException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseLoader;
import org.xml.sax.SAXException;

import com.noi.utility.web.UrlUtils;
import com.sightlyinc.ratecred.admin.model.CityStateEvaluator;
import com.sightlyinc.ratecred.admin.model.RaterAwards;
import com.sightlyinc.ratecred.admin.mvc.controller.TestRulesController;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.Rating;


public class RaterAwardsRuleTest extends TestCase{
	
	static Logger logger = Logger.getLogger(RaterAwardsRuleTest.class);
	
	private String ratingRulesUrl = "http://media.ratecred.com.s3.amazonaws.com/dev/data/rater_awards.java.drl";
	
	public void testRaterAwards() {
		logger.debug("testRaterAwards");
		try {
			
/*			RuleBase ruleBase = 
				RuleBaseLoader.loadFromInputStream(
						TestRulesController.class.getResourceAsStream("/rules/rater_awards.java.drl"));
*/			

			/*RuleBase ruleBase = RuleBaseLoader
			.loadFromInputStream(TestRulesController.class
					.getResourceAsStream("/rules/rater_awards.java.drl"));*/

			RuleBase ruleBase = RuleBaseLoader.loadFromUrl(new URL(ratingRulesUrl));			
			
			WorkingMemory workingMemory = ruleBase.newWorkingMemory( );
			boolean dynamic = true;
			
			RaterAwards raclaytantor = new RaterAwards(
					getRaterByName("/com/sightlyinc/ratecred/admin/rules/claytantor.json"));
			RaterAwards rashowymilkweed = new RaterAwards(
					getRaterByName("/com/sightlyinc/ratecred/admin/rules/showymilkweed.json"));			
			RaterAwards r3 = new RaterAwards(
					getRaterByName("/com/sightlyinc/ratecred/admin/rules/ElijahIsMe.json"));			
			List<Patron> allRaters = new ArrayList<Patron>();
			allRaters.add(raclaytantor.getRater());
			allRaters.add(rashowymilkweed.getRater());
			allRaters.add(r3.getRater());
			
			Map<String,PlaceCityState> allcs = new HashMap<String,PlaceCityState>();
						
			//now get a set of all cities
			addCitiesToMap(getCitiesRated(raclaytantor.getRater()), allcs);
			addCitiesToMap(getCitiesRated(rashowymilkweed.getRater()), allcs);
			addCitiesToMap(getCitiesRated(r3.getRater()), allcs);
						
			for (PlaceCityState placeCityState : allcs.values()) {
				CityStateEvaluator cseval = new CityStateEvaluator(placeCityState, allRaters);
				workingMemory.assertObject( cseval, dynamic );
			}
									
			workingMemory.assertObject( raclaytantor, dynamic );
			workingMemory.assertObject( rashowymilkweed, dynamic );
			workingMemory.assertObject( r3, dynamic );
						
			workingMemory.fireAllRules( );	
			logger.debug("claytantor");
			logAwards(raclaytantor);
			logger.debug("showymilkweed");
			logAwards(rashowymilkweed);
			logger.debug("ElijahIsMe");
			logAwards(r3);
						
			//assertEquals(17, raclaytantor.getAwards().size());
			//assertEquals(16, rashowymilkweed.getAwards().size());
			//assertEquals(2, r3.getAwards().size());
			
			
		} catch (IntegrationException e) {
			logger.error("IntegrationException", e);
			fail("IntegrationException");
		} catch (FactException e) {
			logger.error("FactException", e);
			fail("FactException");
		} catch (SAXException e) {
			logger.error("SAXException", e);
			fail("SAXException");
		} catch (IOException e) {
			logger.error("IOException", e);
			fail("IOException");
		} 
	}
	
	//imageUrl=/images/awards/award_citykey.png&city=St.+Helena&state=CA
	public void testRaterUrlDecode() {
		String metadata = "?imageUrl=/images/awards/award_citykey.png&city=St.+Helena&state=CA";
		try {
			List<NameValuePair> nvs = 
				UrlUtils.parse(new URI(metadata), "UTF-8");
			
			assertEquals(3, nvs.size());
			
		} catch (URISyntaxException e) {
			logger.error("URISyntaxException", e);
			fail("URISyntaxException");
		}
	}
	
	
	private void logAwards(RaterAwards ra)
	{
		logger.debug("===== awards ======");
		for (Award a : ra.getRater().getAwards()) {
			logger.debug(a.getAwardType().getKeyname()+" "+a.getMetadata());
		}
		
		logger.debug("===== awards to give ======");
		for (Award a : ra.getAwards()) {
			logger.debug(a.getAwardType().getKeyname()+" "+a.getMetadata());
		}
		
		logger.debug("===== awards to take ======");
		for (Award a : ra.getRemoveAwards()) {
			logger.debug(a.getAwardType().getKeyname()+" "+a.getMetadata());
		}
		logger.debug("=====");
	}
	
	private Patron getRaterByName(String name) 
	throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();		
		Patron r = mapper.readValue(RaterAwardsRuleTest.class.getResourceAsStream(name), Patron.class);
		return r;

	}
	
	private void addCitiesToMap(List<PlaceCityState> cities, Map<String, PlaceCityState> maplookup)
	{
		for (PlaceCityState placeCityState : cities) {
			maplookup.put(
					placeCityState.getCity().toLowerCase()+placeCityState.getState().toLowerCase(), 
					placeCityState);
		}
	}
	
	private List<PlaceCityState> getCitiesRated(Patron r)
	{
		List<PlaceCityState> allcs = new ArrayList<PlaceCityState>();
		for (Rating rating : r.getRatings()) {
			allcs.add(
					new PlaceCityState(
							rating.getPlace().getCity(), 
							rating.getPlace().getState(),
							null));
		}
			
		return allcs;
	}

}
