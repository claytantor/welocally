package com.welocally.geodb.services.spatial;

import javax.annotation.PostConstruct;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.spatial.tier.projections.CartesianTierPlotter;
import org.apache.lucene.spatial.tier.projections.IProjector;
import org.apache.lucene.spatial.tier.projections.SinusoidalProjector;
import org.apache.lucene.util.NumericUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpatialHelper {
	   
	
		@Value("${spatialSearch.maxSearchRadiusKm:20.0}")
		private Double maxKm;
		
		@Value("${spatialSearch.minSearchRadiusKm:10.0}")
		private Double minKm;
		
	
		private IProjector projector = new SinusoidalProjector();
	    private CartesianTierPlotter ctp = new CartesianTierPlotter(0, projector, CartesianTierPlotter.DEFALT_FIELD_PREFIX);
	    private int startTier;
	    private int endTier;
	 
	    public static final double MILE = 1.609344;
	    public static String LAT_FIELD = "lat";
	    public static String LON_FIELD = "lng";
	 
	    	    
	    public SpatialHelper(){    	
	    }
	    
	    
	    public SpatialHelper(final double maxKm, final double minKm) {
	        endTier = ctp.bestFit(getMiles(maxKm));
	        startTier = ctp.bestFit(getMiles(minKm));
	    }
	    
	    @PostConstruct
	    private void initFit(){
	    	endTier = ctp.bestFit(getMiles(maxKm));
	        startTier = ctp.bestFit(getMiles(minKm));		    	
	    }
	    
	    
//	    String[] tags = { "Abbie",
//	    		"Abbot",
//	    		"Abbott",
//	    		"Abby",
//	    		"Abbygirl",
//	    		"Abbynormal",
//	    		"Abbykitty",
//	    		"Abcat",
//	    		"Abdul",
//	    		"Abe",
//	    		"Abednego",
//	    		"Abegale"};
	    
//	    private String getRandomTag(){
//	    	int min = 0;
//	    	int max = tags.length-1;
//	    	int num = min + (int)(Math.random() * ((max - min) + 1));
//	    	return tags[num];
//	    }

	     
	    public void addLoc(final IndexWriter writer, final String name, final Point coord)
	        throws Exception {
	        final Document doc = new Document();
	        doc.add(new Field("name", name, Field.Store.YES, Index.ANALYZED));
	        //doc.add(new Field("tag", getRandomTag(), Field.Store.YES, Index.ANALYZED));
	        addSpatialLcnFields(coord, doc);
	        writer.addDocument(doc);
	    }
	 
	    public double getMiles(final double km) {
	        return km / MILE;
	    }
	 
	    public double getKm(final double miles) {
	        return miles * MILE;
	    }
	 
	    private void addSpatialLcnFields(final Point coord, final Document document) {
	        document.add(new Field("lat", NumericUtils.doubleToPrefixCoded(coord.getLat()), Field.Store.YES,
	            Field.Index.NOT_ANALYZED));
	        document.add(new Field("lng", NumericUtils.doubleToPrefixCoded(coord.getLon()), Field.Store.YES,
	            Field.Index.NOT_ANALYZED));
	        addCartesianTiers(coord, document);
	    }
	 
	    private void addCartesianTiers(final Point coord, final Document document) {
	        for (int tier = startTier; tier <= endTier; tier++) {
	            ctp = new CartesianTierPlotter(tier, projector, CartesianTierPlotter.DEFALT_FIELD_PREFIX);
	            final double boxId = ctp.getTierBoxId(coord.getLat(), coord.getLon());
	            document.add(new Field(ctp.getTierFieldName(), NumericUtils.doubleToPrefixCoded(boxId), Field.Store.YES,
	                Field.Index.NOT_ANALYZED_NO_NORMS));
	        }
	    }
	    
	    
	    
	    
	    
}
