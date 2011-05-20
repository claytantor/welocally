package com.sightlyinc.ratecred.admin.harvest;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.webharvest.definition.DefinitionResolver;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.xml.sax.InputSource;

import com.noi.utility.data.FileToString;
import com.noi.utility.hibernate.GUIDGenerator;
import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.admin.geocoding.Geocoder;
import com.sightlyinc.ratecred.admin.geocoding.GeocoderException;
import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.pojo.Events;
import com.sightlyinc.ratecred.pojo.Location;
import com.thoughtworks.xstream.XStream;

@Component("ebeEventHarvester")
public class EbeEventHarvester implements EventHarvester {
	
	static Logger logger = Logger.getLogger(EbeEventHarvester.class);

	@Value("${eventHarvester.config}")
	private String harvestConfig = "/harvest/ebe03_events.xml";
	
	@Value("${eventHarvester.harvestWorkingDir}")
	private String harvestWorkingDir = "/Users/claygraham/data/tmp/harvest";	
	
	@Autowired 
	private Geocoder geocoder;

	public Events havestEvents() {
		InputStream is = EbeEventHarvester.class.getResourceAsStream(harvestConfig);
		InputSource in = new InputSource(is);

		String outfile = GUIDGenerator.createId()+".xml";
		String xml = makeEventsXmlFile(in, harvestWorkingDir, outfile);
		Events e = null;
		if (!StringUtils.isEmpty(xml))
			e = makeEventsFromXml(xml);

		return e;
	}

	public String makeEventsXmlFile(InputSource configFile, String targetDir,
			String outFile) {
		DefinitionResolver
				.registerPlugin("com.sightlyinc.ratecred.admin.harvest.plugin.GracefulHttpPlugin");

		ScraperConfiguration config = new ScraperConfiguration(configFile);

		Scraper scraper = new Scraper(config, targetDir);

		scraper.setDebug(true);
		scraper.addVariableToContext("outFile", outFile);

		scraper.execute();
		return FileToString.readFileToSingleString(targetDir + "/" + outFile);

	}

	public Events makeEventsFromXml(String xml) {
		XStream xstream = new XStream();
		xstream.alias("events", Events.class);
		xstream.alias("event", Event.class);
		xstream.alias("place", Place.class);
		xstream.addImplicitCollection(Events.class, "events");
		Events allEvents = (Events) xstream.fromXML(xml);
		for (Event event : allEvents.getEvents()) {
			try {
				Location placeLocation = 
					geocoder.geocode(event.getPlace().getAddressFull());
				if(placeLocation != null)
				{
					event.getPlace().setAddress(placeLocation.getAddressOne());
					event.getPlace().setCity(placeLocation.getCity());
					event.getPlace().setState(placeLocation.getState());
					event.getPlace().setZip(placeLocation.getPostalCode());
					event.getPlace().setLatitude(placeLocation.getLat());
					event.getPlace().setLongitude(placeLocation.getLng());
				}
				
			} catch (GeocoderException e) {
				logger.error(e);
			} 
			
		}
		return allEvents;
	}

}
