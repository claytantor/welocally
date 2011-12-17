package com.sightlyinc.ratecred.index;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import com.sightlyinc.ratecred.model.Place;


public class PlaceDocumentFactory {

	public static Document makePlaceDocument(Place p)
	{
		Document doc = new Document();
		doc.add(new Field("id", p.getId().toString(), Field.Store.YES,
				Field.Index.NO));

		String icontent = p.getName()+" "+p.getDescription();
		doc.add(new Field("indexContent", icontent, Field.Store.YES,
				Field.Index.TOKENIZED));
				
		return doc;
		
	}
	


}
