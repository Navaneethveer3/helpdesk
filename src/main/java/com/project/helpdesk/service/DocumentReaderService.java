package com.project.helpdesk.service;

import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;

@Service
public class DocumentReaderService {

	@Value("classpath:Spring Boot.pdf")
	private Resource resource;
	
	@Autowired
	private VectorStore vectorStore;

	public List<Document> loadDocument(){
		TikaDocumentReader reader = new TikaDocumentReader(this.resource);
		TokenTextSplitter splitter = new TokenTextSplitter();
		List<Document> transformed = splitter.apply(reader.read());
		vectorStore.write(transformed);
		return transformed;
	}
}
