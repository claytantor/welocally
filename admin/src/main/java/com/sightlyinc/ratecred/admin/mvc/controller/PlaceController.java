package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.string.StringUtils;
import com.noi.utility.xml.JsonEncoder;
import com.sightlyinc.ratecred.admin.geocoding.GeocoderException;
import com.sightlyinc.ratecred.admin.geocoding.YahooGeocoder;
import com.sightlyinc.ratecred.admin.model.AjaxError;
import com.sightlyinc.ratecred.admin.model.AjaxErrors;
import com.sightlyinc.ratecred.admin.model.Feature;
import com.sightlyinc.ratecred.admin.model.PlaceSearchResult;
import com.sightlyinc.ratecred.admin.model.wordpress.JsonModelProcessor;
import com.sightlyinc.ratecred.admin.util.GoogleSpreadsheetUtils;
import com.sightlyinc.ratecred.client.geo.GeoPlacesClient;
import com.sightlyinc.ratecred.client.geo.SimpleGeoLocationClient;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.pojo.Location;
import com.sightlyinc.ratecred.service.PlaceManagerService;
import com.sightlyinc.ratecred.service.PublisherService;

@Controller
@RequestMapping("/publisher/place")
public class PlaceController {

	static Logger logger = Logger.getLogger(PlaceController.class);

	@Autowired
	private PublisherService publisherService;

	@Autowired
	private PlaceManagerService placeManagerService;

	@Autowired
	private JsonModelProcessor jsonModelProcessor;

	@Autowired
	private YahooGeocoder yahooGeocoder;

	@Autowired
	private GeoPlacesClient geoPlacesClient;

	@Autowired
	private ObjectMapper jacksonMapper;

	@ModelAttribute("encoder")
	public JsonEncoder getJsonEncoder() {
		return JsonEncoder.getInstance();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String addPlace(Model model) {
		model.addAttribute("placeForm", new Place());
		return "place/edit";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editPlace(@PathVariable Long id, Model model) {
		logger.debug("edit");
		model.addAttribute("placeForm", placeManagerService
				.findPlaceByPrimaryKey(id));
		return "place/edit";
	}

	/**
	 * ok we are having to do some backflips because it looks like the jackson
	 * deserializer is breaking for some reason, if you can get this working
	 * with a simpler implementation then by all means
	 * 
	 * @param f
	 * @param model
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/feature", method = RequestMethod.POST)
	public String createFromFeature(@RequestBody Feature f, Model model,
			HttpServletResponse response) {
		logger.debug("got post action");

		try {
			// try to find the place
			Place p = placeManagerService.findBySimpleGeoId(f.getId());
			if (p == null)
				p = new Place();
			transformFeature(f, p);
			placeManagerService.savePlace(p);

			model.addAttribute("place", p);
			model.addAttribute("status", "SUCCESS");
			response.setStatus(200);
		} catch (BLServiceException e) {
			logger.error("problem saving place", e);
			response.setStatus(500);
		}

		return "save-place";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String create(@Valid Place placeForm, BindingResult result) {
		logger.debug("got post action");
		try {
			placeManagerService.savePlace(placeForm);
			return "redirect:/publisher/place/" + placeForm.getId();

		} catch (BLServiceException e) {
			logger.error("problem saving place", e);
			return "error";
		}

	}

	@RequestMapping(value = "/chooser-add", method = RequestMethod.POST)
	public ModelAndView chooserAddPlace(
			@RequestParam("add-place-name") String name,
			@RequestParam("add-place-street") String street,
			@RequestParam("add-place-city") String city,
			@RequestParam("add-place-state") String state,
			@RequestParam("add-place-zip") String zip,
			HttpServletResponse response
	// , HttpServletRequest request
	) {
		logger.debug("adding place from place chooser");
		ModelAndView modelAndView = new ModelAndView("save-place");

		Place place = new Place();
		place.setName(name);
		place.setAddress(street);
		place.setCity(city);
		place.setState(state);
		place.setPostalCode(zip);
		try {
			Location location = yahooGeocoder.geocode(place.getAddress() + " "
					+ place.getCity() + " " + place.getState() + " "
					+ place.getPostalCode());

			place.setLatitude(location.getLat());
			place.setLongitude(location.getLng());

			// geocode worked, post to simplegeo
			Map<String, Object> results = geoPlacesClient.addPlace(place);

			if (results != null) {
				placeManagerService.savePlace(place);

				modelAndView.addObject("place", place);
				modelAndView.addObject("status", "SUCCESS");
				response.setStatus(200);
			}

		} catch (GeocoderException e) {
			// response.put("error",
			// "Could not verify place address. please check the street address, city, state, and zip code");
			response.setStatus(500);
		} catch (BLServiceException e) {
			// response.put("error", "An error occurred, please try again");
			response.setStatus(500);
		}

		return modelAndView;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getPlaceById(@PathVariable Long id, Model model) {
		logger.debug("view");
		model.addAttribute("place", placeManagerService
				.findPlaceByPrimaryKey(id));
		return "place/view";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deletePlace(@PathVariable Long id) {
		logger.debug("delete");
		Place place = placeManagerService.findPlaceByPrimaryKey(id);
		try {
			placeManagerService.deletePlace(place);
		} catch (BLServiceException e) {
			throw new RuntimeException(e);
		}
		return "redirect:/publisher/place/list";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		logger.debug("list");
		try {
			model.addAttribute("places", placeManagerService.findAllPlaces());
		} catch (BLServiceException e) {
			throw new RuntimeException(e);
		}
		return "place/list";
	}

	@RequestMapping("/search")
	public String searchByName(@RequestParam("name") String name, Model model) {
		logger.debug("search by name");
		List<Place> places;
		try {
			places = placeManagerService.findPlacesByNamePrefix(name);
		} catch (BLServiceException e) {
			throw new RuntimeException(e);
		}
		model.addAttribute("places", places);
		return "place/list_json";
	}

	@RequestMapping(value = "/addplace", method = RequestMethod.POST)
	public ModelAndView chooserAddPlace(@RequestBody String requestJson) {
		logger.debug("adding place from place chooser");

		ModelAndView mav = new ModelAndView("add-place");

		String siteKey = null;
		String siteToken = null;
		AjaxErrors eajax = new AjaxErrors();

		JSONObject jsonObject = null;

		try {
			//the inbound jason can be full of extra slashes that can cause problems, clean it it
			String j2 = JsonEncoder.getInstance().encodeString(requestJson);
			jsonObject = new JSONObject(j2);

			siteKey = jsonObject.getString("siteKey");
			logger.debug("siteKey:" + siteKey);

			siteToken = jsonObject.getString("siteToken");
			logger.debug("siteToken:" + siteToken);

			if (jsonObject != null && siteKey != null) {

				// look up the selected publisher
				Publisher publisher = publisherService
						.findByNetworkKeyAndPublisherKey("welocally", siteKey);
				if (publisher != null
						&& "SUBSCRIBED".equals(publisher
								.getSubscriptionStatus())) {

					Place place = null;
					if (geoPlacesClient instanceof SimpleGeoLocationClient) {
						place = jsonModelProcessor
								.saveNewPlaceFromPostJson(jsonObject);
					}

					StringWriter sw = new StringWriter(); // serialize
					MappingJsonFactory jsonFactory = new MappingJsonFactory();
					JsonGenerator jsonGenerator = jsonFactory
							.createJsonGenerator(sw);
					jacksonMapper.writeValue(jsonGenerator, place);
					sw.flush();
					sw.close();

					mav.addObject("place", sw.getBuffer().toString());

				} else {

					if (publisher == null) {
						eajax.getErrors().add(
								new AjaxError(AjaxError.PUBLISHER_KEY_MISSING,
										"Could not find publisher with key:"
												+ siteKey));
					} else {
						eajax
								.getErrors()
								.add(
										new AjaxError(
												AjaxError.SUBSCRIPTION_INVALID,
												"Subscription status invalid for publisher with key:"
														+ siteKey
														+ " "
														+ publisher
																.getSubscriptionStatus()));
					}

				}

			}

		} catch (JSONException e) {
			logger.error("problem with request", e);
			eajax.getErrors().add(
					new AjaxError(AjaxError.REQ_PARSE_ERROR, "Problem parsing request."));

		} catch (IOException e) {
			logger.error("problem with request", e);
			eajax.getErrors().add(
					new AjaxError(AjaxError.RES_GEN_ERROR, "Problem while generating response."));
		} catch (Exception e) {
			logger.error("problem with request", e);
			eajax.getErrors().add(
					new AjaxError(AjaxError.WL_SERVER_ERROR,
							"Problem back at Welocally please report: "
									+ e.getStackTrace()[0].toString().replace(
											".", " ")));
		}

		// if errors send them instead
		if (eajax.getErrors().size() > 0) {
			try {
				mav.getModel().put("mapperResult",
						makeJsonString(jacksonMapper, eajax));
				mav.setViewName("mapper-result");
			} catch (IOException e) {
				logger.error("cannot serialize message", e);
			}
		}

		return mav;

	}

	@RequestMapping(value = "/finder", method = RequestMethod.GET)
	public ModelAndView placeFinder() {
		ModelAndView mav = new ModelAndView("place/finder");
		return mav;
	}

	@RequestMapping(value = "/saveplace2", method = RequestMethod.POST)
	public ModelAndView savePlace(@RequestBody String requestJson) {
		ModelAndView mav = new ModelAndView("add-place");

		AjaxErrors eajax = new AjaxErrors();

		JSONObject jsonObject = null;

		try {
			jsonObject = new JSONObject(requestJson);

			if (jsonObject != null) {

				Place p = null;


				if (!StringUtils.isEmpty(jsonObject.getString("externalId"))) {
					//geoPlacesClient.addPlace(p);
					p = jsonModelProcessor
					.updatePlaceFromPostJson(jsonObject);
					//eajax.getErrors().add(
					//		new AjaxError(AjaxError.NO_IMPL, "No Implementation Yet"));
				}
				else {
					//geoPlacesClient.addPlace(p);
					p = jsonModelProcessor
					.saveNewPlaceFromPostJson(jsonObject);
				}
				
				
				if(p!=null){
					StringWriter sw = new StringWriter(); // serialize
					MappingJsonFactory jsonFactory = new MappingJsonFactory();
					JsonGenerator jsonGenerator = jsonFactory
							.createJsonGenerator(sw);
					jacksonMapper.writeValue(jsonGenerator, p);
					sw.flush();
					sw.close();

					mav.addObject("place", sw.getBuffer().toString());
				} else {
					eajax.getErrors().add(
							new AjaxError(AjaxError.WL_SERVER_ERROR, "Cannot save place. Please report."));
				}
				

			}

		} catch (JSONException e) {
			logger.error("problem with request", e);
			eajax.getErrors().add(
					new AjaxError(AjaxError.REQ_PARSE_ERROR, "Problem parsing request."));

		} catch (Exception e) {
			logger.error("problem with request", e);
			eajax.getErrors().add(
					new AjaxError(AjaxError.WL_SERVER_ERROR,
							"Problem back at Welocally please report: "
									+ e.getStackTrace()[0].toString().replace(
											".", " ")));
		}

		// if errors send them instead
		if (eajax.getErrors().size() > 0) {
			try {
				mav.getModel().put("mapperResult",
						makeJsonString(jacksonMapper, eajax));
				mav.setViewName("mapper-result");
			} catch (IOException e) {
				logger.error("cannot serialize message", e);
			}
		}

		return mav;
	}

	@RequestMapping(value = "/queryplaces2", method = RequestMethod.POST)
	public ModelAndView getPlacesByQueryJsonToo(@RequestBody String requestJson) {
		logger.debug("getPlacesByQueryJson");
		ModelAndView mav = new ModelAndView("places");

		AjaxErrors eajax = new AjaxErrors();

		JSONObject jsonObject = null;

		try {

			jsonObject = new JSONObject(requestJson);

			if (jsonObject != null) {
				String address = jsonObject.getString("address");
				String query = jsonObject.getString("query");
				String category = "";
				Long radius = jsonObject.getLong("radius");

				List<Place> places = geoPlacesClient.findPlacesByQuery(address,
						query, category, radius);
				mav.getModel().put("places", places);
				mav.setViewName("places");
			}

		} catch (JSONException e) {
			logger.error("problem with request", e);
			eajax.getErrors().add(
					new AjaxError(AjaxError.REQ_PARSE_ERROR, "Problem parsing request."));

		} catch (Exception e) {
			logger.error("problem with request", e);
			eajax.getErrors().add(
					new AjaxError(AjaxError.WL_SERVER_ERROR,
							"Problem back at Welocally please report: "
									+ e.getStackTrace()[0].toString().replace(
											".", " ")));
		}

		// if errors send them instead
		if (eajax.getErrors().size() > 0) {
			try {
				mav.getModel().put("mapperResult",
						makeJsonString(jacksonMapper, eajax));
				mav.setViewName("mapper-result");
			} catch (IOException e) {
				logger.error("cannot serialize message", e);
			}
		}

		return mav;

	}

	@RequestMapping(value = "/loader", method = RequestMethod.GET)
	public ModelAndView placeLoader() {
		ModelAndView mav = new ModelAndView("place/loader");
		return mav;
	}

	@RequestMapping(value = "/loadspreadsheet", method = RequestMethod.POST)
	public ModelAndView loadPlacesBySpreadsheet(@RequestBody String requestJson) {
		logger.debug("getPlacesByQueryJson");
		ModelAndView mav = new ModelAndView("mapper-result");

		AjaxErrors eajax = new AjaxErrors();

		JSONObject jsonObject = null;

		try {

			jsonObject = new JSONObject(requestJson);

			if (jsonObject != null) {

				String key = jsonObject.getString("spreadsheetKey");
				Long radius = jsonObject.getLong("radius");

				String spreadsheetUrl = "https://spreadsheets.google.com/tq?tqx=out:csv&key="
						+ key + "&hl=en";
				List<String[]> rows = GoogleSpreadsheetUtils
						.getSpreadsheetData(spreadsheetUrl);

				List<PlaceSearchResult> results = new ArrayList<PlaceSearchResult>();

				for (String[] strings : rows) {
					PlaceSearchResult result = makeSearchResultForRow(strings);
					if (result != null)
						results.add(result);

				}

				mav.getModel().put("mapperResult",
						makeJsonString(jacksonMapper, results));
				mav.setViewName("mapper-result");

			}

		} catch (JSONException e) {
			logger.error("problem with request", e);
			eajax.getErrors().add(
					new AjaxError(AjaxError.REQ_PARSE_ERROR, "Problem parsing request."));

		} catch (Exception e) {
			logger.error("problem with request", e);
			eajax.getErrors().add(
					new AjaxError(AjaxError.WL_SERVER_ERROR,
							"Problem back at Welocally please report: "
									+ e.getStackTrace()[0].toString().replace(
											".", " ")));
		}

		// if errors send them instead
		if (eajax.getErrors().size() > 0) {
			try {
				mav.getModel().put("mapperResult",
						makeJsonString(jacksonMapper, eajax));
				mav.setViewName("mapper-result");
			} catch (IOException e) {
				logger.error("cannot serialize message", e);
			}
		}

		return mav;
	}

	protected PlaceSearchResult makeSearchResultForRow(String[] row) {
		if (row.length > 0 && !StringUtils.isEmpty(row[0])) {

			String id = row[0];
			String addressSearch = row[16]+ ", " + row[17] + ", " + row[19]+ " " + row[18];
			String address = row[16];
			String city = row[17];
			String state = row[19];
			String postalCode = row[18];
			String website = row[23];
			String phone = row[5];
			String placeName = row[3];
			String category = row[32];
			Double radius = 2.0;

			List<Place> placesResult = geoPlacesClient.findPlacesByQuery(
					addressSearch, placeName, "", radius);

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	
			
			PlaceSearchResult result = 
				new PlaceSearchResult( id,  placeName,  address,
					 city,  state,  postalCode,  website,
					 phone,  category,  radius.longValue(), placesResult);
			
			return result;

		}
		return null;

	}

	@RequestMapping(value = "/queryplaces", method = RequestMethod.POST)
	public ModelAndView getPlacesByQueryJson(@RequestBody String requestJson) {
		logger.debug("getPlacesByQueryJson");
		ModelAndView mav = new ModelAndView("places");

		String address = null;
		String query = null;
		String category = "";
		Long radius = 10l;

		String siteKey = null;
		String siteToken = null;
		AjaxErrors eajax = new AjaxErrors();
		JSONObject jsonObject = null;
		try {

			jsonObject = new JSONObject(requestJson);

			siteKey = jsonObject.getString("siteKey");
			logger.debug("siteKey:" + siteKey);

			siteToken = jsonObject.getString("siteToken");
			logger.debug("siteToken:" + siteToken);

			// required fields
			address = jsonObject.getString("address");
			query = jsonObject.getString("query");

			if (StringUtils.isEmpty(address) || StringUtils.isEmpty(query)) {
				eajax
						.getErrors()
						.add(
								new AjaxError(AjaxError.DATA_VALIDATION_MISSING,
										"Required field is missing, please check all required fields."));
			}

			if (!jsonObject.isNull("category"))
				category = jsonObject.getString("category");
			if (!jsonObject.isNull("radius"))
				radius = new Long(jsonObject.getInt("radius"));

			if (jsonObject != null && siteKey != null) {

				// look up the selected publisher
				Publisher publisher = publisherService
						.findByNetworkKeyAndPublisherKey("welocally", siteKey);
				if (publisher != null
						&& "SUBSCRIBED".equals(publisher
								.getSubscriptionStatus())) {
					List<Place> places = geoPlacesClient.findPlacesByQuery(
							address, query, category, radius);
					mav.getModel().put("places", places);
					mav.setViewName("places");
				} else {

					if (publisher == null) {
						eajax.getErrors().add(
								new AjaxError(AjaxError.PUBLISHER_KEY_MISSING,
										"Could not find publisher with key:"
												+ siteKey));
					} else {
						eajax
								.getErrors()
								.add(
										new AjaxError(
												AjaxError.SUBSCRIPTION_INVALID,
												"Subscription status invalid for publisher with key:"
														+ siteKey
														+ " "
														+ publisher
																.getSubscriptionStatus()));
					}

				}
			}
		} catch (JSONException e) {
			logger.error("problem with request", e);
			eajax.getErrors().add(
					new AjaxError(AjaxError.REQ_PARSE_ERROR, "Problem parsing request."));

		} catch (Exception e) {
			logger.error("problem with request", e);
			eajax.getErrors().add(
					new AjaxError(AjaxError.WL_SERVER_ERROR,
							"Problem back at Welocally please report: "
									+ e.getStackTrace()[0].toString().replace(
											".", " ")));
		}
		// if errors send them instead
		if (eajax.getErrors().size() > 0) {
			try {
				mav.getModel().put("mapperResult",
						makeJsonString(jacksonMapper, eajax));
				mav.setViewName("mapper-result");
			} catch (IOException e) {
				logger.error("cannot serialize message", e);
			}
		}

		return mav;

	}

	private String makeJsonString(ObjectMapper jacksonMapper, Object serialize)
			throws IOException {
		StringWriter sw = new StringWriter(); // serialize
		MappingJsonFactory jsonFactory = new MappingJsonFactory();
		JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(sw);
		jacksonMapper.writeValue(jsonGenerator, serialize);
		sw.flush();
		sw.close();
		return sw.toString();
	}

	/**
	 * so the reason we do this instead of using the service is that we have
	 * serialized this to a sightly type, it isnt the simple geo type so we need
	 * a different transform, this could be cleaned up later
	 * 
	 * @param f
	 * @param p
	 */
	public static void transformFeature(Feature f, Place p) {

		// Place p = new Place();
		p.setSimpleGeoId(f.getId());

		p.setName(f.getProperties().get("name").toString());
		p.setAddress(f.getProperties().get("address").toString());
		p.setLatitude(f.getGeometry().getCoordinates()[1]);
		p.setLongitude(f.getGeometry().getCoordinates()[0]);
		if (f.getProperties().get("city") != null)
			p.setCity(f.getProperties().get("city").toString());
		if (f.getProperties().get("postalcode") != null)
			p.setPostalCode(f.getProperties().get("postalcode").toString());
		if (f.getProperties().get("province") != null)
			p.setState(f.getProperties().get("province").toString());
		if (f.getProperties().get("phone") != null)
			p.setPhone(f.getProperties().get("phone").toString());
		if (f.getProperties().get("url") != null
				&& f.getProperties().get("website").toString().startsWith(
						"http"))
			p.setWebsite(f.getProperties().get("website").toString()
					.toLowerCase());
		else if (f.getProperties().get("website") != null)
			p
					.setWebsite("http://"
							+ f.getProperties().get("website").toString()
									.toLowerCase());
		else if (f.getProperties().get("menulink") != null)
			p.setWebsite(f.getProperties().get("menulink").toString()
					.toLowerCase());

	}

}
