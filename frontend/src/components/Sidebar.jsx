import React from 'react';
import { MessageSquare } from 'lucide-react';

const Sidebar = ({ tickets, activeTicketId, onSelectTicket, viewFilter, onViewFilterChange }) => {
  return (
    <div className="sidebar">
      <div className="sidebar-header" style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h3 style={{ fontSize: '1rem', fontWeight: '600', color: 'var(--text-secondary)', margin: 0 }}>
          Tickets
        </h3>
        <div style={{ display: 'flex', gap: '8px' }}>
          <button 
            className={`btn-icon ${viewFilter === 'mine' ? 'btn-primary' : ''}`}
            onClick={() => onViewFilterChange('mine')}
            style={{ flex: 1, justifyContent: 'center', fontSize: '0.8rem', padding: '6px' }}
          >My Tickets</button>
          <button 
            className={`btn-icon ${viewFilter === 'all' ? 'btn-primary' : ''}`}
            onClick={() => onViewFilterChange('all')}
            style={{ flex: 1, justifyContent: 'center', fontSize: '0.8rem', padding: '6px' }}
          >All Tickets</button>
        </div>
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
