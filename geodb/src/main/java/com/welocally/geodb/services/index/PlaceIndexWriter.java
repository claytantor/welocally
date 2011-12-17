package com.welocally.geodb.services.index;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


public class PlaceIndexWriter  {
	
//	private IndexWriter indexWriter;
//	
//	@Autowired
//	@Qualifier("placeDirectory") 
//	private PlaceDirectory directory;
//	
//	@Autowired
//	@Qualifier("placeIndexWriterConfig") 
//	private PlaceIndexWriterConfig config;
//
//	
//	@PostConstruct
//	private void initWriter() throws CorruptIndexException, LockObtainFailedException, IOException{
//		indexWriter = new IndexWriter(directory.getDirectory(), config.getIndexWriterConfig());
//	}
//
//
//	public IndexWriter getIndexWriter() {
//		return indexWriter;
//	}
	
	
	
}
