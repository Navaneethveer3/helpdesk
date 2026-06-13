package com.project.helpdesk;
	
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.helpdesk.service.AiService;
	
@SpringBootTest
class RagTests {

	@Autowired
	AiService aiService;
	
	@Autowired
	VectorStore vectorStore;

	@Test
	void saveData() {
		Document document1 = new Document("Java is a high-level, object-oriented programming language developed by Oracle Corporation that is widely used for building desktop applications, web applications, mobile apps, and enterprise software. One of Java’s key features is its platform independence, achieved through the Java Virtual Machine (JVM), which allows programs to run on different operating systems without modification. Java follows the principle of “write once, run anywhere,” making it a popular choice among developers worldwide. It provides strong security features, automatic memory management through garbage collection, and a rich standard library that simplifies software development. Java supports concepts such as inheritance, polymorphism, encapsulation, and abstraction, which help create reusable and maintainable code. It is extensively used in Android application development, large-scale business systems, cloud computing, and financial applications due to its reliability, scalability, and performance. With a large developer community and continuous updates, Java remains one of the most influential and widely adopted programming languages in the world.");
		Document document2 = new Document("Spring Boot is an open-source Java-based framework that simplifies the development of stand-alone, production-ready applications. Built on top of the Spring Framework, Spring Boot reduces the need for extensive configuration by providing auto-configuration, embedded servers, and ready-to-use dependencies. Developers can quickly create web applications, RESTful APIs, microservices, and enterprise solutions with minimal setup. It includes features such as dependency injection, security integration, data access support, monitoring tools, and seamless integration with databases and cloud platforms. Spring Boot’s convention-over-configuration approach improves productivity, while its scalability and flexibility make it suitable for both small projects and large enterprise systems. Widely adopted in modern software development, Spring Boot enables faster development, easier deployment, and efficient maintenance of Java applications.");
		Document document3 = new Document("Spring AI is an open-source framework that enables developers to integrate artificial intelligence capabilities into Java applications using the Spring ecosystem. It provides a consistent abstraction layer for working with various AI models and services, allowing applications to connect to large language models (LLMs), embedding models, and vector databases without being tied to a specific provider. Built to work seamlessly with Spring Boot, Spring AI simplifies tasks such as prompt management, chat-based interactions, retrieval-augmented generation (RAG), text embeddings, and AI-powered agents. The framework supports integration with leading AI platforms, making it easier to build intelligent applications such as chatbots, recommendation systems, document analysis tools, and knowledge assistants. By following familiar Spring programming patterns, Spring AI helps Java developers rapidly develop, test, and deploy AI-driven solutions while maintaining scalability, security, and maintainability");
		this.vectorStore.add(List.of(document1, document2, document3));
	}
	
	@Test
	void checkRag() {
		String query = "Tell where do the java is used?";
		String response = this.aiService.getResponseFromAssistant(query, "tester");
		assertNotNull(response);
		System.out.println(response);
	}
}
