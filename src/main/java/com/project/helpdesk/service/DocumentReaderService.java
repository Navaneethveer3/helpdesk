package com.project.helpdesk.service;

import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;

public class DocumentReaderService {

	@Value("classpath:Spring Boot.pdf")
	private Resource resource;
	

	public List<Document> loadDocument(){
		TikaDocumentReader reader = new TikaDocumentReader(this.resource);
		TokenTextSplitter splitter = new TokenTextSplitter();
		return splitter.apply(reader.read());
	}
}
