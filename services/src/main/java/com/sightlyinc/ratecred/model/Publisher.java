package com.sightlyinc.ratecred.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.interceptor.PersistenceObservable;

@PersistenceObservable
@Entity
@Table(name="publisher")
public class Publisher extends BaseEntity {
	
	public enum PublisherStatus { 
		KEY_ASSIGNED, CANCELLED, REGISTERED, SUBSCRIBED, FAILURE, SUSPENDED ;
		
		public String getDisplayValue(){
			return this.name();
		}
	}

	@Column(name="name")
	private String name;
	
	@Column(name="key_value")
	private String key;
	
	@Column(name="description",columnDefinition="TEXT")
	private String description;	
		
	@Column(name="icon_url")
	private String iconUrl;
	
	@Column(name="map_icon_url")
	private String mapIconUrl;

    @Column(name="json_token")
    private String jsonToken;

    @Column(name="service_end_date")
    private Long serviceEndDateMillis;
    
    @Column(name="subscription_status")
    @Enumerated(EnumType.STRING)
    private PublisherStatus subscriptionStatus;
	
	@ManyToOne	
	@JoinColumn(name = "network_member_id")
	private NetworkMember networkMember;
	
	
	@OneToMany(cascade = {CascadeType.ALL })
	@JoinColumn(name = "publisher_id")
	public Set<Site> sites;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name = "publisher_id")
	public Set<Contact> contacts;


    @OneToOne
    @JoinColumn(name="user_principal_id")
    @JsonIgnore
    private UserPrincipal userPrincipal;


	@OneToMany
	@JoinColumn(name = "publisher_id")
	private Set<Order> orders;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	

	public NetworkMember getNetworkMember() {
		return networkMember;
	}
	public void setNetworkMember(NetworkMember networkMember) {
		this.networkMember = networkMember;
	}
	    
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getMapIconUrl() {
		return mapIconUrl;
	}
	public void setMapIconUrl(String mapIconUrl) {
		this.mapIconUrl = mapIconUrl;
	}

    public UserPrincipal getUserPrincipal() {
        return userPrincipal;
    }

    public void setUserPrincipal(UserPrincipal userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

    public String getJsonToken() {
        return jsonToken;
    }

    public void setJsonToken(String simpleGeoJsonToken) {
        this.jsonToken = simpleGeoJsonToken;
    }

    public Long getServiceEndDateMillis() {
        return serviceEndDateMillis;
    }

    public Date getServiceEndDate() {
        return (serviceEndDateMillis == null ? null : new Date(serviceEndDateMillis));
    }

    public void setServiceEndDateMillis(long serviceEndDateMillis) {
        this.serviceEndDateMillis = serviceEndDateMillis;
    }

    public void setServiceEndDate(Date serviceEndDate) {
        this.serviceEndDateMillis = serviceEndDate.getTime();
    }
    
	public void setServiceEndDateMillis(Long serviceEndDateMillis) {
		this.serviceEndDateMillis = serviceEndDateMillis;
	}

    public PublisherStatus getSubscriptionStatus() {
    	return subscriptionStatus;
    }
	public void setSubscriptionStatus(PublisherStatus subscriptionStatus) {
    	this.subscriptionStatus = subscriptionStatus;
    }
	public boolean isServiceExpired() {
        return (serviceEndDateMillis == null || serviceEndDateMillis < new Date().getTime());
    }
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<Site> getSites() {
		return sites;
	}
	public void setSites(Set<Site> sites) {
		this.sites = sites;
	}
	public Set<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}
    
    
    
}
