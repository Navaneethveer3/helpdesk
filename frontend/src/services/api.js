const API_BASE_URL = 'http://localhost:8080/ticket';

export const ticketService = {
  getAllTickets: async () => {
    const response = await fetch(`${API_BASE_URL}/all`);
    if (!response.ok) throw new Error('Failed to fetch tickets');
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
