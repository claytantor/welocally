package com.sightlyinc.ratecred.admin.tag;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.log4j.Logger;

/**
 * shoudl be refactored and put in its own project, will be used by both web applications
 * 
 * @author claygraham
 *
 */
public class AmazonCartTag extends TagSupport {
	
	static Logger logger = Logger.getLogger(AmazonCartTag.class);
	
	//should not be hard coded
	private String merchantID = "A1K9S6OUGRSQ4N";
	private String accessKeyID = "1H8AMWP9WTKKHVMV5782";
	private String secretKeyID = "yAao21CqcUye2SItpFpmLFZ1SnenN5CaFEAacxIR";
	
	private String itemTitle;
	private String itemSku;
	private String itemDescription;
	private String itemPrice;
	private String environment;
		
	@Override
	public int doStartTag() throws JspException {
		
		try {

			MerchantHTMLCartFactory factory = new MerchantHTMLCartFactory();
			SignatureCalculator calculator = new SignatureCalculator();
			String cart = factory.getSignatureInput(merchantID, accessKeyID);
			String merchantSignature = new String(calculator.calculateRFC2104HMAC(
					cart.getBytes(), secretKeyID));
			String cartHTML = factory.getCartHTML(merchantID, itemSku, accessKeyID, merchantSignature);
			pageContext.getOut().println(cartHTML);
			
		} catch (URIException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;

		
	}
	
	public int doEndTag() {
		return EVAL_PAGE;
	}

	
	private class SignatureCalculator {

		private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

		public SignatureCalculator() {
		}

		/**
		 * Computes RFC 2104-compliant HMAC signature.
		 * 
		 * @param data
		 *            The data to be signed.
		 * @param key
		 *            The signing key, a.k.a. the AWS secret key.
		 * @return The base64-encoded RFC 2104-compliant HMAC signature.
		 * @throws java.security.SignatureException
		 *             when signature generation fails
		 */
		public byte[] calculateRFC2104HMAC(byte[] data, String key)
				throws SignatureException {
			byte[] result = null;

			try {
				// get an hmac_sha1 key from the raw key bytes
				SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),
						HMAC_SHA1_ALGORITHM);

				// get an hmac_sha1 Mac instance and initialize with the signing key
				Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
				mac.init(signingKey);

				// compute the hmac on input data bytes
				mac.update(data);
				byte[] rawHmac = mac.doFinal();

				// base64-encode the hmac
				result = Base64.encodeBase64(rawHmac);
			} catch (Exception e) {
				throw new SignatureException("Failed to generate HMAC: ", e);
			}
			
			return result;
		}
	}

	private abstract class CartFactory {
		// Strings to construct the final cart form, with regular expression replacements
		// indicated via [YOUR REPLACEMENT VALUE]
		protected static final String CART_JAVASCRIPT_START = "<script type=\"text/javascript\" " +
				"src=\"https://images-na.ssl-images-amazon.com/images/G/01/cba/js/jquery.js\"></script>\n" +
				"<script type=\"text/javascript\" src=\"https://images-na.ssl-images-amazon.com/images/G/01/cba/js/widget/widget.js\"></script>\n";
		
		protected static final String CART_FORM_START_PROD = "<form id=\"form_[SKU]\" method=\"POST\" action=\"http://payments.amazon.com/checkout/[MERCHANT_ID]\">\n";
		protected static final String CART_FORM_START_SANDBOX = "<form id=\"form_[SKU]\" method=\"POST\" action=\"http://payments-sandbox.amazon.com/checkout/[MERCHANT_ID]\">\n";

		protected static final String CART_FORM_INPUT_FIELD = "<input type=\"hidden\" name=\"[KEY]\" value=\"[VALUE]\" />\n";

		protected static final String CART_FORM_SIGNATURE_INPUT_FIELD = "<input type=\"hidden\" name=\"merchant_signature\" value=\"[SIGNATURE]\" />\n";

		protected static final String CART_FORM_END = "</form>";
		
		/*protected static final String CART_FORM_BUTTON_INPUT_FIELD = "<input type=\"image\" "
			+ "src=\"https://payments.amazon.com/gp/cba/button?"
			+ "ie=UTF8&color=orange&background=white&size=medium\" alt=\"Checkout with Amazon Payments\" />\n";*/
		


		/**
		 * Gets cart html fragment used to generate entire cart html
		 * 
		 * @param merchantID
		 * @param awsAccessKeyID
		 * @return
		 * @throws URIException
		 */
		public abstract String getCart(String merchantID, String awsAccessKeyID)
		throws URIException;
		
		
		/**
		 * Returns the concatenated cart used for signature generation.
		 * 
		 * @param merchantID
		 * @param awsAccessKeyID
		 * @return
		 * @throws URIException
		 */
		public abstract String getSignatureInput(String merchantID, String awsAccessKeyID)
				throws URIException;

		/**
		 * Returns a finalized full cart html including the base 64 encoded cart,
		 * signature, and buy button image link.
		 * 
		 * @param merchantID
		 * @param awsAccessKeyID
		 * @param signature
		 * @return
		 * 
		 * @throws URIException
		 */
		public abstract String getCartHTML(String merchantID, String itemSku, String awsAccessKeyID, String signature);
	}	
	
	private abstract class HTMLCartFactory extends CartFactory {
		/**
		 * Returns html fragment used to generate complete html cart.
		 * 
		 * @see CartFactory
		 */
		public String getCart(String merchantID, String awsAccessKeyID) {
			Map<String, String> parameterMap = getCartMap(merchantID, awsAccessKeyID);
			
			StringBuffer cart = new StringBuffer();
			
			// add lines to the cart html fragment like:
			// <input name="item_title_1" value="Red Fish" type="hidden" />
			for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
				cart.append(CART_FORM_INPUT_FIELD.replaceAll("\\[KEY\\]", entry.getKey()).
						replaceAll("\\[VALUE\\]", entry.getValue()));
			}
			
			return cart.toString();
		}

		/**
		 * Generates the finalized cart html, including javascript headers, cart contents,
		 * signature and button.
		 * 
		 */
		public String getCartHTML(String merchantID, String itemSku, String awsAccessKeyID, String signature) {
			StringBuffer cartHTML = new StringBuffer();
			
			//scripts
						
			if(environment.equalsIgnoreCase("SANDBOX")) {
				cartHTML.append(
						CART_FORM_START_SANDBOX
						.replaceAll("\\[SKU\\]", itemSku)
						.replaceAll("\\[MERCHANT_ID\\]", merchantID));
			}
			else 
				cartHTML.append(CART_FORM_START_PROD.replaceAll("\\[MERCHANT_ID\\]", merchantID));
			
			cartHTML.append(getCart(merchantID, awsAccessKeyID));
			cartHTML.append(CART_FORM_SIGNATURE_INPUT_FIELD.replaceAll("\\[SIGNATURE\\]", signature));
			
			//cartHTML.append(CART_FORM_BUTTON_INPUT_FIELD);
				
			cartHTML.append(CART_FORM_END);

			return cartHTML.toString();
		}
		


		/**
		 * Generates the signature input - basically a contenation of all url parameters.
		 * Doesn't handle full url specification, since it
		 * doesn't handle parameter value of arrays - just assumes each parameter
		 * value is a basic string.
		 * 
		 * Checkout by Amazon only supports this format as well, so that is
		 * perfectly fine.
		 * 
		 * @see CartFactory
		 */
		protected String getSignatureInput(Map<String, String> parameterMap)
				throws URIException {
			StringBuilder stringBuilder = new StringBuilder();
			Map<String, String> sortedParameterMap = new TreeMap<String, String>(
					parameterMap);

			/*
			 * Assumes url parameters are in a Map named parameterMap where the key
			 * is the parameter name.
			 */
			for (Map.Entry<String, String> entry : sortedParameterMap.entrySet()) {
				stringBuilder.append(entry.getKey());
				stringBuilder.append("=");
				stringBuilder.append(URIUtil.encodeWithinQuery(entry.getValue()));
				stringBuilder.append("&");
			}

			return stringBuilder.toString();
		}

		protected abstract Map<String, String> getCartMap(String merchantID,
				String awsAccessKeyID);
	}
	
	private class MerchantHTMLCartFactory extends HTMLCartFactory {

		/**
		 * Replace with your own cart here to try out
		 * different promotions, tax, shipping, etc. 
		 * 
		 * @param merchantID
		 * @param awsAccessKeyID
		 * @return
		 */
		protected Map<String, String> getCartMap(String merchantID, String awsAccessKeyID) {
			Map<String, String> parameterMap = new TreeMap<String, String>();

			parameterMap.put("item_merchant_id_1", merchantID);
			parameterMap.put("item_title_1", itemTitle.replaceAll("[^a-zA-Z0-9.% ]", ""));
			parameterMap.put("item_sku_1", itemSku);
			parameterMap.put("item_description_1", itemDescription.replaceAll("[^a-zA-Z0-9.% ]", ""));
			parameterMap.put("item_price_1", itemPrice);
			parameterMap.put("item_quantity_1", "1");
			parameterMap.put("currency_code", "USD");
			parameterMap.put("item_custom_data", "");
			parameterMap.put("cart_custom_data", "");
			parameterMap.put("aws_access_key_id", awsAccessKeyID);
			

			return parameterMap;
		}
		
		/**
		 * Construct a very basic cart with one item.
		 */
		public String getSignatureInput(String merchantID, String awsAccessKeyID)
				throws URIException {

			return getSignatureInput(getCartMap(merchantID, awsAccessKeyID));
		}




	}


	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public void setItemSku(String itemSku) {
		this.itemSku = itemSku;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	

}
