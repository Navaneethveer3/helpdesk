package com.project.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.helpdesk.service.DocumentReaderService;

@RestController
@RequestMapping("/docs")
public class DocumentReaderController {
	
	@Autowired
	private DocumentReaderService documentReaderService;
	
	@PostMapping
	public ResponseEntity<?> loadDocument(@RequestPart MultipartFile file){
		Resource resource = file.getResource();
		var response = this.documentReaderService.loadDocument(resource);
		return ResponseEntity.ok(response);
	}
}
