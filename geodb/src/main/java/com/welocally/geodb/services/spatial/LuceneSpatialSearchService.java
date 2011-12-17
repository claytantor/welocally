package com.welocally.geodb.services.spatial;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.spatial.tier.DistanceFieldComparatorSource;
import org.apache.lucene.spatial.tier.DistanceQueryBuilder;
import org.apache.lucene.spatial.tier.projections.CartesianTierPlotter;
import org.apache.lucene.spatial.tier.projections.IProjector;
import org.apache.lucene.spatial.tier.projections.SinusoidalProjector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.NoSuchDirectoryException;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.welocally.geodb.services.db.DbException;
import com.welocally.geodb.services.db.DbPage;
import com.welocally.geodb.services.db.JsonDatabase;
import com.welocally.geodb.services.index.DirectoryException;
import com.welocally.geodb.services.index.DocumentContentException;
import com.welocally.geodb.services.index.PlaceDirectory;

@Component
public class LuceneSpatialSearchService implements SpatialSearchService {

	static Logger logger = Logger.getLogger(LuceneSpatialSearchService.class);
	
	@Value("${spatialSearch.maxSearchRadiusKm:30.00}")
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
    
    @Value("${placeDirectoryIndexer.collection.names:places}")
    private String collectionNames;

	@Autowired PlaceDirectory placeDirectory;

	@Autowired
	private JsonDatabase jsonDatabase;

	@Autowired
	private SpatialDocumentFactory documentFactory;

	@PostConstruct
	public void initIndex() {
		logger.debug("initializing");
				
		try {
								
			String[] collectionNamesArray = collectionNames.split(",");
			
			for (int i = 0; i < collectionNamesArray.length; i++) {
				String collectionName = collectionNamesArray[i];
				
				String[] files=null;
				try {
					files = placeDirectory.getDirectory(collectionName).listAll();
					
					boolean indexExists = false;
					for (String file : files) {
						if(file.equals("segments.gen")){
							indexExists=true;
							break;
						}			
					}
					
					if(!indexExists){
						generateIndex(collectionName);
					}
					
					
				} catch (NoSuchDirectoryException e) {
					generateIndex(collectionName);
				}
				
				
			}
		} 
		catch (DbException e) {
			logger.error("cant get documents for indexing", e);
		} 
		catch (CorruptIndexException e) {
			logger.error("cant get documents for indexing", e);
		} catch (IOException e) {
			logger.error("cant get documents for indexing", e);
		} catch (Exception e) {
			logger.error("cant get documents for indexing", e);
		}

		

	}
	
	private void generateIndex(String collectionName) throws CorruptIndexException, LockObtainFailedException, IOException, DirectoryException, DbException {
		logger.debug("generating index");
		endTier = ctp.bestFit(getMiles(maxKm));
        startTier = ctp.bestFit(getMiles(minKm));
		
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_33,
				new StandardAnalyzer(Version.LUCENE_33));
		final IndexWriter writer = 
			new IndexWriter(placeDirectory.getDirectory(collectionName), 
					config);

		// get the first page and add
		int resultCount = 0;
		DbPage dbpage = jsonDatabase.findAll(collectionName, 1);
		addPage(dbpage, writer);

		if (dbpage.getCount() > dbpage.getObjects().length()) {
			int pages = dbpage.getCount() / dbpage.getPageSize();
			if (dbpage.getCount() % dbpage.getPageSize() > 0)
				pages = pages + 1;
			for (int pageNumIndex = 2; pageNumIndex <= pages; pageNumIndex++) {
				dbpage = jsonDatabase.findAll(collectionName, pageNumIndex);
				resultCount = resultCount + dbpage.getObjects().length();
				addPage(dbpage, writer);
			}
		}

		writer.commit();
		writer.close(true);
		logger.debug("finished indexing");
	}

	public void addPage(DbPage page, IndexWriter writer) {
		// this is only getting first page, we need
		// to iterate through all pages
		for (int i = 0; i < page.getObjects().length(); i++) {

			// don't fail on a specific doc
			try {
				JSONObject place = page.getObjects().getJSONObject(i);
				// Term removeTerm = new Term("_id", place.getString("_id"));
				// placeIndexWriter.getIndexWriter().deleteDocuments(removeTerm);

				logger.debug("adding document for _id:"
						+ place.getString("_id"));
				

				JSONObject geom = place.getJSONObject("geometry");
				JSONArray coords = geom.getJSONArray("coordinates");
				Point coord = new Point(
						Double.parseDouble(coords.getString(1)), Double
								.parseDouble(coords.getString(0)));
				
				final Document doc = documentFactory.makePlaceDocument(place);
				addSpatialLcnFields(coord, doc);
				writer.addDocument(doc);
				

			} catch (JSONException e) {
				logger.warn("cant index a documenent", e);
			} catch (CorruptIndexException e) {
				logger.warn("cant index a documenent", e);
			} catch (IOException e) {
				logger.warn("cant index a documenent", e);
			} catch (DocumentContentException e) {
				logger.warn("cant index a documenent", e);
			} catch (Exception e) {
				logger.warn("cant index a documenent", e);
			}

		}
	}


	@Override
	public JSONArray find(String queryString, Point start, double km, String collectionName)
			throws SpatialSearchException, DirectoryException {

		JSONArray results = new JSONArray();

		try {
			
			final IndexSearcher searcher = new IndexSearcher(placeDirectory.getDirectory(collectionName), true);
			final double miles = getMiles(km);
		

			int minTierIndexed = 1;
			int maxTierIndexed = 20;
			int maxResults = 20;
			final DistanceQueryBuilder dq = new DistanceQueryBuilder(
					start.getLat(), start.getLon(), miles, LAT_FIELD,
					LON_FIELD,
					CartesianTierPlotter.DEFALT_FIELD_PREFIX, true, minTierIndexed,
					maxTierIndexed);

			// Create a distance sort
			// As the radius filter has performed the distance calculations
			// already, pass in the filter to reuse the results.
			final DistanceFieldComparatorSource dsort = new DistanceFieldComparatorSource(
					dq.getDistanceFilter());
			final Sort sort = new Sort(new SortField("geo_distance", dsort));

			Query query = new QueryParser(Version.LUCENE_33, "search",
					new StandardAnalyzer(Version.LUCENE_33)).parse(queryString);

			// find with distance sort
			final TopDocs hits = searcher.search(query, dq.getFilter(), maxResults,
					sort);
			final Map<Integer, Double> distances = dq.getDistanceFilter()
					.getDistances();

			logger.debug("hits:"+hits.totalHits);
			for (int i = 0; i < hits.totalHits && i < maxResults; i++) {
				final int docID = hits.scoreDocs[i].doc;

				final Document doc = searcher.doc(docID);

				final StringBuilder builder = new StringBuilder();
				builder.append("name:").append(doc.get("name"))
						.append(" distance:").append(
								getKm(distances.get(docID)));
				logger.debug(builder.toString());
				
				Document hit = searcher.doc(hits.scoreDocs[i].doc);
				JSONObject place = new JSONObject(hit.get("place"));
				Double dist = new Double(getKm(distances.get(docID)));
				place.put("distance",dist);
				results.put(place);

				
			}
			
		} catch (CorruptIndexException e) {
			logger.error("problem with search",e);
			throw new SpatialSearchException(e); 
		} catch (IOException e) {
			logger.error("problem with search",e);
			throw new SpatialSearchException(e); 
		} catch (ParseException e) {
			logger.error("problem with search",e);
			throw new SpatialSearchException(e); 
		} catch (JSONException e) {
			logger.error("problem with search",e);
			throw new SpatialSearchException(e); 
		}
		
		return results;
	}


	public double getMiles(final double km) {
		return km / MILE;
	}

	public double getKm(final double miles) {
		return miles * MILE;
	}

	private void addSpatialLcnFields(final Point coord, final Document document) {
		document.add(new Field("lat", NumericUtils.doubleToPrefixCoded(coord
				.getLat()), Field.Store.YES, Field.Index.NOT_ANALYZED));
		document.add(new Field("lng", NumericUtils.doubleToPrefixCoded(coord
				.getLon()), Field.Store.YES, Field.Index.NOT_ANALYZED));
		addCartesianTiers(coord, document);
	}

	private void addCartesianTiers(final Point coord, final Document document) {
		for (int tier = startTier; tier <= endTier; tier++) {
			ctp = new CartesianTierPlotter(tier, projector,
					CartesianTierPlotter.DEFALT_FIELD_PREFIX);
			final double boxId = ctp.getTierBoxId(coord.getLat(), coord
					.getLon());
			document.add(new Field(ctp.getTierFieldName(), NumericUtils
					.doubleToPrefixCoded(boxId), Field.Store.YES,
					Field.Index.NOT_ANALYZED_NO_NORMS));
		}
	}
	
}
