package com.sightlyinc.ratecred.index;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;

import com.sightlyinc.ratecred.dao.RatingDao;
import com.sightlyinc.ratecred.model.Rating;



/**
 * @author cgraham
 *
 */
public class DefaultRatingDirectoryIndexer implements RatingDirectoryIndexer {
	
	static boolean initialized = false;
	
	static Logger logger = 
		Logger.getLogger(DefaultRatingDirectoryIndexer.class);
	
	private Directory directory;
	private IndexWriter indexWriter;
	private RatingDao ratingDao;
	
	public void initIndex()
	{
		logger.debug("initializing");
				
		if(!initialized)
		{
			List<Rating> allRates = ratingDao.findAll();
			
			try {
				for (Rating rating : allRates) {
					Term removeTerm = new Term("id", rating.getId().toString());
					indexWriter.deleteDocuments(removeTerm);
					
					indexWriter.addDocument(
							RatingDocumentFactory.makeRateDocument(
									rating));
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
	 * @see com.sightlyinc.ratecred.index.RateDirectoryIndexer#indexRate(com.sightlyinc.ratecred.model.Rate)
	 */
	public void indexRate(Rating rating)
	{
		try {
			Term removeTerm = new Term("id", rating.getId().toString());
			indexWriter.deleteDocuments(removeTerm);
			
			indexWriter.addDocument(
					RatingDocumentFactory.makeRateDocument(
							rating));
			
			indexWriter.flush();
		} catch (CorruptIndexException e) {
			logger.error("problem with indexing", e);
		} catch (IOException e) {
			logger.error("problem with indexing", e);
		}
	}
	
	
	
	@Override
	public void removeRate(Rating rating) {
		try {
			Term removeTerm = new Term("id", rating.getId().toString());
			indexWriter.deleteDocuments(removeTerm);
			indexWriter.flush();
		} catch (CorruptIndexException e) {
			logger.error("problem with indexing", e);
		} catch (IOException e) {
			logger.error("problem with indexing", e);
		}
		
	}

	public void setDirectory( Directory directory) {
		this.directory = directory;
	}

	public void setIndexWriter( IndexWriter indexWriter) {
		this.indexWriter = indexWriter;
	}

	public void setRatingDao(RatingDao ratingDao) {
		this.ratingDao = ratingDao;
	}	
		
}
