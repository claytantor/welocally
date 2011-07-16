package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import au.com.bytecode.opencsv.CSVReader;

import com.noi.utility.net.ClientResponse;
import com.noi.utility.net.SimpleHttpClient;
import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.authentication.UserNotFoundException;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.authentication.UserPrincipalServiceException;
import com.sightlyinc.ratecred.client.geo.GeoPersistenceException;
import com.sightlyinc.ratecred.client.geo.SimpleGeoPlaceManager;
import com.sightlyinc.ratecred.model.Merchant;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.service.MerchantService;
import com.sightlyinc.ratecred.service.NetworkMemberService;
import com.sightlyinc.ratecred.service.PlaceManagerService;
import com.simplegeo.client.SimpleGeoPlacesClient;
import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.FeatureCollection;

@Controller
@RequestMapping("/association/merchant")
public class MerchantController {

    static Logger logger = Logger.getLogger(MerchantController.class);
    
	public static String sgoauthkey = "ZdGSMVGmne9ccTn6dykyGffHU8AXCAaC";
	
	public static String sgoauthsecret = "kmbYEGBVbhA6473Y2ms3SwMS5SYYuWux";
	
	@Autowired
	@Qualifier("locationPlacesClient")
	private SimpleGeoPlaceManager simpleGeoPlaceManager ;
	
	@Autowired
	private PlaceManagerService placeManagerService ;

    @Autowired
    private MerchantService merchantService;
    
    @Autowired
	private UserPrincipalService userPrincipalService;
    
	@Autowired
	private NetworkMemberService networkMemberService;
	
	@ModelAttribute("member")
    public NetworkMember getPublisherMember() {
		UserDetails details = 
			(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
		try {			
			UserPrincipal principal = userPrincipalService.loadUser(details.getUsername());
			return networkMemberService.findMemberByUserPrincipal(principal);		
		} catch (UserPrincipalServiceException e) {
			logger.error("", e);
			return null;
		} catch (UserNotFoundException e) {
			logger.error("", e);
			return null;
		}
    }

	
    @RequestMapping(method = RequestMethod.GET)
    public String addMerchant(@ModelAttribute("member") NetworkMember member, Model model) {
        //set the member
        Merchant merchant = new Merchant();
        merchant.setNetworkMember(member);

        model.addAttribute("merchantForm",merchant);

        return "merchant/edit";
	        
    }

    @RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
    public String editMerchant(@PathVariable Long id, Model model) {
        logger.debug("edit");
        Merchant a = merchantService.findByPrimaryKey(id);
        //model.addAttribute("publisher", a.getPublisher());
        model.addAttribute("member", a.getNetworkMember());
        model.addAttribute("merchantForm", a);
        return "merchant/edit";
    }

    @RequestMapping(method=RequestMethod.POST)
    public String saveMerchant(@Valid Merchant merchant) {

        Long id = merchantService.save(merchant);
        return "redirect:/association/merchant/"+id.toString();

    }
	
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public String getMerchantById(@PathVariable Long id, Model model) {
        logger.debug("view");
        Merchant a = merchantService.findByPrimaryKey(id);
        //model.addAttribute("publisher", a.getPublisher());
        model.addAttribute("member", a.getNetworkMember());
        model.addAttribute("merchant", a);
        return "merchant/view";
    }

    @RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
    public String deleteMerchant(@PathVariable Long id) {
        logger.debug("delete");
        Merchant merchant = merchantService.findByPrimaryKey(id);
        merchantService.delete(merchant);
        return "redirect:/association/merchant/list";
    }
    
    @RequestMapping(value="/load/{key}", method=RequestMethod.GET)
    
    public String loadMerchantsFromGoogle(
    		@PathVariable String key, 
    		@ModelAttribute("member") NetworkMember member, 
    		Model model) 
    {
        logger.debug("load");        
        String spreadsheetUrl = "https://spreadsheets.google.com/tq?tqx=out:csv&key="+key+"&hl=en";
        
        try {
			List<String[]> rows = getSpreadsheetData(spreadsheetUrl);
			findAndSaveMerchantsAsTable(rows, member);
			return "redirect:/association/merchant/list";
		} catch (IOException e) {
			model.addAttribute("error", e); 
			return "error";
		} catch (JSONException e) {
			model.addAttribute("error", e); 
			return "error";
		} catch (GeoPersistenceException e) {
			model.addAttribute("error", e); 
			return "error";
		} catch (BLServiceException e) {
			model.addAttribute("error", e); 
			return "error";
		}
        
        
    }
	
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public String list(@ModelAttribute("member") NetworkMember member, Model model) {
        logger.debug("list");

        model.addAttribute("merchants", member.getMerchants());
        return "merchant/list";

    }
    
    
    protected List<String[]> getSpreadsheetData(String spreadsheetUrl) throws IOException {
		
		List<String[]> lines = new ArrayList<String[]>();
		
		ClientResponse response = 
			SimpleHttpClient.get(spreadsheetUrl, null, null);
		StringReader sreader = new StringReader(new String(response.getResponse()));
		CSVReader reader = new CSVReader(sreader);
		reader.readNext();	
		reader.readNext();	
		
		
		String [] offerLine;
	    while ((offerLine = reader.readNext()) != null) {
	    	if(!StringUtils.isEmpty(offerLine[0]))
	    		lines.add(offerLine);
	    }
	    return lines;
	    
	}
	
	

	protected void findAndSaveMerchantsAsTable(List<String[]> lines, NetworkMember member)
			throws IOException, JSONException, GeoPersistenceException, BLServiceException {

		StringBuffer places = new StringBuffer();
		//-122.265698, 37.811373
		double lat = 37.811373;
		double lon = -122.265698;
		
		
		double radiusInKMeters = 10.00;

		SimpleGeoPlacesClient client = SimpleGeoPlacesClient.getInstance();
		client.getHttpClient().setToken(sgoauthkey, sgoauthsecret);

		String line = null; // not declared within while loop
		/*
		 * readLine is a bit quirky : it returns the content of a line MINUS the
		 * newline. it returns null only for the END of the stream. it returns
		 * an empty String if two newlines appear in a row.
		 */
		for (String[] fields : lines) {

			Long recordId = Long.parseLong(fields[0]);
			String status = fields[1].trim();
			String placeName = fields[2].trim();
			String city = fields[3].trim();
			String state = fields[4].trim();
			String category = fields[5].trim();
			String description = fields[6].trim();
			String url = fields[7].trim();
			String facebookUrl = fields[8].trim();
			
			logger.debug("trying to load place:"+placeName);
			
			if (status.equalsIgnoreCase("ACTIVE")) {

				FeatureCollection collection = client.search(lat, lon,
						placeName.trim(), "", radiusInKMeters);

				ArrayList<Feature> features = collection.getFeatures();
				//if (features.size() == 1) {
					for (Feature feature : features) {

						String name = (String) feature.getProperties().get(
								"name");
						logger.debug("found:"+name);
						
						//this is lame, should probably use distance or something better
						if(name.equals(placeName) && 
								feature.getProperties().get("city").toString().contains(city)){
							
							places.append(name + ","
									+ placeName.trim().replace(" ", ""));
							places.append(System.getProperty("line.separator"));
							merchantService.save(
									makeMerchantFromFeature(
											recordId,											
											feature, 
											name,
											description, 
											url, 
											facebookUrl,
											member));
						}
						

					}

				//}
			} else if (status.equalsIgnoreCase("DELETE")) {
				Merchant m = merchantService.findByPrimaryKey(new Long(recordId));				
				merchantService.delete(m);
			}

		}

		logger.debug(places.toString());

	}	
	
	private Merchant makeMerchantFromFeature(
			Long recordId, Feature f, String name, String description, String url, 
			String facebookUrl, NetworkMember member) 
	throws BLServiceException 
	{
		
		Merchant m = merchantService.findByNameAndMember(name, member);
		if(m == null) {
			m = new Merchant();
			m.setName(name);
		}
		
		//dont add a place that is already in the database
		Place p = null;
		try {
			p = placeManagerService.findBySimpleGeoId(f.getSimpleGeoId());
		} catch (BLServiceException e) {
			logger.error("cannot find place", e);
		}
		
		if(p == null) {
			p = new Place();
			simpleGeoPlaceManager.transformFeature( f,  p );
			placeManagerService.savePlace(p);
		}
		
		
		m.setPlace(p);
		m.setDescription(description);
		m.setUrl(url);
		m.setFacebookUrl(facebookUrl);
		m.setNetworkMember(member);
		m.setStatus("ACTIVE");
		m.setTimeCreated(Calendar.getInstance().getTimeInMillis());
		m.setTimeUpdated(Calendar.getInstance().getTimeInMillis());

		return m;
	}
	
	
    
    
    
    
}
