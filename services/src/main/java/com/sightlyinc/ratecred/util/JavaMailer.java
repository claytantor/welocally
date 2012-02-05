package com.sightlyinc.ratecred.util;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Message.RecipientType;

import org.apache.log4j.Logger;
import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * #mail service
MailerQueueService.smtpHostname=smtp.gmail.com
MailerQueueService.smtpUsername=mailer@ratecred.com
MailerQueueService.smtpPassword=Mail45Head
MailerQueueService.numberThreads=2
MailerQueueService.sslProvider=true
MailerQueueService.securityType=tls
 * @author claygraham
 *
 */
@Component
public class JavaMailer {
	
	static Logger logger = Logger.getLogger(JavaMailer.class);
	
	@Value("${MailerQueueService.smtpHostname:smtp.gmail.com}")
	private String smtpHostname;	
	
	@Value("${MailerQueueService.smtpPort:587}")
	private Integer smtpPort;	
	
	@Value("${MailerQueueService.smtpUsername:mailer@ratecred.com}")
	private String smtpUsername;
	
	@Value("${MailerQueueService.smtpPassword:supersecret}")
	private String smtpPassword;
	
	@Value("${MailerQueueService.numberThreads:10}")
	private Integer numberThreads;
	
	// create ExecutorService to manage threads
    private ExecutorService threadExecutor;
    
    @PostConstruct
    public void initMailer(){
    	threadExecutor = Executors.newFixedThreadPool(numberThreads);
    }

	public void sendMessage(String fromAddressName,String fromName, String toAddressName,
			String toName, String subject, String body, String mimetype) {
		logger.debug("sending message");
		MailWorker worker = 
			new MailWorker( fromAddressName, fromName,  toAddressName,
				 toName,  subject,  body,  mimetype);
		threadExecutor.execute(worker);
			
	}
	
	private class MailWorker implements Runnable
	{
		private String fromAddressName;
		private String fromName;
		private String toAddressName;
		private String toName;
		private String subject;
		private String body;
		private String mimetype;
		
		public MailWorker(String fromAddressName,String fromName, String toAddressName,
				String toName, String subject, String body, String mimetype) {
			super();
			this.fromAddressName = fromAddressName;
			this.fromName = fromName;
			this.toAddressName = toAddressName;
			this.toName = toName;
			this.subject = subject;
			this.body = body;
			this.mimetype = mimetype;
		}



		@Override
		public void run() {
			try {
				logger.debug("running");
				
				final Email email = new Email();
				email.setFromAddress(fromName, fromAddressName);
				email.setSubject(subject);
				email.addRecipient(toName, toAddressName, RecipientType.TO);
				boolean foundType=false;
				if(mimetype.equals("text/html")){
					email.setTextHTML(body);
					foundType=true;
				} else if(mimetype.equals("text/plain")){
					email.setText(body);
					foundType=true;
				}
				
				if(foundType)
					new Mailer(smtpHostname, smtpPort, smtpUsername, smtpPassword,
							TransportStrategy.SMTP_TLS).sendMail(email);
				else
					logger.error("could not find mime type for message, only html and plain supported");

			} catch (Exception e) {
				logger.error("cannot send email", e);
			}
			
		}
		
	}

}
