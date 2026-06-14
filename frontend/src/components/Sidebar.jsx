import React from 'react';
import { MessageSquare } from 'lucide-react';

const Sidebar = ({ tickets, activeTicketId, onSelectTicket }) => {
  return (
    <div className="sidebar">
      <div className="sidebar-header">
        <h3 style={{ fontSize: '1rem', fontWeight: '600', color: 'var(--text-secondary)' }}>
          User Chat History
        </h3>
      </div>
      <div className="ticket-list">
        {tickets.length === 0 ? (
          <div style={{ textAlign: 'center', color: 'var(--text-secondary)', padding: '20px' }}>
            No chats yet
          </div>
        ) : (
          tickets.map((ticket) => (
            <div
              key={ticket.id}
              className={`ticket-item ${activeTicketId === ticket.id ? 'active' : ''}`}
              onClick={() => onSelectTicket(ticket)}
              style={{ display: 'flex', alignItems: 'center', gap: '8px' }}
            >
              <MessageSquare size={16} />
              <div style={{ overflow: 'hidden', textOverflow: 'ellipsis' }}>
                {ticket.username}'s Issue
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default Sidebar;
