package com.sightlyinc.ratecred.admin.mvc.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sightlyinc.ratecred.admin.velocity.PublisherRegistrationGenerator;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.model.Contact;
import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.Product;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.OrderService;
import com.sightlyinc.ratecred.service.ProductService;
import com.sightlyinc.ratecred.service.PublisherService;
import com.sightlyinc.ratecred.util.JavaMailer;

@Controller
@RequestMapping(value="/order")
public class OrderController {
		
	static Logger logger = Logger.getLogger(OrderController.class);
	
    @Autowired
    private UserPrincipalService userService;

	@Autowired
	private PublisherService publisherService;
	
	@Autowired
	private ProductService productService;
	
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private JavaMailer mailer;
	
	@Value("${admin.adminEmailAccountFrom:mailer@ratecred.com}")
	private String adminEmailAccountFrom;
	
	
	@ModelAttribute("products")
    public List<Product> getProducts() {
		return productService.findAll();
    }
	
	
	@RequestMapping(method=RequestMethod.GET)
	public String getCreateForm(@RequestParam Long publisherId, Model model) { 
		Order newOrder= new Order();
		Publisher publisheToCreateOrderFor = publisherService.findByPrimaryKey(publisherId);
		newOrder.setOwner(publisheToCreateOrderFor);
		model.addAttribute("orderForm", newOrder);
		return "order/edit";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid Order form, BindingResult result, Model model) {
		Order order = new Order();
		try {	
			
			if(form.getId() != null)
				order = orderService.findByPrimaryKey(form.getId());
			
			if(order!=null){
				
				
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
				order.getOwner().setSubscriptionStatus("SUBSCRIBED");
				
				
				//MOVE TO SERVICE
				//activate the user
				UserPrincipal up = userService.loadUser(order.getOwner().getKey());
				up.setEmail(form.getBuyerEmail());
				up.setEnabled(true);
				up.setLocked(false);
				up.setCredentialsExpired(false);
				userService.saveUserPrincipal(up);
				
				//set the status of the publisher
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

				publisherService.save(pub);

							
				Long id = orderService.save(order);
				return "redirect:/order/"+id.toString();
			}
		
		} catch(Exception e){
			logger.error("", e);
			return "error";
		}
		return "home";
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public String getById(@PathVariable Long id, Model model) {
		logger.debug("view");
		model.addAttribute("order", orderService.findByPrimaryKey(id));
		return "order/view";
	}

	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public String edit(@PathVariable Long id, Model model) {
		logger.debug("edit");
		model.addAttribute(
				"orderForm",
				orderService.findByPrimaryKey(id));
		return "order/edit";
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public String delete(@PathVariable Long id, Model model) {
		logger.debug("delete");
		Order p = orderService.findByPrimaryKey(id);
		Long publisherId = p.getOwner().getId();
		
		orderService.delete(p);
		return "redirect:/publisher/"+publisherId;
	}
	
	@RequestMapping(value="/email/{id}", method=RequestMethod.GET)
	private String email(@PathVariable Long id, Model model) 
	{
		Order o = orderService.findByPrimaryKey(id);
				
		Map genModel = new HashMap();
		genModel.put("order", o);
		genModel.put("number", new NumberTool());
		NumberTool nt = new NumberTool();
		String test = nt.currency(o.getTotal());
		
		PublisherRegistrationGenerator generator = 
			new PublisherRegistrationGenerator(genModel);
			
		mailer.sendMessage(
				adminEmailAccountFrom, 
				"Welocally Mailer Bot",
				o.getBuyerEmail(), 
				"Welocally Admin", 
				"[Welocally Admin] Order "+o.getOwner().getSubscriptionStatus(), 
				generator.makeDisplayString(),
				"text/html");
		
		return "redirect:/publisher/"+o.getOwner().getId();
		
	}
	
	
}
