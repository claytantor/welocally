package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.noi.utility.hibernate.GUIDGenerator;
import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.admin.velocity.PublisherOrderGenerator;
import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.PaymentNotification;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.OrderService;
import com.sightlyinc.ratecred.service.PaymentNotificationService;
import com.sightlyinc.ratecred.service.PublisherService;
import com.sightlyinc.ratecred.util.JavaMailer;
import com.welocally.admin.security.UserNotFoundException;
import com.welocally.admin.security.UserPrincipal;
import com.welocally.admin.security.UserPrincipalService;
import com.welocally.admin.security.UserPrincipalServiceException;

/**
 * @author sam
 * @version $Id$
 */
@Controller
@RequestMapping("/paypal")
public class PaypalNotificationController {

	static Logger logger = Logger.getLogger(PaypalNotificationController.class);
	
	@Value("${admin.adminEmailAccountTo:clay@welocally.com}")
	private String adminEmailAccountTo;
	
	@Value("${admin.adminEmailAccountFrom:mailer@ratecred.com}")
	private String adminEmailAccountFrom;
	
	@Autowired
	private JavaMailer mailer;
	
    @Autowired
    private PublisherService publisherService;
    
    @Autowired
    private UserPrincipalService userService;
    
    @Autowired
    private PaymentNotificationService paymentNotificationService; 
    
//    @Autowired
//    private SimpleGeoJsonTokenDao simpleGeoJsonTokenDao;

	@Autowired
	OrderService orderManagerService;
	
	@Value("${paypal.callback.endpoint:https://www.sandbox.paypal.com/cgi-bin/webscr}")
	//https://www.paypal.com/cgi-bin/webscr
	//https://www.sandbox.paypal.com/cgi-bin/webscr
	private String paypalEndpoint;
	
	@Value("${paypal.merchant.email:clay_1314558577_biz@ratecred.com}")
	private String merchantEmail;
	
	
	/**
	 * Handles requests sent by the Wordpress plugin when users publish a post.
	 * Sends blog, post, place and SimpleGeo feature info in a JSON object.
	 * 
	 */
	@RequestMapping(value = "/notify")
	@ResponseBody
	public ModelAndView notify(Model m, HttpServletRequest request)
			throws JSONException {
		ModelAndView modelAndView = new ModelAndView("paypal/response");

		logger.debug("got notification");
		try {
			// read post from PayPal system and add 'cmd'
			Enumeration en = request.getParameterNames();
			StringBuffer paramsBuf = new StringBuffer();
			String str = "cmd=_notify-validate";
			while (en.hasMoreElements()) {
				String paramName = (String) en.nextElement();
				String paramValue = request.getParameter(paramName);
				str = str + "&" + paramName + "="
						+ URLEncoder.encode(paramValue, "utf-8");
				logger.debug("["+paramName+"="+paramValue+"]");
				paramsBuf.append("["+paramName+"="+paramValue+"]\n");
			}

			//save the notification
			PaymentNotification notification = new PaymentNotification();
			notification.setPublisherKey(request.getParameter("custom"));
			notification.setTransactionType(request.getParameter("txn_type"));
			notification.setExternalKey(request.getParameter("ipn_track_id"));
			notification.setNotficationBody(paramsBuf.toString());
			notification.setTimeCreated(Calendar.getInstance().getTimeInMillis());
			notification.setTimeUpdated(Calendar.getInstance().getTimeInMillis());			
			paymentNotificationService.save(notification);
			
			//send an email on all notifications
			sendNotificationEmail(paramsBuf.toString()); 
			
						
			// post back to PayPal system to validate
			// NOTE: change http: to https: in the following URL to verify using
			// SSL (for increased security).
			// using HTTPS requires either Java 1.4 or greater, or Java Secure
			// Socket Extension (JSSE)
			// and configured for older versions.
			URL u = new URL(paypalEndpoint);
			URLConnection uc = u.openConnection();
			uc.setDoOutput(true);
			uc.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			PrintWriter pw = new PrintWriter(uc.getOutputStream());
			pw.println(str);
			pw.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(uc
					.getInputStream()));
			String res = in.readLine();
			in.close();

			// assign posted variables to local variables
			/*
			 *  - [item_number=71d14d4574db]
 - [residence_country=US]
 - [period3=1 M]
 - [period1=1 M]
 - [verify_sign=AM-lQIiNYo1ooBjV7r75PXJhBPuIAIrt-AlLgxCNY2x1SYM5F5Dr.0UB]
 - [business=clay_1314558577_biz@ratecred.com]
 - [first_name=Test]
 - [payer_id=D6V5WV763Y5YJ]
 - [payer_email=clay_1314675261_per@ratecred.com]
 - [subscr_id=I-K3TF1UJ2UMCM]
 - [btn_id=1882543]
 - [receiver_email=clay_1314558577_biz@ratecred.com]
 - [notify_version=3.2]
 - [recurring=1]
 - [txn_type=subscr_signup]
 - [test_ipn=1]
 - [mc_currency=USD]
 - [payer_status=verified]
 - [custom=62415d7d0bde]
 - [subscr_date=20:55:43 Aug 29, 2011 PDT]
 - [charset=windows-1252]
 - [amount3=5.99]
 - [amount1=0.00]
 - [ipn_track_id=1BGNUC1HK762WB6Z9jsOcg]
 - [item_name=Welocally Places for Wordpress BETA DEV]
 - [last_name=User]
 - [mc_amount1=0.00]
 - [mc_amount3=5.99]
 - [reattempt=1]
			 */
			
			
			
			if (res.equals("VERIFIED")) {
				
				String itemName = request.getParameter("item_name");
				String itemNumber = request.getParameter("item_number");
				String payerStatus = request.getParameter("payer_status");
				String publisherKey = request.getParameter("custom");	
				String receiverEmail = request.getParameter("receiver_email");
				String orderId = request.getParameter("txn_id");
				String txType = request.getParameter("txn_type");
				String ipnTrack = request.getParameter("ipn_track_id");
								
				// check that paymentStatus=Completed
				// check that txnId has not been previously processed
				Order  o = this.orderManagerService.findOrderByTxId(orderId);
				
				if(o == null 
						&& txType.equals("subscr_signup")
						&& ipnTrack != null
						&& merchantEmail.equalsIgnoreCase(receiverEmail))
				{
							
					String paymentCurrency = request.getParameter("mc_currency");
					String payerEmail = request.getParameter("payer_email");
					String payerId = request.getParameter("payer_id");
					String paymentAmount = request.getParameter("mc_amount3");
					
					// process payment
					//this should have the subscription and the 
					//tx id, since we dont have the tx id order on cancel
					//we need a refactor
					 logger.debug("new order:"+orderId);
					 o = new Order();
					 o.setExternalTxId(orderId);
					 //should be tracking no field
					 o.setExternalOrderItemCode(ipnTrack);
					 o.setStatus(Order.OrderStatus.SUBSCRIBED);
					 
					 //lookup by product by product sku
					 
					 //o.setPrice(Float.valueOf(paymentAmount));
					 //o.setSku(itemNumber);
					 
					 o.setBuyerEmail(payerEmail);
					 
					 //o.setQuantity(1);
					 o.setBuyerKey(publisherKey);
					 
					 //o.setTitle(itemName);
					 
					 o.setExternalPayerId(payerId);
					 o.setTimeCreated(Calendar.getInstance().getTimeInMillis());
					 o.setTimeUpdated(Calendar.getInstance().getTimeInMillis());
									 				 
					 //complete subscription
					 logger.debug("processing order:"+o.getExternalTxId());
					 //find the publisher by custom field
					 Publisher publisher = 
						publisherService.findByNetworkKeyAndPublisherKey("welocally", publisherKey);
					
					if(publisher != null)
						processPublisherOrder(publisher, o, paramsBuf.toString());					 
					 
				} else if(o == null //prepay txn_type=web_accept
						&& txType.equals("web_accept")
						&& ipnTrack != null
						&& merchantEmail.equalsIgnoreCase(receiverEmail))
				{
									
					String paymentCurrency = request.getParameter("mc_currency");
					String payerEmail = request.getParameter("payer_email");				
					String payerId = request.getParameter("payer_id");
					String paymentAmount = request.getParameter("mc_gross");
					
					// process payment
					// this should have the subscription and the 
					// tx id, since we dont have the tx id order on cancel
					// we need a refactor
					 logger.debug("new order:"+orderId);
					 o = new Order();
					 o.setExternalTxId(orderId);
					 //should be tracking no field
					 o.setExternalOrderItemCode(ipnTrack);
					 o.setStatus(Order.OrderStatus.SUBSCRIBED);
					 //o.setPrice(Float.valueOf(paymentAmount));
					 //o.setSku(itemNumber);
					 o.setBuyerEmail(payerEmail);
					 //o.setQuantity(1);
					 o.setBuyerKey(publisherKey);
					 //o.setTitle(itemName);
					 o.setExternalPayerId(payerId);
					 o.setTimeCreated(Calendar.getInstance().getTimeInMillis());
					 o.setTimeUpdated(Calendar.getInstance().getTimeInMillis());
									 				 
					 //complete subscription
					 logger.debug("processing order:"+o.getExternalTxId());
					 //find the publisher by custom field
					 Publisher publisher = 
						publisherService.findByNetworkKeyAndPublisherKey("welocally", publisherKey);
					
					if(publisher != null)
						processPublisherOrder(publisher, o, paramsBuf.toString());					 
					 
				} else if(o != null 
						&& txType.equals("subscr_cancel") 
						&& publisherKey != null) {
					
					processSubscriptionChange( txType, Publisher.PublisherStatus.CANCELLED, publisherKey,
							o, paramsBuf.toString());
										
				} else if(o != null 
						&& txType.equals("subscr_failed") 
						&& publisherKey != null) {
					
					processSubscriptionChange( txType, Publisher.PublisherStatus.FAILURE, publisherKey,
							o, paramsBuf.toString());
										
				}  else if(o != null 
						&& txType.startsWith("recurring_payment_suspended") 
						&& publisherKey != null) {
					
					processSubscriptionChange( txType, Publisher.PublisherStatus.SUSPENDED, publisherKey,
							o, paramsBuf.toString());
					
				}
				else {
					logger.debug("an order was already found, or " +
							"there was a validation problem with the txid:"+orderId);
				}
								
				
			} else if (res.equals("INVALID")) {
				logger.error("INVALID TRANSACTION!");
			} else {
				logger.error("NO ACTION TAKEN!");
			}
		} catch (MalformedURLException e) {
			logger.error("problem", e);
		} catch (IOException e) {
			logger.error("problem", e);
		} catch (BLServiceException e) {
			logger.error("problem", e);
		} catch (Exception e) {
			logger.error("undertimed problem", e);
		}

		return modelAndView;
	}
	
	private void processSubscriptionChange(String txType, Publisher.PublisherStatus statusType, String publisherKey,
			Order o, String body) throws BLServiceException {
		
		Publisher publisher = publisherService.findByPublisherKey(publisherKey);

		if (publisher != null) {
			logger.debug("canceling subscription for publisher:"
					+ publisher.getName() + " with key:"
					+ publisher.getKey());
			publisher.setSubscriptionStatus(statusType);
			o.setStatus(Order.OrderStatus.CANCELLED);
		}

		publisherService.save(publisher);
		orderManagerService.save(o);

		// dont fail on email problems
		try {
			sendOrderStatusEmail(o, body);
		} catch (Exception e) {
			logger.error("could not send email", e);
		}
	}
	
	
	private void processPublisherOrder(Publisher publisher, Order o, String params) throws BLServiceException {
		
		logger.debug("processPublisherOrder");
		           
        
		try {
			long serviceEndDateMillis = new Date().getTime();
	        
	        serviceEndDateMillis += (2592000000L*1);
	        
	        String publisherToken = GUIDGenerator.createId().replaceAll("-", "");
	        
	        //update the password to be the key
			UserPrincipal up = userService.loadUser(publisher.getKey());
//			String hashedPass = userService.makeMD5Hash(publisherToken);					
//			up.setPassword(hashedPass);
			userService.activateWithRoles(up, Arrays.asList("ROLE_USER", "ROLE_PUBLISHER"));
			
			publisher.setServiceEndDateMillis(serviceEndDateMillis);
	        publisher.setJsonToken(publisherToken);
	        publisher.setSubscriptionStatus(Publisher.PublisherStatus.SUBSCRIBED);
	        
	        o.setOwner(publisher);       
	        
	        orderManagerService.save(o);
	        
	        //enable the user
	        //publisher.getUserPrincipal().setEnabled(true);
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
		PublisherOrderGenerator generator = 
			new PublisherOrderGenerator(model);
		

		mailer.sendMessage(
				adminEmailAccountFrom, 
				"Welocally Mailer Bot",
				adminEmailAccountTo, 
				"Welocally Admin", 
				"[Welocally Admin] Subscription "+o.getOwner().getSubscriptionStatus(), 
				generator.makeDisplayString()+params,
				"text/plain");		
	}
	
	private void sendNotificationEmail(String params) 
	{
				
		Map model = new HashMap();

		mailer.sendMessage(
				adminEmailAccountFrom, 
				"Welocally Mailer Bot",
				adminEmailAccountTo, 
				"Welocally Admin", 
				"[Welocally Admin] Paypal Notfication", 
				params,
				"text/plain");		
	}
	

}
