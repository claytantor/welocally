package com.welocally.geodb.services.spatial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.spatial.tier.DistanceFieldComparatorSource;
import org.apache.lucene.spatial.tier.DistanceQueryBuilder;
import org.apache.lucene.spatial.tier.projections.CartesianTierPlotter;
import org.apache.lucene.spatial.tier.projections.IProjector;
import org.apache.lucene.spatial.tier.projections.SinusoidalProjector;
import org.apache.lucene.util.Version;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.welocally.geodb.services.index.DirectoryException;
import com.welocally.geodb.services.index.PlaceDirectory;

@Component
public class LuceneSpatialSearchService implements SpatialSearchService {

	static Logger logger = Logger.getLogger(LuceneSpatialSearchService.class);
	


	@Autowired PlaceDirectory placeDirectory;
	
	@Autowired SpatialConversionUtils spatialConversionUtils;
	
	public IndexSearcher getPlaceSearcher() throws SpatialSearchException{
		//searcher
		try {
			return new IndexSearcher(placeDirectory.getDirectory(), true);
		} catch (CorruptIndexException e) {
			logger.error("problem with search",e);
			throw new SpatialSearchException(e);
		} catch (IOException e) {
			logger.error("problem with search",e);
			throw new SpatialSearchException(e);
		} catch (DirectoryException e) {
			logger.error("problem with search",e);
			throw new SpatialSearchException(e);
		}
	}

	@Override
	public JSONArray find(IndexSearcher searcher, Point start, double km, String queryString) 
		throws SpatialSearchException  {

		  JSONArray result = new JSONArray();		      	      
	      
	      try {
			final DistanceQueryBuilder dq = new DistanceQueryBuilder(
						start.getLat(), start.getLon(), 
						getMiles(km), 
						spatialConversionUtils.LAT_FIELD,
						spatialConversionUtils.LON_FIELD,
						CartesianTierPlotter.DEFALT_FIELD_PREFIX, 
						true, 
						spatialConversionUtils.getStartTier(),					
						spatialConversionUtils.getEndTier());

			  Query query = new TermQuery(new Term("metafile", "doc"));
			 		      
			  BooleanQuery bq = new BooleanQuery();

			  Query queryName = new QueryParser(Version.LUCENE_33, "search",
						new StandardAnalyzer(Version.LUCENE_33)).parse(queryString);
			  
			  bq.add(query, BooleanClause.Occur.MUST);
			  bq.add(queryName, BooleanClause.Occur.MUST);

			  TopDocs hits = searcher.search(dq.getQuery(bq), 10);
			  		      
			  logger.debug("total hits:"+ hits.totalHits);
			  for (int i = 0; i < hits.totalHits && i<hits.scoreDocs.length ; i++) {
			     Document doc = searcher.doc(hits.scoreDocs[i].doc);
			     result.put(new JSONObject(doc.get("place")));  
			  }
		} catch (CorruptIndexException e) {
			logger.error("problem with search",e);
			throw new SpatialSearchException(e);
		} catch (ParseException e) {
			logger.error("problem with search",e);
			throw new SpatialSearchException(e);
		} catch (IOException e) {
			logger.error("problem with search",e);
			throw new SpatialSearchException(e);
		} catch (JSONException e) {
			logger.error("problem with search",e);
			throw new SpatialSearchException(e);
		}
	    return result;
	}


	public double getMiles(final double km) {
		return km / spatialConversionUtils.MILE;
	}

	public double getKm(final double miles) {
		return miles * spatialConversionUtils.MILE;
	}


	
}
