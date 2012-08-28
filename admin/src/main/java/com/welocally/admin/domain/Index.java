package com.welocally.admin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.sightlyinc.ratecred.model.BaseEntity;
import com.welocally.admin.security.UserPrincipal;

@Entity
@JsonIgnoreProperties(ignoreUnknown=true)

@Table(name="ws_index")
public class Index extends BaseEntity  {
    
 
    @Column(name = "ws_id")
    private String worksheetFeed;
    
  /*
   * id, version, ss_key, owner_user_principal_id, ws_id, name, time_created, time_updated, pk_field, search_fields, lat_field, lng_field
--------------
id               bigint(20) PK
version          bigint(20)
ss_key           varchar(128)
owner_user_principal_id bigint(20)
ws_id            varchar(255)
name             varchar(255)
time_created     bigint(20)
time_updated     bigint(20)
pk_field         varchar(128)
search_fields    text
lat_field        varchar(128)
lng_field        varchar(128)
  
   */
    @Column(name = "pk_field")
    private String primaryKey;
    
    @Column(name = "search_fields")
    private String searchFields;
    
    @Column(name = "lat_field")
    private String lat;
    
    @Column(name = "lng_field")
    private String lng;
    
    private String name;
    
    @OneToOne
    @JoinColumn(name="owner_user_principal_id")
    private UserPrincipal owner;
    

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getWorksheetFeed() {
        return worksheetFeed;
    }
    public void setWorksheetFeed(String worksheetFeed) {
        this.worksheetFeed = worksheetFeed;
    }
    public UserPrincipal getOwner() {
        return owner;
    }
    public void setOwner(UserPrincipal owner) {
        this.owner = owner;
    }
    public String getPrimaryKey() {
        return primaryKey;
    }
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
    public String getSearchFields() {
        return searchFields;
    }
    public void setSearchFields(String searchFields) {
        this.searchFields = searchFields;
    }
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLng() {
        return lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }
    
       
}
