package com.welocally.geodb.services.spatial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.spatial.tier.DistanceFieldComparatorSource;
import org.apache.lucene.spatial.tier.DistanceQueryBuilder;
import org.apache.lucene.spatial.tier.projections.CartesianTierPlotter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

public class GeoSpatialTest {
	
	@Test
	public void testSpatialSearch() {

	}
	
//	static Logger logger = 
//		Logger.getLogger(GeoSpatialTest.class);
//	
//	private SpatialHelper helper;
//	
//	@Before
//	public void setup(){
//		helper = new SpatialHelper(10.0d, 20.0d);
//	}
//	
//	@Test
//    public void testSpatialSearch()
//        throws Exception {
//		
//		
//		logger.debug("testSpatialSearch");
//		
//        final Directory dir = new RAMDirectory();
//        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_33, 
//        		new StandardAnalyzer(Version.LUCENE_33));
//        final IndexWriter writer =  new IndexWriter(dir, config); 
//        
//        SpatialExamples.createExampleLocationsGeo(helper, writer);
//        writer.commit();
//        writer.close(true);
// 
//        // test data
//        final IndexSearcher searcher = new IndexSearcher(dir, true);
//        final double testDistance = helper.getMiles(10.0D);
//        Point p = new Point( -12.4962596893, 131.045791626);
// 
//        final List<String> locations = find(searcher, "Northcoast", p, testDistance);
//        for (final String location : locations) {
//            logger.debug("location found: " + location);
//        }
//    }
// 
//    private List<String> find(final IndexSearcher searcher, String name,  final Point start, final double miles)
//        throws Exception {
//    	
// 
//        final List<String> result = new ArrayList<String>();
//
//        int minTierIndexed = 1;
//        int maxTierIndexed = 20;
//        int maxResults = 20;
//        final DistanceQueryBuilder dq = new DistanceQueryBuilder(start.getLat(), start.getLon(), miles, 
//        		SpatialHelper.LAT_FIELD, SpatialHelper.LON_FIELD, CartesianTierPlotter.DEFALT_FIELD_PREFIX, 
//        		true,  minTierIndexed,  maxTierIndexed) ;		
//        
//        // Create a distance sort
//        // As the radius filter has performed the distance calculations
//        // already, pass in the filter to reuse the results.
//        final DistanceFieldComparatorSource dsort = 
//        	new DistanceFieldComparatorSource(dq.getDistanceFilter());
//        final Sort sort = new Sort(new SortField("geo_distance", dsort));
// 
////        final Query query = new MatchAllDocsQuery();
////        Query query = 
////			new QueryParser(
////					Version.LUCENE_33, "name", new StandardAnalyzer(Version.LUCENE_33))
////					.parse(name);
////        
//        Query query = new MatchAllDocsQuery();
//        
//        // find with distance sort
//        final TopDocs hits = searcher.search(query, dq.getFilter(), maxResults, sort);
//        final Map<Integer, Double> distances = dq.getDistanceFilter().getDistances();
// 
//        // find normal, gets unordered result
//        //final TopDocs hits = searcher.search(dq.getQuery(query), 10);
//
//        for (int i = 0; i < hits.totalHits && i<maxResults; i++) {
//            final int docID = hits.scoreDocs[i].doc;
// 
//            final Document doc = searcher.doc(docID);
// 
//            final StringBuilder builder = new StringBuilder();
//            builder.append("name:").append(doc.get("name")).append(" distance:").append(
//            		helper.getKm(distances.get(docID)));
//                     
//            result.add(builder.toString());
//        }
// 
//        return result;
//    }

}
