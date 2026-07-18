import React from 'react';
import { MessageSquare, LayoutDashboard, Ticket as TicketIcon } from 'lucide-react';

const Sidebar = ({ tickets, activeTicketId, onSelectTicket }) => {
  return (
    <div className="sidebar">
      <div className="sidebar-header">
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <LayoutDashboard size={24} color="var(--primary-color)" />
          <h3 style={{ fontSize: '1.2rem', fontWeight: '700', color: 'var(--text-primary)', margin: 0, letterSpacing: '-0.02em' }}>
            My Tickets
          </h3>
        </div>
      </div>
      <div className="ticket-list">
        {tickets.length === 0 ? (
          <div style={{ textAlign: 'center', color: 'var(--text-muted)', padding: '30px 20px', fontSize: '0.95rem' }}>
            No tickets found
          </div>
        ) : (
          tickets.map((ticket) => (
            <div
              key={ticket.id}
              className={`ticket-item ${activeTicketId === ticket.id ? 'active' : ''}`}
              onClick={() => onSelectTicket(ticket)}
            >
              <div style={{
                background: activeTicketId === ticket.id ? 'var(--primary-color)' : 'rgba(255,255,255,0.05)',
                padding: '10px',
                borderRadius: '12px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                color: activeTicketId === ticket.id ? 'white' : 'var(--primary-color)'
              }}>
                <TicketIcon size={18} />
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
                <span style={{ fontWeight: '600', color: activeTicketId === ticket.id ? 'var(--text-primary)' : 'var(--text-secondary)', whiteSpace: 'nowrap', textOverflow: 'ellipsis', overflow: 'hidden' }}>
                  {ticket.summary}
                </span>
                <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginTop: '2px' }}>
                  By {ticket.username}
                </span>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default Sidebar;
