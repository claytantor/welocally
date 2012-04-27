package com.sightlyinc.ratecred.service;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noi.utility.spring.service.BLMessage;
import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.dao.BaseDao;
import com.sightlyinc.ratecred.dao.OrderLineDao;
import com.sightlyinc.ratecred.dao.PublisherDao;
import com.sightlyinc.ratecred.dao.SiteDao;
import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.OrderLine;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.model.Site;


@Service
public class SiteServiceImpl extends AbstractTransactionalService<Site>
		implements SiteService {
    
    static Logger logger = 
        Logger.getLogger(SiteServiceImpl.class);
	
    @Autowired
    private SiteDao siteDao;
    
    @Autowired
    private PublisherDao publisherDao;
    
    @Autowired
    private OrderLineDao orderLineDao;
    
    
	@Override
	public BaseDao<Site> getDao() {
		return siteDao;
	}

    @Override
	public List<Site> findBySiteUrl(String siteUrl) {
       return siteDao.findByUrl(siteUrl);
	}

	@Override
    public List<Site>  findByName(String name) {
    	throw new RuntimeException("NO IMPL");
    }
  

	@Override
	public void delete(Site entity) {		
		super.delete(entity);
	}

    @Override
    /**
     * needs to check that a new site can be created based on the 
     * active orders
     */
    public Long saveSiteWithChecks(Site entity) throws BLServiceException {
        
        BLServiceException blexception = new BLServiceException();
        
        if(StringUtils.isEmpty(entity.getName())){
            blexception.getMessages().add(new BLMessage("The name is required for a new " +
                    "site to be created.", 1304));
        }
        
        if(StringUtils.isEmpty(entity.getUrl())){
            blexception.getMessages().add(new BLMessage("The URL of the site is required for a new " +
                    "site to be created.", 1305));
        }
        
        String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(entity.getUrl());
        
        if(!matcher.matches()){
            blexception.getMessages().add(new BLMessage("The URL of the site looks invalid. ",1306));
        }
        
        //String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        
        Publisher p = publisherDao.findByPrimaryKey(entity.getPublisher().getId());
        
              
        List<Order> activeOrders = 
            filter(org.hamcrest.Matchers.hasProperty("status", org.hamcrest.Matchers.anyOf(
                    org.hamcrest.Matchers.equalTo(Order.OrderStatus.PROVISIONED),
                    org.hamcrest.Matchers.equalTo(Order.OrderStatus.REGISTERED),
                    org.hamcrest.Matchers.equalTo(Order.OrderStatus.SUBSCRIBED))), 
                    p.getOrders());
        
        Integer remaining = -1*p.getSites().size();
               
        for (Order order : activeOrders) {           
            List<OrderLine> siteLines =
                orderLineDao.findByNameAndOrder("Site License", order);
            
            int totalQty = sum(siteLines, on(OrderLine.class).getQuantityOrig());
            remaining = remaining+totalQty;
            
        }
        
        logger.debug("remaining:"+remaining);
        if(remaining<=0){
            blexception.getMessages().add(new BLMessage("You cannot create any more sites for this publisher, the number of sites exceeds the license.", 1302));
        }
        
        
        if(blexception.getMessages().size()>0){
            throw blexception;
        } 
        
        super.save(entity);
        return entity.getId();
        
       
    }
	
	
	
	


}
