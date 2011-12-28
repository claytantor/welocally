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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations="classpath:geodb-applicationContext.xml")
public class GeoSpatialTest {
	static Logger logger = 
		Logger.getLogger(GeoSpatialTest.class);
	
	
	@Autowired SpatialHelper spatialHelper;
	@Autowired SpatialExamples spatialExamples;
	
	@Test
	public void testSpatialSearch() {
		logger.debug("testSpatialSearch");

		try {
			final Directory dir = new RAMDirectory();
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_33,
					new StandardAnalyzer(Version.LUCENE_33));
			final IndexWriter writer = new IndexWriter(dir, config);
			spatialExamples.createExampleLocationsGeo(spatialHelper, writer);
			writer.commit();
			writer.close(true);
			// test data
			final IndexSearcher searcher = new IndexSearcher(dir, true);
			final double testDistance = spatialHelper.getMiles(10.0D);
			Point p = new Point(-12.4962596893, 131.045791626);
			final List<String> locations = find(searcher, "Northcoast", p,
					testDistance,  spatialHelper);
			for (final String location : locations) {
				logger.debug("location found: " + location);
			}
			
			Assert.assertEquals(1, locations.size());
			
		} catch (Exception e) {
			logger.error("fail", e);
			Assert.fail(e.getMessage());
		}

	}
	
	private List<String> find(final IndexSearcher searcher, String name,
			final Point start, final double miles, SpatialHelper helper) throws Exception {

		final List<String> result = new ArrayList<String>();

		int minTierIndexed = 1;
		int maxTierIndexed = 20;
		int maxResults = 20;
		final DistanceQueryBuilder dq = new DistanceQueryBuilder(
				start.getLat(), start.getLon(), miles, SpatialHelper.LAT_FIELD,
				SpatialHelper.LON_FIELD,
				CartesianTierPlotter.DEFALT_FIELD_PREFIX, true, minTierIndexed,
				maxTierIndexed);

		// Create a distance sort
		// As the radius filter has performed the distance calculations
		// already, pass in the filter to reuse the results.
		final DistanceFieldComparatorSource dsort = new DistanceFieldComparatorSource(
				dq.getDistanceFilter());
		final Sort sort = new Sort(new SortField("geo_distance", dsort));

		Query query = new QueryParser(Version.LUCENE_33, "name",
				new StandardAnalyzer(Version.LUCENE_33)).parse(name);

		// find with distance sort
		final TopDocs hits = searcher.search(query, dq.getFilter(), maxResults,
				sort);
		final Map<Integer, Double> distances = dq.getDistanceFilter()
				.getDistances();

		for (int i = 0; i < hits.totalHits && i < maxResults; i++) {
			final int docID = hits.scoreDocs[i].doc;

			final Document doc = searcher.doc(docID);

			final StringBuilder builder = new StringBuilder();
			builder.append("name:").append(doc.get("name"))
					.append(" distance:").append(
							helper.getKm(distances.get(docID)));

			result.add(builder.toString());
		}

		return result;
	}


}
