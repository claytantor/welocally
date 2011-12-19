package com.welocally.geodb.services.spatial;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.spatial.tier.projections.CartesianTierPlotter;
import org.apache.lucene.spatial.tier.projections.IProjector;
import org.apache.lucene.spatial.tier.projections.SinusoidalProjector;
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

import com.welocally.geodb.services.app.CommandException;
import com.welocally.geodb.services.app.CommandSupport;
import com.welocally.geodb.services.db.DbException;
import com.welocally.geodb.services.db.DbPage;
import com.welocally.geodb.services.db.JsonDatabase;
import com.welocally.geodb.services.index.DirectoryException;
import com.welocally.geodb.services.index.DocumentContentException;
import com.welocally.geodb.services.index.PlaceDirectory;

@Component
public class LuceneSpatialIndexService implements SpatialIndexService,CommandSupport  {

	static Logger logger = Logger.getLogger(LuceneSpatialIndexService.class);
	
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


	@Override
	public void doCommand(JSONObject command) throws CommandException {
		try {
			index(command.getInt("maxDocs"));
		} catch (JSONException e) {
			throw new CommandException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.welocally.geodb.services.spatial.SpatialIndexService#index()
	 */
	public void index(int maxdocs) {
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
						generateIndex(collectionName, maxdocs);
					}
					
					
				} catch (NoSuchDirectoryException e) {
					generateIndex(collectionName, maxdocs);
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
	
	private void generateIndex(String collectionName, int maxDocs) throws CorruptIndexException, LockObtainFailedException, IOException, DirectoryException, DbException {
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
			for (int pageNumIndex = 2; pageNumIndex <= pages && resultCount<maxDocs; pageNumIndex++) {
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
