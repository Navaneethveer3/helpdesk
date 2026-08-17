**AI Based Helpdesk**
- This is an helpdesk system that is developed with the help of AI to resolve the tickets dynamically with zero interference of the user.

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

**Frontend :**
- **React.js :** provides intereactive, lag-free UI to the user.

**Backend :**
- **Spring Boot :** gives enterprise level development support with auto-configuration.
- **Spring AI :** provides AI support AI driven backend for enterprise grade backend systems.
- **JUnit :** helps to test the code and the endpoints.
- **Gemma 4 :** it is an open-source LLM model that helps to work with the data.
