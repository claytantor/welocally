package com.sightlyinc.ratecred.admin.harvest.plugin;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.ScraperContext;
import org.webharvest.runtime.processors.WebHarvestPlugin;
import org.webharvest.runtime.variables.NodeVariable;
import org.webharvest.runtime.variables.Variable;
import org.webharvest.utils.CommonUtil;


public class GracefulHttpPlugin extends WebHarvestPlugin {
	
	private static final Log logger = LogFactory.getLog(GracefulHttpPlugin.class);

	public String[] getRequiredAttributes() {
		return new String[] {"url"};
	}

	public String[] getValidAttributes() {
		return new String[] {"url"};
	}

	public Variable executePlugin(Scraper scraper, ScraperContext context) {
		
		String url = CommonUtil.nvl(evaluateAttribute("url", scraper), "");
		this.setProperty("Url", url);
		
		HttpClient client = new HttpClient();

		BufferedReader br = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		GetMethod method = new GetMethod(url);
		
		try {
			
			logger.debug(method.getURI().toString());
			
			int returnCode = client.executeMethod(method);
		
			if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
				logger.error("The GET method is not implemented by this URI");
			
			} else {
				IOUtils.copy(
						method.getResponseBodyAsStream(), baos);				
				baos.flush();
				return new NodeVariable(baos.toByteArray());
				
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			method.releaseConnection();
		}
		
		return new NodeVariable("".toString().getBytes());
	}

	public String getName() {
		return "ghttp";
	}


	

}
