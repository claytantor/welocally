package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.noi.utility.hibernate.GUIDGenerator;
import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.dao.SimpleGeoJsonTokenDao;
import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.model.SimpleGeoJsonToken;
import com.sightlyinc.ratecred.service.NetworkMemberService;
import com.sightlyinc.ratecred.service.OrderManagerService;
import com.sightlyinc.ratecred.service.PublisherService;

/**
 * @author sam
 * @version $Id$
 */
@Controller
@RequestMapping("/paypal")
public class PaypalNotificationController {

	static Logger logger = Logger.getLogger(PaypalNotificationController.class);
	
    @Autowired
    private PublisherService publisherService;
    
    @Autowired
    private SimpleGeoJsonTokenDao simpleGeoJsonTokenDao;

	@Autowired
	OrderManagerService orderManagerService;
	
	@Value("${paypal.callback.endpoint:https://www.sandbox.paypal.com/cgi-bin/webscr}")
	//https://www.paypal.com/cgi-bin/webscr
	//https://www.sandbox.paypal.com/cgi-bin/webscr
	private String paypalEndpoint;
	
	@Value("${paypal.merchant.email:clay_1314558577_biz@ratecred.com}")
	private String merchantEmail;
	
	@Value("${paypal.subscription.item-number:4cb094d23eb9}")
	private String productItemNumber;

	//lame way to do this
	@Value("${paypal.subscription:5.99}")
	private String subscriptionAmount;
	
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
			String str = "cmd=_notify-validate";
			while (en.hasMoreElements()) {
				String paramName = (String) en.nextElement();
				String paramValue = request.getParameter(paramName);
				str = str + "&" + paramName + "="
						+ URLEncoder.encode(paramValue, "utf-8");
				logger.debug("["+paramName+"="+paramValue+"]");
			}

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
			String itemName = request.getParameter("item_name");
			String itemNumber = request.getParameter("item_number");
			String payerStatus = request.getParameter("payer_status");
			String paymentAmount = request.getParameter("mc_amount3");
			String paymentCurrency = request.getParameter("mc_currency");
			String orderId = request.getParameter("subscr_id");
			String ipnTrack = request.getParameter("ipn_track_id");
			String receiverEmail = request.getParameter("receiver_email");
			String payerEmail = request.getParameter("payer_email");
			String publisherKey = request.getParameter("custom");
			String txType = request.getParameter("txn_type");
			//txn_type=subscr_signup
			// check notification validation
			
			
			if (res.equals("VERIFIED")) {
								
				// check that paymentStatus=Completed
				// check that txnId has not been previously processed
				Order  o = this.orderManagerService.findOrderByTxId(orderId);
				
				//need a check on supported product ids
				if(o == null 
						&& txType.equals("subscr_signup")
						//&& itemNumber.equals(productItemNumber) should check products
						&& ipnTrack != null
						&& merchantEmail.equalsIgnoreCase(receiverEmail))
				{
									
					// process payment
					//this shpudl have the subscription and the 
					//tx id, since we dont have the tx id order on cancel
					//we need a refactor
					 logger.debug("new order:"+orderId);
					 o = new Order();
					 o.setExternalTxId(orderId);
					 //should be tracking no field
					 o.setExternalOrderItemCode(ipnTrack);
					 o.setStatus(txType);
					 o.setPrice(Float.valueOf(paymentAmount));
					 o.setSku(itemNumber);
					 o.setBuyerEmail(payerEmail);
					 o.setQuantity(1);
					 o.setBuyerName(publisherKey);
					 				 
					 //complete subscription
					logger.debug("processing order:"+o.getExternalTxId());
					//find the publisher by custom field
					Publisher publisher = 
						publisherService.findByNetworkKeyAndPublisherKey("welocally", publisherKey);
					
					if(publisher != null)
						processPublisherOrder(publisher, o);
											 
					 
					 
				} else if(o != null 
						&& txType.equals("subscr_cancel") 
						&& publisherKey != null) {
					
					Publisher publisher = 
						publisherService.findByNetworkKeyAndPublisherKey("welocally", publisherKey);
					
					if(publisher != null) {
						publisher.setSubscriptionStatus("CANCELLED");
						o.setStatus(txType);
					}
					
					publisherService.save(publisher);
					orderManagerService.saveOrder(o);
					
					
				} else {
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
	
	
	private void processPublisherOrder(Publisher publisher, Order o) throws BLServiceException {
		
		logger.debug("processPublisherOrder order:"+o.getExternalTxId()+" key:"+publisher.getKey());
		      
        long serviceEndDateMillis = new Date().getTime();
        
        serviceEndDateMillis += (2592000000L*1);
        
        publisher.setServiceEndDateMillis(serviceEndDateMillis);
        publisher.setJsonToken(GUIDGenerator.createId().replaceAll("-", ""));
        publisher.setSubscriptionStatus("SUBSCRIBER");
               
        
        orderManagerService.saveOrder(o);
        
        //enable the user
        publisher.getUserPrincipal().setEnabled(true);
        publisherService.save(publisher);
	}
	

}
