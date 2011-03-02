package com.sightlyinc.ratecred.client.offers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.noi.utility.date.DateUtils;
import com.noi.utility.string.StringUtils;

public class Offer {
	private Long id = new Long(0);
	private String externalId = "";
	private String externalSource ="";
	private String programId="";
	private String programName="";
	private String name="";
	private String couponCode="";
	private String description="";
	private String url="";
	private String beginDateString="";
	private String expireDateString="";
	private String city="";
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
	
	private List<Item> items;
	private Advertiser advertiser;
	
		
	private boolean visible = true;
	
	private PropertyChangeSupport changes = new PropertyChangeSupport( this );
	
	
	
	public Offer() {
		super();
		items = new ArrayList<Item>();		
	}

	public boolean fieldsEmpty()
	{
		if(StringUtils.isEmpty(couponCode) || StringUtils.isEmpty(name) || StringUtils.isEmpty(description))
			return true;
		else
			return false;
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

	@Override
	public String toString() {
		return "Offer [beginDateString=" + beginDateString + ", couponCode="
				+ couponCode + ", description=" + description
				+ ", expireDateString=" + expireDateString + ", id=" + id
				+ ", name=" + name + ", programId=" + programId
				+ ", programName=" + programName + ", url=" + url + "]";
	}
	
	

}
