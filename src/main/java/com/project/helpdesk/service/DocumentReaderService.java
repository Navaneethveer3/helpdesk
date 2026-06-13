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
	private Resource localResource;
	
	@Autowired
	private VectorStore vectorStore;

	public List<Document> loadDocument(Resource resource){
		TikaDocumentReader reader = new TikaDocumentReader(resource);
		TokenTextSplitter splitter = new TokenTextSplitter(500, 100, 100, 10000, true, List.of('.', '?', '!', '\n'));
		List<Document> transformed = splitter.apply(reader.read());
		vectorStore.add(transformed);
		return transformed;
	}
}
