package com.sightlyinc.ratecred.client.offers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.noi.utility.date.DateUtils;
import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.pojo.Location;

/**
 * 
{
    "visible": "true",
    "state": "CA",
    "type": "deal",
    "externalId": "17c61c45-e833-4fbf-af13-2b5d64864b76",
    "city": "Oakland",
    "id": "0",
    "programId": "b93acd058af5",
    "discountType": "deal",
    "description": "Six pounds of your favorite flavors for a great price! Choose any grind or who bean, we will make it just like you want it!",
    "beginDateString": "2011-04-10",
    "name": "Six Pounds of Any Grind",
    "value": "60.0",
    "quantity": null,
    "externalSource": "Oaklandly",
    "programName": "Oaklandly",
    "expire": "Tue Apr 10 00:00:00 PDT 2012",
    "ends": "Tue Apr 10 00:00:00 PDT 2012",
    "offerLocation": {
        "addressTwo": null,
        "postalCode": "null",
        "addressOne": "300 Webster Street",
        "name": null,
        "state": "CA",
        "lng": "-122.27278",
        "lat": "37.798027",
        "comments": null,
        "city": "Oakland"
    },
    "expireDateString": "2012-04-10",
    "extraDetails": "Does not apply to Sumatra or Hawaiian flavors ",
    "illustrationUrl": "http://oaklandunwrapped.org/bluebottle/graphics/logo.gif",
    "begin": "Sun Apr 10 00:00:00 PDT 2011",
    "price": "39.99",
    "checkinsRequired": null,
    "items": "[]",
    "advertiser": {
        "siteUrl": null,
        "id": null,
        "contactPhone": null,
        "locations": [
            {
                "addressTwo": null,
                "postalCode": "null",
                "addressOne": "300 Webster Street",
                "name": null,
                "state": "CA",
                "lng": "-122.27278",
                "lat": "37.798027",
                "comments": null,
                "city": "Oakland"
            }
        ],
        "description": "A slightly disaffected freelance musician and coffee lunatic, weary of the grande eggnog latte, and the double skim pumpkin-pie macchiato, decides to open a roaster for people who are clamoring for the actual taste of freshly roasted coffee. Using a miniscule six-pound batch roaster, he makes an historic vow: \"I will only sell coffee less than 48 hours out of the roaster to my customers, so they may enjoy coffee at its peak of flavor. I will only use the finest organic, and pesticide-free, shade-grown beans. If they can't come to me, I will drive to their house to give them the freshest coffee they have ever tasted.\" In honor of Kolshitsky's heroics, he names his business The Blue Bottle Coffee Company, and begins another chapter in the history of superlative coffee.",
        "name": "Blue Bottle Coffee Co.",
        "categoryId": null,
        "advertiserLogoUrl": null,
        "externalId": null
    },
    "endDateString": "2012-04-10"
}
 * @author claygraham
 *
 */
@JsonIgnoreProperties({ "expire", "ends", "begin", "items"  })
public class OfferOld {
	
	static Logger logger = Logger.getLogger(OfferOld.class);
	
	private Long id = new Long(0);
	
	@JsonProperty
	private String externalId = "";
	
	
	@JsonProperty
	private String externalSource ="";
	
	@JsonProperty
	private String programId="";
	
	@JsonProperty
	private String programName="";
	
	@JsonProperty
	private String name="";
	
	@JsonProperty
	private String couponCode="";
	
	@JsonProperty
	private String description="";
	
	@JsonProperty
	private String url="";
	
	//these could probably be native Longs
	@JsonProperty
	private String beginDateString="";
	
	@JsonProperty("")
	private String expireDateString="";
	
	@JsonProperty
	private String endDateString="";
	
	@JsonProperty
	private String city="";
	
	@JsonProperty
	private String state="";
	
	private String discountType;
	
	
	private String type;
	private Float price;
	private Float discountValue;
	private Float value;
	private String extraDetails;
	private String illustrationUrl;
	private Integer quantity;
	private Integer checkinsRequired;
	
	@JsonIgnore
	private Integer score;
	
	private Location offerLocation;
	
	private List<Item> items;
	
	private Advertiser advertiser;
	
		
	private boolean visible = true;
	
	@JsonIgnore
	private PropertyChangeSupport changes = new PropertyChangeSupport( this );
		
	public OfferOld() {
		super();
		items = new ArrayList<Item>();	
		score = new Integer(0);
	}

	public boolean fieldsEmpty()
	{
		if(StringUtils.isEmpty(couponCode) || StringUtils.isEmpty(name) || StringUtils.isEmpty(description))
			return true;
		else
			return false;
	}
	
	public void addScore(int value) {
		logger.debug("adding:"+value+" to score:"+this.score+" name:"+this.getName());
		this.score = this.score+value;
	}
	
	@JsonIgnore
	public Integer getScore() {
		return score;
	}

	@JsonIgnore
	public void setScore(int score) {
		logger.debug("setting score:"+score+" name:"+this.getName());
		this.score = score;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
		

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getExternalSource() {
		return externalSource;
	}
	public void setExternalSource(String externalSource) {
		this.externalSource = externalSource;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBeginDateString() {
		return beginDateString;
	}
	public void setBeginDateString(String beginDateString) {
		this.beginDateString = beginDateString;
	}
	public String getExpireDateString() {
		return expireDateString;
	}
	public void setExpireDateString(String expireDateString) {
		this.expireDateString = expireDateString;
	}
	
	public String getEndDateString() {
		return endDateString;
	}

	public void setEndDateString(String endDateString) {
		this.endDateString = endDateString;
	}

	public Date getEnds()
	{
		return DateUtils.stringToDate(endDateString, DateUtils.DESC_SIMPLE_FORMAT);
	}
	
	@JsonIgnore
	public Date getExpire()
	{
		return DateUtils.stringToDate(expireDateString, DateUtils.DESC_SIMPLE_FORMAT);
	}
	
	public Date getBegin()
	{
		return DateUtils.stringToDate(beginDateString, DateUtils.DESC_SIMPLE_FORMAT);
	}

	public boolean isVisible() {
		return visible;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setVisible(boolean newvisible) {
		
		boolean oldvisible = visible;
		visible = newvisible;
        changes.firePropertyChange("visible", oldvisible, newvisible);
	}
	
	public void addPropertyChangeListener( PropertyChangeListener l ) {
        changes.addPropertyChangeListener( l );
    }
    
    public void removePropertyChangeListener( PropertyChangeListener l ) {
        changes.removePropertyChangeListener( l ); 
    }	   
	
	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(Float discountValue) {
		this.discountValue = discountValue;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public String getExtraDetails() {
		return extraDetails;
	}

	public void setExtraDetails(String extraDetails) {
		this.extraDetails = extraDetails;
	}

	public String getIllustrationUrl() {
		return illustrationUrl;
	}

	public void setIllustrationUrl(String illustrationUrl) {
		this.illustrationUrl = illustrationUrl;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getCheckinsRequired() {
		return checkinsRequired;
	}

	public void setCheckinsRequired(Integer checkinsRequired) {
		this.checkinsRequired = checkinsRequired;
	}

	public PropertyChangeSupport getChanges() {
		return changes;
	}

	public void setChanges(PropertyChangeSupport changes) {
		this.changes = changes;
	}

	public Advertiser getAdvertiser() {
		return advertiser;
	}

	public void setAdvertiser(Advertiser advertiser) {
		this.advertiser = advertiser;
	}

	public Location getOfferLocation() {
		return offerLocation;
	}

	public void setOfferLocation(Location offerLocation) {
		this.offerLocation = offerLocation;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Object clone = new OfferOld();
		try {
			
			clone = org.apache.commons.beanutils.BeanUtils.cloneBean(this);
			
			
		} catch (IllegalAccessException e) {
			throw new CloneNotSupportedException("cannot make clone");
		} catch (InvocationTargetException e) {
			throw new CloneNotSupportedException("cannot make clone");
		} catch (InstantiationException e) {
			throw new CloneNotSupportedException("cannot make clone");
		} catch (NoSuchMethodException e) {
			throw new CloneNotSupportedException("cannot make clone");
		}
		
		return clone;
		
	}

	@Override
	public String toString() {
		return "Offer [advertiser=" + advertiser + ", beginDateString="
				+ beginDateString + ", changes=" + changes
				+ ", checkinsRequired=" + checkinsRequired + ", city=" + city
				+ ", couponCode=" + couponCode + ", description=" + description
				+ ", discountType=" + discountType + ", discountValue="
				+ discountValue + ", expireDateString=" + expireDateString
				+ ", externalId=" + externalId + ", externalSource="
				+ externalSource + ", extraDetails=" + extraDetails + ", id="
				+ id + ", illustrationUrl=" + illustrationUrl + ", items="
				+ items + ", name=" + name + ", price=" + price
				+ ", programId=" + programId + ", programName=" + programName
				+ ", quantity=" + quantity + ", score=" + score + ", state="
				+ state + ", type=" + type + ", url=" + url + ", value="
				+ value + ", visible=" + visible + "]";
	}




	
	

}
