**AI Based Helpdesk**
The Al-Powered Helpdesk is a full-stack support-ticket platform in which an LLM agent handles the support workflow end to end not just answering questions, but autonomously triaging, resolving, and learning from tickets with no human in the loop by default. It pairs a Spring Boot/Spring Al backend with a React front end, PostgreSQL + pgvector for storage and retrieval, and a locally hosted LLM (Gemma, served through LM Studio).
The core idea that differentiates this from a typical "chatbot over a database" project is closed-loop automation: when a ticket is created, an independent Al agent is dispatched asynchronously to resolve it; when it resolves a ticket, that resolution is embedded back into the knowledge base so the next similar ticket is answered from real historical fixes. The system also exposes a conversational assistant, document ingestion (RAG), and live web research as agent-callable tools, with dynamic tool discovery so the model isn't overloaded with irrelevant tool schemas.

**Core Features :**
- **Ticket lifecycle management** : create, read, update, delete, and query tickets by user, with priority/category/status tracking.
- **Autonomous ticket resolution** : ticket creation fires an event that triggers a background Al agent to analyze and resolve the ticket without user involvement.
- **Streaming conversational assistant** : a chat endpoint over Server-Sent Events (SSE) for real-time,
token-by-token responses.
- **Retrieval-Augmented Generation (RAG)** : uploaded documents (PDF, DOCX, etc.) are parsed, chunked, embedded, and made searchable by the agent.
- **Self-learning knowledge base** : every resolved ticket is embedded back into the vector store, so the KB compounds over time without retraining.
- **Agentic web research** : the agent can search the live web, crawl the results, and permanently ingest what it finds into the knowledge base.
- **Dynamic tool discovery** : tools are retrieved via vector similarity per turn instead of being statically bound to every prompt.

**Architecture :**

<img width="1864" height="611" alt="image" src="https://github.com/user-attachments/assets/404ed88c-7440-4726-90a0-1c3ca3ab3727" />


**Frontend :**
- **React.js :** provides interactive, lag-free UI to the user.

**Backend :**
- **Spring Boot :** gives enterprise level development support with auto-configuration.
- **Spring AI :** provides AI support AI driven backend for enterprise grade backend systems.
- **JUnit :** helps to test the code and the endpoints.
- **Gemma 4 :** it is an open-source LLM model that helps to work with the data.

**Database :**
- **PostgreSQL** : provides reliability and persistence of the data.
