package com.sightlyinc.ratecred.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import com.sightlyinc.ratecred.authentication.UserPrincipal;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.sightlyinc.ratecred.interceptor.PersistenceObservable;

@PersistenceObservable
@Entity
@Table(name="publisher")
public class Publisher extends BaseEntity {
	
	private String url;
	
	@Column(name="site_name")
	private String siteName;
	
	@Column(name="key_value")
	private String key;
	
	@Column(name="description",columnDefinition="TEXT")
	private String description;	

	private String summary;	
	
	@Column(name="monthly_pageviews")
	private Integer monthlyPageviews;
	
	@Column(name="icon_url")
	private String iconUrl;
	
	@Column(name="map_icon_url")
	private String mapIconUrl;

    @Column(name="simple_geo_json_token")
    private String simpleGeoJsonToken;

    @Column(name="service_end_date")
    private long serviceEndDateMillis;
	
	@ManyToOne	
	@JoinColumn(name = "network_member_id")
	private NetworkMember networkMember;

    @OneToOne
    @JoinColumn(name="user_principal_id")
    @JsonIgnore
    private UserPrincipal userPrincipal;

	@OneToMany
	@JoinColumn(name = "publisher_id")
	private Set<OfferEconomics> offerEconomics;
	
	@OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name = "publisher_id")
	@JsonIgnore
	private Set<Event> events;
	
	@OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name = "publisher_id")
	@JsonIgnore
	private Set<Article> articles;

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}


	public Integer getMonthlyPageviews() {
		return monthlyPageviews;
	}
	public void setMonthlyPageviews(Integer monthlyPageviews) {
		this.monthlyPageviews = monthlyPageviews;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public NetworkMember getNetworkMember() {
		return networkMember;
	}
	public void setNetworkMember(NetworkMember networkMember) {
		this.networkMember = networkMember;
	}
    @JsonIgnore
	public Set<OfferEconomics> getOfferEconomics() {
		return offerEconomics;
	}
    @JsonIgnore
	public void setOfferEconomics(Set<OfferEconomics> offerEconomics) {
		this.offerEconomics = offerEconomics;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
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
	@JsonIgnore
	public Set<Event> getEvents() {
		return events;
	}
	@JsonIgnore
	public void setEvents(Set<Event> events) {
		this.events = events;
	}
	@JsonIgnore
	public Set<Article> getArticles() {
		return articles;
	}
	@JsonIgnore
	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

    public UserPrincipal getUserPrincipal() {
        return userPrincipal;
    }

    public void setUserPrincipal(UserPrincipal userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

    public String getSimpleGeoJsonToken() {
        return simpleGeoJsonToken;
    }

    public void setSimpleGeoJsonToken(String simpleGeoJsonToken) {
        this.simpleGeoJsonToken = simpleGeoJsonToken;
    }

    public long getServiceEndDateMillis() {
        return serviceEndDateMillis;
    }

    public Date getServiceEndDate() {
        return new Date(serviceEndDateMillis);
    }

    public void setServiceEndDateMillis(long serviceEndDateMillis) {
        this.serviceEndDateMillis = serviceEndDateMillis;
    }

    public void setServiceEndDate(Date serviceEndDate) {
        this.serviceEndDateMillis = serviceEndDate.getTime();
    }
}
