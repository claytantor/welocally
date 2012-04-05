package com.sightlyinc.ratecred.service;

import static ch.lambdaj.Lambda.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noi.utility.hibernate.GUIDGenerator;
import com.noi.utility.spring.service.BLMessage;
import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.admin.velocity.PublisherRegistrationGenerator;
import com.sightlyinc.ratecred.authentication.UserNotFoundException;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.authentication.UserPrincipalServiceException;
import com.sightlyinc.ratecred.model.Contact;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.Product;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.model.Site;
import com.sightlyinc.ratecred.util.JavaMailer;

import static org.hamcrest.Matchers.*;
import static java.util.Arrays.*;

@Service
@Transactional
public class OrderManager {
    
    private static Logger logger = Logger.getLogger(OrderManager.class);
    
    @Autowired
    private PublisherService publisherService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private NetworkMemberService networkMemberService;
    
    @Autowired
    private ContactService contactService;
       
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserPrincipalService userService;
    
    @Autowired
    private JavaMailer mailer;
    
    @Value("${default.product.sku:f102b14a87a1}")
    private String freeProductSku;
    
    
    
    public void deleteOrder(Order order) throws BLServiceException {
        
        try {
            if(order.getOwner()!=null){
                //remove roles associated with product
                UserPrincipal up = userService.loadUser(order.getOwner().getKey());
                //add the product roles          
                userService.removeRoles(
                        up, Arrays.asList(
                                order.getProduct().getRoles().split(",")));
                
          
                userService.saveUserPrincipal(up);
            }
            
            orderService.delete(order);
        } catch (UserPrincipalServiceException e) {
            throw new BLServiceException(e);
        } catch (UserNotFoundException e) {
            throw new BLServiceException(e);
        }
    }

    /**
     * does the work of saving all relevant data like roles
     * based on order
     * 
     * @param form
     * @param order
     * @return
     * @throws BLServiceException
     */
    public Long save(Order form, Order order) throws BLServiceException
    {
        if(form.getId() != null)
            order = orderService.findByPrimaryKey(form.getId());
        
        if(order!=null){
            
            
            try {
                Product p = productService.findByPrimaryKey(form.getProduct().getId());
                Publisher pub = publisherService.findByPrimaryKey(form.getOwner().getId());
                orderService.setOrderProduct(order,p);
                order.setExternalTxId(form.getExternalTxId());
                order.setBuyerKey(form.getOwner().getKey());
                order.setBuyerEmail(form.getBuyerEmail());
                order.setOwner(pub);
                order.setStatus(Order.OrderStatus.PROVISIONED);
                order.setTimeCreated(Calendar.getInstance().getTimeInMillis());
                order.setTimeUpdated(Calendar.getInstance().getTimeInMillis());
                order.setChannel("WELOCALLY_ADMIN");
                order.getOwner().setSubscriptionStatus(Publisher.PublisherStatus.SUBSCRIBED);
                
                
                //activate the user
                UserPrincipal up = userService.loadUser(order.getOwner().getKey());
                //add the product roles          
                userService.addRoles(
                        up, Arrays.asList(
                                order.getProduct().getRoles().split(",")));
                
                up.setEmail(form.getBuyerEmail());
                up.setEnabled(true);
                up.setLocked(false);
                up.setCredentialsExpired(false);
                userService.saveUserPrincipal(up);
                
                //set the status of the publisher
                //see if contact already exists
                List<Contact> contactsForEmail = contactService.findByEmailAndPublisher(form.getBuyerEmail(), pub);
                if(contactsForEmail.size() == 0){
                    Contact c = new Contact();
                    c.setFirstName("");
                    c.setLastName("");
                    c.setEmail(form.getBuyerEmail());
                    c.setActive(true);
                    
                    if(pub.getContacts() != null){
                        pub.getContacts().add(c);
                    } else {
                        Set<Contact> contacts = new HashSet<Contact>();
                        contacts.add(c);
                        pub.setContacts(contacts);
                    }
                } else {
                    Contact c = contactsForEmail.get(0);
                    pub.getContacts().add(c);
                }

                publisherService.save(pub);
                            
                return orderService.save(order);
                
            } catch (UserPrincipalServiceException e) {
                throw new BLServiceException(e);
            } catch (UserNotFoundException e) {
                throw new BLServiceException(e);
            }
        }
        
        return null;

    }
    
    public void checkSiteForPublisher(
            Publisher p,
            String email, 
            String key, 
            String siteToken,
            String siteName, 
            String siteUrl) throws BLServiceException {
        
        //verify that the site exists
        BLServiceException blexception = new BLServiceException();
        
        if(p== null && !StringUtils.isEmpty(siteToken)){
            blexception.getMessages().add(new BLMessage("A token has been entered, but no publisher can be found with the key:"+key+
                    ". Please remove your token to begin the registration process or contact welocally if you have any questions.", 1201));
        }
        
        if(p!= null && !StringUtils.isEmpty(siteToken) && !p.getJsonToken().equals(siteToken)){
            blexception.getMessages().add(new BLMessage("The site token did not match our records " +
            		"for the publisher with key:"+key+
                    ". Please check your token or contact welocally if you have any questions.", 1201));
        }
            
        if(p!= null){
            List<Site> urlSites = 
                filter(org.hamcrest.Matchers.hasProperty("url", org.hamcrest.Matchers.equalTo(siteUrl)), 
                        p.getSites());
            if(urlSites.size() == 0){
                blexception.getMessages().add(new BLMessage("The site:"+siteUrl+
                        " cannot be found as a licenced site for the publisher with key:"+key+
                        ". Please check your token or contact welocally if you have any questions.", 1201));       
                
            }
            
        }
        
        
        
        if(blexception.getMessages().size()>0){
            throw blexception;
        }
        
        

        
    }
    
    public Publisher processPublisherRegistration(
            String email, 
            String key, 
            String siteToken,
            String siteName, 
            String siteUrl, 
            Boolean verified,
            String adminEmailAccountFrom) throws BLServiceException {
        
        
        try {
            
                               
            //make the user if they dont exist
            if(StringUtils.isEmpty(key)){
                BLServiceException blexception = new BLServiceException();
                blexception.getMessages().add(new BLMessage("The key you entered was empty. " +
                		"Please check your key or contact welocally if you have any questions.", 1201));
                throw blexception;
                
            }
            
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
                    BLServiceException blexception = new BLServiceException();
                    blexception.getMessages().add(new BLMessage("There was a problem finding the user with the key:"+key+
                            ". Please check your key or contact welocally if you have any questions.", 1201));
                    throw blexception;
                    
                    //throw new BLServiceException(e);
                }
            } 
            
            Publisher publisher = publisherService.findByPublisherKey(key);
            checkSiteForPublisher(publisher, email, key, siteToken, siteName, siteUrl);
            
            if(publisher == null){
                
                //make the publisher
                publisher = new Publisher();
                publisher.setUserPrincipal(user);
                
                Site s = new Site();
                s.setName(siteName);
                s.setUrl(siteUrl);
                s.setActive(true);
                s.setVerified(verified);

                Set<Site> sites = new HashSet<Site>();
                sites.add(s);
                publisher.setSites(sites);
                
                publisher.setName(siteName);
                NetworkMember defaultNetworkMember = networkMemberService.getDefaultNetworkMember();
                publisher.setNetworkMember(defaultNetworkMember);

                publisher.setKey(key);
                publisher.setSubscriptionStatus(Publisher.PublisherStatus.REGISTERED);
                                 
                publisherService.save(publisher);
                                        
                //ok go find the free product and use it to build the order
                Product freeProduct = productService.findProductBySku(freeProductSku);
                Order o = orderService.makeOrderFromProduct(freeProduct);
                
                o.setStatus(Order.OrderStatus.REGISTERED);
                o.setBuyerEmail(email);
                o.setBuyerKey(key);              
                o.setExternalPayerId(email);
                o.setTimeCreated(Calendar.getInstance().getTimeInMillis());
                o.setTimeUpdated(Calendar.getInstance().getTimeInMillis());
                o.setChannel("SELF_SIGNUP");
                                             
                //complete subscription
                logger.debug("processing order:"+o.getExternalTxId());
                               
                if(publisher != null){
                    orderService.save(o);   
                    processPublisherOrder(
                            publisher, 
                            o, 
                            adminEmailAccountFrom); 
                }
                
            } else if (publisher != null && !StringUtils.isEmpty(siteToken) && 
                        publisher.getJsonToken().equals(siteToken)){   //token match
                
                //REGISTERED    
                switch(publisher.getSubscriptionStatus()){
                case REGISTERED:
                {
                    
                    Site s = new Site();
                    s.setName(siteName);
                    s.setUrl(siteUrl+"roo");
                    
                    //verify that the site exists
                    List<Site> urlSites = 
                        filter(
                                org.hamcrest.Matchers.hasProperty("url", org.hamcrest.Matchers.equalTo(s.getUrl())), 
                                publisher.getSites());
                                      
                                     
                    //activate the user, this is the first registration
                    UserPrincipal up = userService.loadUser(publisher.getKey());
                    up.setEmail(email);
                    up.setEnabled(true);
                    up.setLocked(false);
                    up.setCredentialsExpired(false);
                    userService.saveUserPrincipal(up);
                    
                    //set the status of the publisher
                    Contact c = new Contact();
                    c.setEmail(email);
                    c.setActive(true);
                    c.setFirstName("");
                    c.setLastName("");
                    publisher.setSubscriptionStatus(Publisher.PublisherStatus.SUBSCRIBED);
                    if(publisher.getContacts() != null){
                        publisher.getContacts().add(c);
                    } else {
                        Set<Contact> contacts = new HashSet<Contact>();
                        contacts.add(c);
                        publisher.setContacts(contacts);
                    }
                    break;
                }
                case SUBSCRIBED:{
                    /* use may be trying to add a new site to their order*/
                    Site s = new Site();
                    s.setName(siteName);
                    s.setUrl(siteUrl);
                    
                    //only add site if thier order allows it and is not already added
                    if(!publisher.getSites().contains(s)){
                        for (Order o : publisher.getOrders()) {
                            o.getOrderLines();
                            
                            
                        }
                        
                    }
                    
                    
                    break;
                }
                }
                publisherService.save(publisher);
                
            }
            
            return publisher;

        } catch (UserPrincipalServiceException e) {
            BLServiceException blexception = new BLServiceException();
            blexception.getMessages().add(new BLMessage("There was a problem finding the user with the key:"+key+
                    ". Please check your key or contact welocally if you have any questions.", 1201));
            throw blexception;
        } catch (UserNotFoundException e) {
            BLServiceException blexception = new BLServiceException();
            blexception.getMessages().add(new BLMessage("There was a problem finding the user with the key:"+key+
                    ". Please check your key or contact welocally if you have any questions.", 1201));
            throw blexception;
        }

    }
    
    public void processPublisherOrder(Publisher publisher, Order o, String accountFrom) throws BLServiceException {
        
        logger.debug("processPublisherOrder");
                   
        
        try {
            long serviceEndDateMillis = new Date().getTime();
            
            serviceEndDateMillis += (2592000000L*1);
            
            String publisherToken = GUIDGenerator.createId().replaceAll("-", "");
            
            //update the password to be the key
            UserPrincipal up = userService.loadUser(publisher.getKey());
            String hashedPass = userService.makeMD5Hash(publisherToken);                    
            up.setPassword(hashedPass);

            List<String> roles = new ArrayList<String>(Arrays.asList(new String[]{"ROLE_USER", "ROLE_PUBLISHER"}));
            List<String> productRoles = Arrays.asList(o.getProduct().getRoles().split(","));
            roles.addAll(productRoles);
            
            
            userService.activateWithRoles(up, roles);
            
            publisher.setServiceEndDateMillis(serviceEndDateMillis);
            publisher.setJsonToken(publisherToken);
            publisher.setSubscriptionStatus(Publisher.PublisherStatus.REGISTERED);
            o.setOwner(publisher);   
            
            
            orderService.save(o);
            
            //enable the user
            publisher.getUserPrincipal().setEnabled(true);
            publisherService.save(publisher);
            

            sendOrderStatusEmail(o,accountFrom); 

            
        } catch (UserPrincipalServiceException e) {
            logger.error("could not find user problem",e);
        } catch (UserNotFoundException e) {
            logger.error("cound not find user problem",e);
        } catch(Exception e){
            logger.error("problem",e);
        }
        
        
    }

    public void sendOrderStatusEmail(Order o, String accountFrom) 
    {
                
        Map model = new HashMap();
        model.put("order", o);
        model.put("number", new NumberTool());
        
        PublisherRegistrationGenerator generator = 
            new PublisherRegistrationGenerator(model);
            
        mailer.sendMessage(
                accountFrom, 
                "Welocally Mailer Bot",
                o.getBuyerEmail(), 
                "Welocally Admin", 
                "[Welocally Admin] Registration "+o.getOwner().getSubscriptionStatus(), 
                generator.makeDisplayString(),
                "text/html");       
    }   
    
}
