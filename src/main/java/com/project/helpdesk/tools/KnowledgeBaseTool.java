package com.project.helpdesk.tools;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KnowledgeBaseTool {

    @Autowired
    private VectorStore vectorStore;

    @Tool(description = "Search the knowledge base for documentation, guides, or general information. Use this tool when the user asks a question about a concept or how something works.")
    public String searchKnowledgeBase(@ToolParam(description = "The question or search query to look up in the knowledge base") String query) {
        
        // Search the vector store for the query
        List<Document> results = vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(query)
                .topK(3)
                .similarityThreshold(0.75) // You can adjust or remove this!
                .build()
        );

        // If nothing found, return a message saying so
        if (results == null || results.isEmpty()) {
            return "No relevant information found in the knowledge base for this query.";
        }

        // Combine the text of all matching documents into a single string
        return results.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));
    }
}
