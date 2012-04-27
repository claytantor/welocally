package com.sightlyinc.ratecred.service;

import static ch.lambdaj.Lambda.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.NumberTool;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noi.utility.hibernate.GUIDGenerator;
import com.noi.utility.spring.service.BLMessage;
import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.web.UrlUtils;
import com.sightlyinc.ratecred.admin.util.HttpHelperUtils;
import com.sightlyinc.ratecred.admin.util.JsonObjectSerializer;
import com.sightlyinc.ratecred.admin.velocity.PublisherRegistrationGenerator;
import com.sightlyinc.ratecred.admin.velocity.UserActivationGenerator;
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
import com.sightlyinc.ratecred.util.DigestUtils;
import com.sightlyinc.ratecred.util.JavaMailer;

@Service
@Transactional
public class OrderManagerV2 {
    
    private static Logger logger = Logger.getLogger(OrderManagerV2.class);
    
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
    
    @Autowired DigestUtils digestUtils;
    
    @Autowired GeodbProvisionManager geodbProvisionManager;
    
    @Autowired
    private JavaMailer mailer;
    
    @Value("${default.product.sku:f102b14a87a1}")
    private String freeProductSku;
    
    @Value("${registration.activationUrl:https://api.welocally.com/admin/signup/4_0/activate}")
    private String activationUrl;
    
    @Value("${geodb.admin.username}")
    String adminUser;
    
    @Value("${geodb.admin.password}")
    String adminPassword;
    
      
    
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
    
    public Publisher processActivation(
            String username, 
            String siteName, 
            String siteUrl, 
            String digestToken,
            String adminEmailAccountFrom) throws BLServiceException {
        
        try {
            UserPrincipal user = userService.findByUserName(username);
            

            Long lval = Long.parseLong(digestToken);
            if(lval.equals(user.getTimeCreated())){
                
              Publisher publisher = publisherService.findByPublisherKey(username);
              
              if(publisher == null){
                //make the publisher
                publisher = new Publisher();
                publisher.setUserPrincipal(user);
                
                Site s = new Site();
                s.setName(siteName);
                s.setUrl(siteUrl);
                s.setActive(true);
                s.setVerified(false);

                Set<Site> sites = new HashSet<Site>();
                sites.add(s);
                publisher.setSites(sites);
                
                publisher.setName(siteName);
                NetworkMember defaultNetworkMember = networkMemberService.getDefaultNetworkMember();
                publisher.setNetworkMember(defaultNetworkMember);

                publisher.setKey(username);
                publisher.setSubscriptionStatus(Publisher.PublisherStatus.REGISTERED);
                                 
                publisherService.save(publisher);
                

                                        
                //ok go find the free product and use it to build the order
                Product freeProduct = productService.findProductBySku(freeProductSku);
                Order o = orderService.makeOrderFromProduct(freeProduct);
                
                o.setStatus(Order.OrderStatus.REGISTERED);
                o.setBuyerEmail(user.getEmail());
                o.setBuyerKey(username);              
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
                
                //provision
                
                UserPrincipal up = publisher.getUserPrincipal();
                geodbProvisionManager.provision(up,adminUser,adminPassword);
                  
                  
                  
              } else {
                  BLServiceException blexception = new BLServiceException();
                  blexception.getMessages().add(new BLMessage("Publisher account already exists.", 1205));
                  throw blexception;
              }
                
                
                
            } else {
                BLServiceException blexception = new BLServiceException();
                blexception.getMessages().add(new BLMessage("Tokens did not match cannot activate.", 1206));
                throw blexception;
            }
            
            
        } catch (UserPrincipalServiceException e) {
            BLServiceException blexception = new BLServiceException();
            blexception.getMessages().add(new BLMessage("Problem finding user:"+username, 1207));
            throw blexception;
        } 

        catch (NoSuchAlgorithmException e) {
            BLServiceException blexception = new BLServiceException();
            blexception.getMessages().add(new BLMessage("Problem provisioning user:"+username, 1208));
            throw blexception;
        } catch (JSONException e) {
            BLServiceException blexception = new BLServiceException();
            blexception.getMessages().add(new BLMessage("Problem provisioning user:"+username, 1209));
            throw blexception;
        } catch (IOException e) {
            BLServiceException blexception = new BLServiceException();
            blexception.getMessages().add(new BLMessage("Problem provisioning user:"+username, 1210));
            throw blexception;
        }
        
        return null;
        
    }
    
    public UserPrincipal processPublisherRegistration(
            String email, 
            String username, 
            String password,
            String siteName, 
            String siteUrl, 
            Boolean verified,
            String adminEmailAccountFrom) throws BLServiceException {
        
        
        try {
            
                               
            //make the user if they dont exist
            if(StringUtils.isEmpty(username)){
                BLServiceException blexception = new BLServiceException();
                blexception.getMessages().add(new BLMessage("The key you entered was empty. " +
                		"Please check your key or contact welocally if you have any questions.", 1201));
                throw blexception;
                
            }
            
            UserPrincipal user = userService.findByUserName(username);
            if(user==null){
                user = new UserPrincipal();
                user.setEmail(email);
                user.setPassword(digestUtils.makeMD5Hash(password));
                user.setUsername(username);
                user.setEnabled(false);
                user.setTimeCreated(Calendar.getInstance().getTimeInMillis());
                user.setTimeUpdated(Calendar.getInstance().getTimeInMillis());
                
                try {
                    userService.saveUserPrincipal(user);
                    // not going to worry about roles for now since user isn't going to be logging in
                } catch (UserPrincipalServiceException e) {
                    logger.error("problem with request", e);
                    BLServiceException blexception = new BLServiceException();
                    blexception.getMessages().add(new BLMessage("There was a problem finding the user with the username:"+username+
                            ". Please check your key or contact welocally if you have any questions.", 1201));
                    throw blexception;

                }
                
         
                Map<String,String> model = new HashMap<String,String>();
                model.put("u", user.getUsername());
                model.put("siteName", siteName);
                model.put("siteUrl", siteUrl);
                model.put("token", user.getTimeCreated().toString());
                
                String qa = com.sightlyinc.ratecred.admin.util.UrlUtils.toQueryString(model);
                
                sendActivationEmail(user, activationUrl+"?"+qa, adminEmailAccountFrom);              
                return user;
                
            } else {
                BLServiceException blexception = new BLServiceException();
                blexception.getMessages().add(new BLMessage("Problem creating user.", 1202));
                throw blexception;
            }


        } catch (UserPrincipalServiceException e) {
            BLServiceException blexception = new BLServiceException();
            blexception.getMessages().add(new BLMessage("There was a problem registering the user with the username:"+username+
                    ". Please check your key or contact welocally if you have any questions.", 1201));
            throw blexception;
        } 
        catch (MalformedURLException e) {
            BLServiceException blexception = new BLServiceException();
            blexception.getMessages().add(new BLMessage("Problem with actvation URL", 1204));
            throw blexception;
        } catch (NoSuchAlgorithmException e) {
            BLServiceException blexception = new BLServiceException();
            blexception.getMessages().add(new BLMessage("Problem hasing password", 1214));
            throw blexception;
        }


    }
    
 
    
    
    public void processPublisherOrder(Publisher publisher, Order o, String accountFrom) throws BLServiceException {
        
        logger.debug("processPublisherOrder");
                   
        
        try {
            long serviceEndDateMillis = new Date().getTime();
            
            serviceEndDateMillis += (2592000000L*1);
            
            //update the password to be the key
            UserPrincipal up = userService.loadUser(publisher.getKey());
            
            //update the password to be the key
            String publisherToken = GUIDGenerator.createId().replaceAll("-", "");                 
//            up.setPassword(digestUtils.makeMD5Hash(up.getPassword()));

            //roles are product based
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

            
        } 
//        catch (UserPrincipalServiceException e) {
//            logger.error("could not find user problem",e);
//        } catch (UserNotFoundException e) {
//            logger.error("cound not find user problem",e);
//        } 
        catch(Exception e){
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
    
    public void sendActivationEmail(UserPrincipal up, String activationUrl, String accountFrom) throws MalformedURLException 
    {
                
        Map model = new HashMap();
        
        model.put("username", up.getUsername());    
        model.put("activationUrl", activationUrl);
        
        UserActivationGenerator generator = 
            new UserActivationGenerator(model);
            
        mailer.sendMessage(
                accountFrom, 
                "Welocally Mailer Bot",
                up.getEmail(), 
                "Welocally Admin", 
                "[Welocally Admin] Registration, Activation Required", 
                generator.makeDisplayString(),
                "text/html");       
    }   
    
}
