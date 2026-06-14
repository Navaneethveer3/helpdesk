import React, { useState, useEffect } from 'react';
import Sidebar from './components/Sidebar';
import ChatArea from './components/ChatArea';
import ChatbotWindow from './components/ChatbotWindow';
import NewTicketModal from './components/NewTicketModal';
import { ticketService } from './services/api';
import { List, PlusCircle } from 'lucide-react';
import './index.css';

function App() {
  const [tickets, setTickets] = useState([]);
  const [activeTicketId, setActiveTicketId] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [viewMode, setViewMode] = useState('chatbot'); // 'chatbot' or 'tickets'

  useEffect(() => {
    fetchTickets();
  }, []);

  const fetchTickets = async () => {
    try {
      const data = await ticketService.getAllTickets();
      const sorted = data.sort((a, b) => new Date(b.createdOn) - new Date(a.createdOn));
      setTickets(sorted);
    } catch (error) {
      console.error("Failed to fetch tickets", error);
    }
  };

  const handleCreateTicket = async (ticketData) => {
    const newTicket = await ticketService.createTicket(ticketData);
    setTickets([newTicket, ...tickets]);
    setActiveTicketId(newTicket.id);
    setViewMode('tickets');
  };

  const handleUpdateStatus = async (id, status) => {
    try {
      await ticketService.updateTicket(id, { status });
      setTickets(tickets.map(t => t.id === id ? { ...t, status } : t));
    } catch (error) {
      console.error("Failed to update ticket", error);
    }
  };

  const handleDeleteTicket = async (id) => {
    try {
      await ticketService.deleteTicket(id);
      setTickets(tickets.filter(t => t.id !== id));
      if (activeTicketId === id) setActiveTicketId(null);
    } catch (error) {
      console.error("Failed to delete ticket", error);
    }
  };

  const activeTicket = tickets.find(t => t.id === activeTicketId);

  return (
    <div className="app-container">
      <Sidebar 
        tickets={tickets} 
        activeTicketId={activeTicketId}
        onSelectTicket={(t) => {
          setActiveTicketId(t.id);
          setViewMode('tickets');
        }}
      />
      
      <div className="main-area" style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
        {/* Top Bar with Buttons */}
        <div className="top-bar">
          <h2>{viewMode === 'chatbot' ? 'AI Support Assistant' : (activeTicket ? `Ticket #${activeTicket.id}` : 'All Tickets')}</h2>
          <div className="top-bar-buttons">
            <button className="btn-icon" onClick={() => setViewMode(viewMode === 'chatbot' ? 'tickets' : 'chatbot')}>
              <List size={18} /> {viewMode === 'chatbot' ? 'View All Tickets' : 'Open AI Chatbot'}
            </button>
            <button className="btn-icon btn-primary" onClick={() => setIsModalOpen(true)}>
              <PlusCircle size={18} /> New Ticket
            </button>
          </div>
        </div>

        {/* Dynamic Content Area */}
        {viewMode === 'chatbot' ? (
          <ChatbotWindow userId="user" />
        ) : (
          <ChatArea 
            ticket={activeTicket}
            onUpdateStatus={handleUpdateStatus}
            onDelete={handleDeleteTicket}
            onNewTicket={() => setIsModalOpen(true)}
            onViewAll={() => setActiveTicketId(null)}
          />
        )}
      </div>
      
      {isModalOpen && (
        <NewTicketModal 
          onClose={() => setIsModalOpen(false)}
          onSubmit={handleCreateTicket}
        />
      )}
    </div>
  );
}

export default App;
