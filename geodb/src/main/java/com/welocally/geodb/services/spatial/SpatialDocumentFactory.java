package com.welocally.geodb.services.spatial;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.spatial.tier.projections.CartesianTierPlotter;
import org.apache.lucene.util.NumericUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.welocally.geodb.services.index.DocumentContentException;

@Component
public class SpatialDocumentFactory {
	
	
	@Autowired SpatialConversionUtils spatialConversionUtils;
	

	/**
	 * 
	 * @param placeObject
	 * @return
	 * @throws JSONException
	 */
	public Document makePlaceDocument(JSONObject placeObject) throws JSONException, DocumentContentException
	{
				
		JSONObject properties = placeObject.getJSONObject("properties");
		JSONObject geom = placeObject.getJSONObject("geometry");
		JSONArray coords = geom.getJSONArray("coordinates");
		Point coord = 
			new Point(
					Double.parseDouble(coords.getString(1)), 
					Double.parseDouble(coords.getString(0)));
		
		Document doc = new Document();
		
		if(!placeObject.isNull("_id")){
			doc.add(new Field("_id", placeObject.getString("_id"), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
			
		doc.add(new Field("name", properties.getString("name"), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		
		doc.add(new Field("place", placeObject.toString(), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		
		doc.add(new Field("search", makeSearchablePlaceContent(properties), Field.Store.YES,
				Field.Index.ANALYZED));
		
		doc.add(new Field("metafile", "doc", Store.YES, Index.ANALYZED));
		addSpatialLcnFields(coord, doc);
								
		
		return doc;
		
	}
	
	
	public Document makePlaceDocumentForIndex(JSONObject placeObject) throws JSONException, DocumentContentException
	{
				
		JSONObject properties = placeObject.getJSONObject("properties");
		JSONObject geom = placeObject.getJSONObject("geometry");
		JSONArray coords = geom.getJSONArray("coordinates");
		Point coord = 
			new Point(
					Double.parseDouble(coords.getString(1)), 
					Double.parseDouble(coords.getString(0)));
		
		Document doc = new Document();
		
		if(!placeObject.isNull("_id")){
			doc.add(new Field("_id", placeObject.getString("_id"), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
			
//		doc.add(new Field("name", properties.getString("name"), Field.Store.YES,
//				Field.Index.NOT_ANALYZED));
//		
//		doc.add(new Field("place", placeObject.toString(), Field.Store.YES,
//				Field.Index.NOT_ANALYZED));
		
		doc.add(new Field("search", makeSearchablePlaceContent(properties), Field.Store.YES,
				Field.Index.ANALYZED));
		
		doc.add(new Field("metafile", "doc", Store.YES, Index.ANALYZED));
		addSpatialLcnFields(coord, doc);
								
		
		return doc;
		
	}
	
	/*
{
    "startDate": 1330934400000,
    "location": {
        "phoneNumber": "202-822-8808",
        "address": "1134 19th St NW",
        "zipCode": "20036",
        "name": "Charmed Salon",
        "state": "DC",
        "longitude": -77.043657,
        "latitude": 38.904831,
        "city": "Washington"
    },
    "smallImageUrl": "http://www.signpost.com/media/BAhbB1sHOgZmSSIyMjAxMi8wMS8wNS8xNC80NC8yMi80NzYvY2hhcm1lZF9zYWxvbl9waWMuanBnBjoGRVRbCToGcDoMY29udmVydEkiEiAtc2NhbGUgNDV4NDUGOwZGMA",
    "endDate": 1331280000000,
    "available": 250,
    "savings": 90,
    "url": "http://www.signpost.com/deals/washington-dc/charmed-salon-dc/90-haircut-full-head-of-highlights-50-off/574?utm_source=welocally&utm_medium=distribution&utm_campaign=baltimore-md&utm_content=charmed-salon-dc",
    "title": "$90 Haircut & Full Head of Highlights (50% OFF)",
    "price": 90,
    "_id": "WLD_7m4q0hujgpsh12fc6gr7lc_38.904831_-77.043657@1331222040",
    "percentageDiscount": 50,
    "details": "If you're looking to highlight your best features, why not get literal by adding a full head of highlights? Charmed Salon is offering an amazingly priced 50% off deal on a full head of highlights with haircut. Their team of talented professionals strives to exceed their customers’ expectations with each visit. Each stylist has more than 10 years of experience cutting, dyeing, and styling diverse types of hair. Conveniently located near Dupont Circle and Farragut West metro stations, each member of the team at Charmed Salon is dedicated to ensuring your complete satisfaction with your new head of highlights and haircut. And at just $90 with this deal, you'll leave Charmed Salon with a Charming new look!",
    "signpostId": "574",
    "value": 180,
    "largeImageUrl": "http://www.signpost.com/media/BAhbB1sHOgZmSSIyMjAxMi8wMS8wNS8xNC80NC8yMi80NzYvY2hhcm1lZF9zYWxvbl9waWMuanBnBjoGRVRbCToGcDoMY29udmVydEkiFCAtc2NhbGUgMTkweDE5MAY7BkYw",
    "categories": [],
    "quantity": 1000,
    "mediumImageUrl": "http://www.signpost.com/media/BAhbB1sHOgZmSSIyMjAxMi8wMS8wNS8xNC80NC8yMi80NzYvY2hhcm1lZF9zYWxvbl9waWMuanBnBjoGRVRbCToGcDoMY29udmVydEkiEiAtc2NhbGUgOTV4OTUGOwZGMA",
    "sold": 5
}
	 */
	public String makeSearchableDealContent(JSONObject deal) throws JSONException{
        StringBuffer buf = new StringBuffer();
        buf.append(deal.getString("title")+" ");
        buf.append(deal.getString("details")+" ");
        
        if(deal.has("location")){
            JSONObject location = deal.getJSONObject("location");
            buf.append(location.getString("city")+" ");
        }
           
        return buf.toString();  
    }
	
	public String makeSearchablePlaceContent(JSONObject placeProperties) throws JSONException{
		StringBuffer buf = new StringBuffer();
		buf.append(placeProperties.getString("name")+" ");
		
		//classifiers
		if(placeProperties.has("classifiers")){
			JSONArray categories = placeProperties.getJSONArray("classifiers");
			for (int i = 0; i < categories.length(); i++) {
				//buf.append(tags.getString(i)+" ");
				JSONObject cat = categories.getJSONObject(i);
				buf.append(cat.getString("category")+" ");
				buf.append(cat.getString("subcategory")+" ");
				buf.append(cat.getString("type")+" ");
			}
		}
		
		//tags
		if(placeProperties.has("tags")){
			JSONArray tags = placeProperties.getJSONArray("tags");
			for (int i = 0; i < tags.length(); i++) {
				buf.append(tags.getString(i)+" ");
			}
		}
		
		return buf.toString();	
	}
		
	private void addSpatialLcnFields(Point coord, Document document) {
		document.add(new Field(spatialConversionUtils.LAT_FIELD, NumericUtils
				.doubleToPrefixCoded(coord.getLat()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		document.add(new Field(spatialConversionUtils.LON_FIELD, NumericUtils
				.doubleToPrefixCoded(coord.getLon()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		addCartesianTiers(coord, document);
	}
	
	private void addCartesianTiers(Point coord, Document document) {
	   for (int tier = spatialConversionUtils.getStartTier(); tier <= spatialConversionUtils.getEndTier(); tier++) {
		  CartesianTierPlotter ctp = new CartesianTierPlotter(tier, spatialConversionUtils.getProjector(), CartesianTierPlotter.DEFALT_FIELD_PREFIX);
	      double boxId = ctp.getTierBoxId(coord.getLat(), coord.getLon());
	      document.add(new Field(ctp.getTierFieldName(), NumericUtils
	         .doubleToPrefixCoded(boxId), Field.Store.YES,
	         Field.Index.NOT_ANALYZED_NO_NORMS));
	   }
	}
	

}
