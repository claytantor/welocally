package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.AuthenticationProvider;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.ui.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.noi.utility.hibernate.GUIDGenerator;
import com.noi.utility.mail.MailerQueueService;
import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.admin.model.AjaxError;
import com.sightlyinc.ratecred.admin.model.AjaxErrors;
import com.sightlyinc.ratecred.admin.model.UserPrincipalForm;
import com.sightlyinc.ratecred.admin.util.JsonObjectSerializer;
import com.sightlyinc.ratecred.admin.velocity.PublisherRegistrationGenerator;
import com.sightlyinc.ratecred.authentication.UserNotFoundException;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.authentication.UserPrincipalServiceException;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.Product;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.model.Site;
import com.sightlyinc.ratecred.service.NetworkMemberService;
import com.sightlyinc.ratecred.service.OrderService;
import com.sightlyinc.ratecred.service.ProductService;
import com.sightlyinc.ratecred.service.PublisherService;

/**
 * @author sam
 * @version $Id$
 */
@Controller
@RequestMapping("/signup/2_0")
public class SignUpControllerV2 {

    private static Logger logger = Logger.getLogger(SignUpControllerV2.class);

    @Autowired
    private UserPrincipalService userService;
    

    @Autowired
    private PublisherService publisherService;
    
    @Autowired
    private ProductService productService;
    

    @Autowired
    private NetworkMemberService networkMemberService;
    
	@Autowired
	private OrderService orderManagerService;
	
	@Autowired
	private ObjectMapper jacksonMapper;
	
	@Autowired
	JsonObjectSerializer jsonObjectSerializer;
	
	@Value("${paypal.callback.endpoint:https://www.sandbox.paypal.com/cgi-bin/webscr}")
    private String paypalFormEndpoint;
    
	@Value("${default.product.sku:f102b14a87a1}")
    private String freeProductSku;
    
    
    
    @Autowired
	private MailerQueueService mailerQueueService;
    
	@Value("${admin.adminEmailAccountTo:clay@welocally.com}")
	private String adminEmailAccountTo;
	
	@Value("${admin.adminEmailAccountFrom:mailer@ratecred.com}")
	private String adminEmailAccountFrom;
    

    @ModelAttribute("signup")
    public UserPrincipalForm getForm() {
        return new UserPrincipalForm();
    }

    @RequestMapping("/success")
    public String success() {
        return "signup_success";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getSignUpForm() {
        return "signup";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String postSignUpForm(@ModelAttribute("signup") UserPrincipalForm form, BindingResult errors) {
        String viewName = "signup";

        // validate email
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "emailRequired", "Please provide an email");

        if (!errors.hasErrors()) {
            if (!com.noi.utility.validation.ValidationUtils.isEmailAddressValid(form.getEmail())) {
                errors.rejectValue("email", "invalidEmail", "Please provide a valid email");
            }
        }

        // validate username
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "usernameRequired", "Please provide a username");

        // validate password
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "passwordRequired", "Please provide a password");

        // password requirements?
        if (!errors.hasErrors()) {
            // create account
            try {
                // TODO hard coding the role to publisher for now, will need to figure out a better way of doing this later
                userService.signUp(form.getEntity(), Arrays.asList("ROLE_USER", "ROLE_PUBLISHER"));
                // send user to success page
                viewName = "redirect:/signup/success";
            } catch (UserPrincipalServiceException e) {
                if (e.getMessage().toLowerCase().contains("email")) {
                    errors.rejectValue("email", "emailUsed", e.getMessage());
                } else if (e.getMessage().toLowerCase().contains("username")) {
                    errors.rejectValue("username", "usernameUsed", e.getMessage());
                } else {
                    errors.reject("error", e.getMessage());
                }
            } catch (Exception e) {
                logger.error(e);
                e.printStackTrace();
                errors.reject("error", "Something went wrong saving your account information, please try again");
            }
        }

        return viewName;
    }
    
    @RequestMapping(value="/plugin/login", method=RequestMethod.GET)
	public String homePublisher(@RequestParam String siteKey, @RequestParam String siteToken, HttpServletRequest request, Model model) {
    	logger.debug("home");
		
			
		// Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(siteKey, siteToken);
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authentication = ((AuthenticationProvider)userService).authenticate(token);
        logger.debug("Logging in with [{}]"+ authentication.getPrincipal());
        SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/home";

	}

    @RequestMapping(value = "/plugin/key")
    @ResponseBody
    public Map<String, Object> signUpFromPluginForKey(@RequestBody String requestJson, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<String, Object>();
        List<String> errors = new ArrayList<String>();

        String key = null;
        String email = null;
        String siteUrl = null;
        String siteName = null;
        String description = null;
        String iconUrl = null;

        try {
            JSONObject jsonObject = new JSONObject(requestJson);
            if(!jsonObject.isNull("siteKey"))
            	key = jsonObject.getString("siteKey");
            
            email = jsonObject.getString("siteEmail");
            siteUrl = jsonObject.getString("siteHome");
            siteName = jsonObject.getString("siteName");
            description = jsonObject.getString("siteDescription");
            iconUrl = jsonObject.getString("iconUrl");
        } catch (JSONException e) {
            e.printStackTrace();
            errors.add("Unable to parse request, please check the format of your data");
        }

        if (errors.isEmpty()) {
            // validate input
            if (StringUtils.isBlank(siteUrl)) {
                errors.add("Please provide the URL for your site");
            }
            if (StringUtils.isBlank(siteName)) {
                errors.add("Please provide the name for your site");
            }
            if (StringUtils.isBlank(email)) {
                errors.add("Please provide a real email address");
            }

            if(key == null || key.isEmpty()){
            	key = UUID.randomUUID().toString();
                key = key.substring(key.lastIndexOf('-') + 1);
                response.put("key", key);
                response.put("subscriptionStatus", "KEY_ASSIGNED");               
            } else {
            	Publisher p = publisherService.findByPublisherKey(key);
            	response.put("key", key);
                response.put("subscriptionStatus", p.getSubscriptionStatus());
            }
        }
            
            
        response.put("errors", errors);

        return response;
    }
    
    @RequestMapping(value = "/plugin/register",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> register(@RequestBody String requestJson, HttpServletRequest request) {
    	//ModelAndView mav = new ModelAndView("mapper-result");
    	Map<String, Object> response = new HashMap<String, Object>();
        
    	
		AjaxErrors eajax = new AjaxErrors();
    	JSONObject jsonObject = null;
    	
    	//all we do here is gen the token and send it by email
    	try {
    		jsonObject = new JSONObject(requestJson);

    		
			if (jsonObject != null) {
				String key= null;
	            if(!jsonObject.isNull("siteKey"))
	            	key = jsonObject.getString("siteKey");
	            String email = jsonObject.getString("siteEmail");
	            String siteUrl = jsonObject.getString("siteHome");
	            String siteName = jsonObject.getString("siteName");
	            
	            String siteToken = null;
	            if(!jsonObject.isNull("siteToken"))
	              siteToken = jsonObject.getString("siteToken");
	          
				
				//make the user if they dont exist
				UserPrincipal user = userService.findByUserName(key);
				if(user==null){
					user = new UserPrincipal();
                    user.setEmail(email);
                    user.setPassword(Long.toString(System.currentTimeMillis()));
                    user.setUsername(key);
                    user.setEnabled(false);
                    try {
                        userService.saveUserPrincipal(user);
                        // not going to worry about roles for now since user isn't going to be logging in
                    } catch (UserPrincipalServiceException e) {
                    	logger.error("problem with request", e);
            			eajax.getErrors().add(
            					new AjaxError(AjaxError.RES_GEN_ERROR, "Problem with request."));
                    }
				}
								
				if(eajax.getErrors().isEmpty()){
					
					//try to look up the publisher
					Publisher publisher = publisherService.findByPublisherKey(key);
					if(publisher == null){
						
						//make the publisher
						publisher = new Publisher();
						publisher.setUserPrincipal(user);
						
						Site s = new Site();
						s.setName(siteName);
						//s.setDescription(description);
						s.setUrl(siteUrl);
						s.setActive(true);
						if(siteUrl.equals(request.getHeader("referer"))){
							s.setVerified(true);
						} else {
							s.setVerified(false);
						}
						
						Set<Site> sites = new HashSet<Site>();
						sites.add(s);
						publisher.setSites(sites);
						
						  publisher.setName(siteName);
						  //publisher.setDescription(description);
						  NetworkMember defaultNetworkMember = networkMemberService.getDefaultNetworkMember();
						  publisher.setNetworkMember(defaultNetworkMember);


	                  	publisher.setKey(key);
	                  	publisher.setSubscriptionStatus("REGISTERED");

	                  	publisherService.save(publisher);
												
						//ok go find the free product and use it to build the order
						Product freeProduct = productService.findProductBySku(freeProductSku);
						Order o = orderManagerService.makeOrderFromProduct(freeProduct);
						
						o.setStatus(Order.OrderStatus.REGISTERED);
						o.setBuyerEmail(jsonObject.getString("siteEmail"));
						o.setBuyerKey(jsonObject.getString("siteKey"));				 
						o.setExternalPayerId(jsonObject.getString("siteEmail"));
						o.setTimeCreated(Calendar.getInstance().getTimeInMillis());
						o.setTimeUpdated(Calendar.getInstance().getTimeInMillis());
									 				 
						//complete subscription
						logger.debug("processing order:"+o.getExternalTxId());
						
						
						if(publisher != null){
							orderManagerService.saveOrder(o);	
							processPublisherOrder(publisher, o, requestJson);	
						}
						
					} else if (publisher != null && siteToken != null && publisher.getSubscriptionStatus().equals("REGISTERED")){
						if(publisher.getJsonToken().equals(siteToken)){
							
							//activate the user
							UserPrincipal up = userService.loadUser(publisher.getKey());
							up.setEnabled(true);
							up.setLocked(false);
							up.setCredentialsExpired(false);
							userService.saveUserPrincipal(up);
							
							//set the status of the publisher
							publisher.setSubscriptionStatus("SUBSCRIBED");
							publisherService.save(publisher);
						}
						
					}					
					
					response.put("siteKey", publisher.getKey());
					response.put("subscriptionStatus", publisher.getSubscriptionStatus());
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
				response.put("mapperResult",
						jsonObjectSerializer.serialize(eajax));
				//mav.setViewName("mapper-result");
			} catch (IOException e) {
				logger.error("cannot serialize message", e);
			}
		}
			
    
		return response;
    }
    
    private void processPublisherOrder(Publisher publisher, Order o, String params) throws BLServiceException {
		
		logger.debug("processPublisherOrder");
		           
        
		try {
			long serviceEndDateMillis = new Date().getTime();
	        
	        serviceEndDateMillis += (2592000000L*1);
	        
	        String publisherToken = GUIDGenerator.createId().replaceAll("-", "");
	        
	        //update the password to be the key
			UserPrincipal up = userService.loadUser(publisher.getKey());
			String hashedPass = userService.makeMD5Hash(publisherToken);					
			up.setPassword(hashedPass);
			userService.activateWithRoles(up, Arrays.asList("ROLE_USER", "ROLE_PUBLISHER"));
			
			publisher.setServiceEndDateMillis(serviceEndDateMillis);
	        publisher.setJsonToken(publisherToken);
	        publisher.setSubscriptionStatus("REGISTERED");
	        o.setOwner(publisher);       
	        
	        orderManagerService.saveOrder(o);
	        
	        //enable the user
	        publisher.getUserPrincipal().setEnabled(true);
	        publisherService.save(publisher);
	        

			sendOrderStatusEmail(o,params); 

			
		} catch (UserPrincipalServiceException e) {
			logger.error("could not find user problem",e);
		} catch (UserNotFoundException e) {
			logger.error("cound not find user problem",e);
		} catch(Exception e){
			logger.error("problem",e);
		}
        
        
	}

	private void sendOrderStatusEmail(Order o, String params) 
	{
				
		Map model = new HashMap();
		model.put("order", o);
		//model.put("jsonModel",params);
		
		PublisherRegistrationGenerator generator = 
			new PublisherRegistrationGenerator(model);
			
		mailerQueueService.sendMessage(
				adminEmailAccountFrom, 
				"Welocally Mailer Bot",
				o.getBuyerEmail(), 
				"Welocally Admin", 
				"[Welocally Admin] Registration "+o.getOwner().getSubscriptionStatus(), 
				generator.makeDisplayString(),
				"text/html");		
	}

    public void setUserService(UserPrincipalService userService) {
        this.userService = userService;
    }
}
