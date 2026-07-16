const API_BASE_URL = 'http://localhost:8080/ticket';

export const ticketService = {
  getAllTickets: async () => {
    const response = await fetch(`${API_BASE_URL}/all`);
    if (!response.ok) throw new Error('Failed to fetch tickets');
    return response.json();
  },

  getTicketsByUsername: async (username) => {
    const response = await fetch(`${API_BASE_URL}/user/${encodeURIComponent(username)}`);
    if (!response.ok) throw new Error('Failed to fetch tickets for user');
    return response.json();
  },

  getTicketById: async (id) => {
    const response = await fetch(`${API_BASE_URL}/id/${id}`);
    if (!response.ok) throw new Error('Failed to fetch ticket');
    return response.json();
  },

  createTicket: async (ticket) => {
    const response = await fetch(`${API_BASE_URL}/create`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(ticket),
    });
    if (!response.ok) throw new Error('Failed to create ticket');
    return response.json();
  },

  updateTicket: async (id, ticketUpdates) => {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(ticketUpdates),
    });
    if (!response.ok) throw new Error('Failed to update ticket');
    return response.json();
  },

  deleteTicket: async (id) => {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to delete ticket');
    return response.text();
  },

  resolveTicket: async (id, solution) => {
    const response = await fetch(`${API_BASE_URL}/support/resolve/${id}?solution=${encodeURIComponent(solution)}`, {
      method: 'POST'
    });
    if (!response.ok) throw new Error('Failed to resolve ticket');
    return response.json();
  }
};

export const aiService = {
  chat: async (query, userId) => {
    const url = new URL('http://localhost:8080/ai/chat');
    url.searchParams.append('query', query);
    url.searchParams.append('userId', userId);
    
    const response = await fetch(url.toString());
    if (!response.ok) throw new Error('Failed to fetch AI response');
    return response.text();
  },

  chatStream: async (query, userId, onChunk) => {
    const url = new URL('http://localhost:8080/ai/chat');
    url.searchParams.append('query', query);
    url.searchParams.append('userId', userId);
    
    const response = await fetch(url.toString(), {
      headers: { 'Accept': 'text/event-stream' }
    });

    if (!response.ok) throw new Error('Failed to fetch AI response');

    const reader = response.body.getReader();
    const decoder = new TextDecoder('utf-8');
    let buffer = '';
    let eventData = [];

    while (true) {
      const { done, value } = await reader.read();
      if (done) {
        if (eventData.length > 0) onChunk(eventData.join('\n'));
        break;
      }

      buffer += decoder.decode(value, { stream: true });
      const lines = buffer.split('\n');
      buffer = lines.pop() || ''; // Keep the incomplete line

      for (let line of lines) {
        line = line.replace(/\r$/, ''); // Handle \r\n endings
        
        if (line.startsWith('data:')) {
          eventData.push(line.slice(5));
        } else if (line === '') {
          // Empty line signifies end of the event
          if (eventData.length > 0) {
            let chunk = eventData.join('\n');
            chunk = chunk.replace(/\\n/g, '\n');
            onChunk(chunk);
            eventData = [];
          }
        }
      }
    }
  },

  uploadDocument: async (file) => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await fetch('http://localhost:8080/docs', {
      method: 'POST',
      body: formData,
    });
    if (!response.ok) throw new Error('Failed to upload document');
    
    // Attempt to parse JSON, or fallback to text if backend returns plain string
    const text = await response.text();
    try {
      return JSON.parse(text);
    } catch (e) {
      return text;
    }
  }
};
