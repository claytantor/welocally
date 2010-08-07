package com.sightlyinc.ratecred.index;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import com.sightlyinc.ratecred.model.Rating;


public class RatingDocumentFactory {

	public static Document makeRateDocument(Rating t)
	{
		Document doc = new Document();
		doc.add(new Field("id", t.getId().toString(), Field.Store.YES,
				Field.Index.NO));

		String icontent = t.getNotes();
		if(StringUtils.isNotEmpty(icontent))
		{
			doc.add(new Field("indexContent", icontent, Field.Store.YES,
					Field.Index.TOKENIZED));
		}
				
		return doc;
		
	}
	
	
	public static Rating makeDocumentRate(Document doc)
	{
		Rating t = new Rating();
		return t;
		
	}


}
