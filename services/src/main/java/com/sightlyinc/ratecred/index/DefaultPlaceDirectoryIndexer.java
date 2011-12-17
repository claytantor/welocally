package com.sightlyinc.ratecred.index;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sightlyinc.ratecred.dao.PlaceDao;
import com.sightlyinc.ratecred.model.Place;



/**
 * @author cgraham
 *
 */
@Component("placeDirectoryIndexer")
public class DefaultPlaceDirectoryIndexer implements PlaceDirectoryIndexer {
	
	static boolean initialized = false;
	
	static Logger logger = 
		Logger.getLogger(DefaultPlaceDirectoryIndexer.class);
	
	@Autowired
	@Qualifier("placeIndexWriter")
	private IndexWriter indexWriter;
	
	@Autowired
	private PlaceDao placeDao;
		
	@Override
	public void removePlace(Place place) {
		try {
			Term removeTerm = new Term("id", place.getId().toString());
			indexWriter.deleteDocuments(removeTerm);
			indexWriter.flush();
		} catch (CorruptIndexException e) {
			logger.error("problem with indexing", e);
		} catch (IOException e) {
			logger.error("problem with indexing", e);
		}
		
	}

	public void initIndex()
	{
		logger.debug("initializing");
				
		if(!initialized)
		{
			List<Place> allPlaces = placeDao.findAll();
			
			try {
				for (Place place : allPlaces) {
					Term removeTerm = new Term("id", place.getId().toString());
					indexWriter.deleteDocuments(removeTerm);
					
					indexWriter.addDocument(
							PlaceDocumentFactory.makePlaceDocument(
									place));	
				}
				indexWriter.flush();
				
			} catch (CorruptIndexException e) {
				logger.error("problem with indexing", e);
			} catch (IOException e) {
				logger.error("problem with indexing", e);
			}	
			
			initialized = true;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.noi.placer.index.PlaceDirectoryIndexer#indexPlace(com.noi.placer.model.Place)
	 */
	public void indexPlace(Place place)
	{
		try {
			Term removeTerm = new Term("id", place.getId().toString());
			indexWriter.deleteDocuments(removeTerm);
			
			indexWriter.addDocument(
					PlaceDocumentFactory.makePlaceDocument(
							place));
			
			indexWriter.flush();
		} catch (CorruptIndexException e) {
			logger.error("problem with indexing", e);
		} catch (IOException e) {
			logger.error("problem with indexing", e);
		}
	}
	
//	public void setDirectory( Directory directory) {
//		this.directory = directory;
//	}
//
//	public void setIndexWriter( IndexWriter indexWriter) {
//		this.indexWriter = indexWriter;
//	}
//
//	public void setPlaceDao(PlaceDao placeDao) {
//		this.placeDao = placeDao;
//	}	
		
}
