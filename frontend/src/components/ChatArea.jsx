import React, { useState } from 'react';
import { MessageSquare, PlusCircle, CheckCircle, Trash2, List } from 'lucide-react';

const ChatArea = ({ ticket, onUpdateStatus, onDelete, onNewTicket, onViewAll }) => {
  const [isDeleting, setIsDeleting] = useState(false);

  const handleDelete = () => {
    if (window.confirm('Are you sure you want to delete this ticket?')) {
      setIsDeleting(true);
      onDelete(ticket.id).finally(() => setIsDeleting(false));
    }
  };

  return (
    <div className="main-area">
      <div className="top-bar">
        <h2>{ticket ? `Ticket #${ticket.id}` : 'Helpdesk Dashboard'}</h2>
        <div className="top-bar-buttons">
          <button className="btn-icon" onClick={onViewAll}>
            <List size={18} /> View All Tickets
          </button>
          <button className="btn-icon btn-primary" onClick={onNewTicket}>
            <PlusCircle size={18} /> New Ticket
          </button>
        </div>
      </div>
      
      {!ticket ? (
        <div className="empty-state">
          <MessageSquare size={48} />
          <h2 style={{ marginTop: '16px' }}>Select a conversation to view</h2>
        </div>
      ) : (
        <div className="chat-content">
          <div className="chat-messages">
            
            {/* User Message Bubble */}
            <div className="message-row user">
              <div className="avatar">
                {ticket.username.substring(0, 2).toUpperCase()}
              </div>
              <div className="message-bubble">
                <div style={{ fontWeight: 600, marginBottom: '8px', fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                  {ticket.username} • {new Date(ticket.createdOn).toLocaleString()}
                </div>
                {ticket.summary}
              </div>
            </div>

            {/* System/Support Response Bubble */}
            <div className="message-row system">
              <div className="avatar system">
                HD
              </div>
              <div className="message-bubble">
                <div style={{ fontWeight: 600, marginBottom: '8px', fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                  Support System • {new Date(ticket.updatedOn).toLocaleString()}
                </div>
                <p style={{ marginBottom: '16px' }}>
                  Your ticket priority is currently set to <strong>{ticket.priority}</strong>. 
                  The current status of your issue is: <strong className={`ticket-status ${ticket.status?.toLowerCase() || 'open'}`}>{ticket.status}</strong>.
                </p>
                
                <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                  {ticket.status !== 'RESOLVED' && (
                    <button 
                      className="btn-icon btn-primary" 
                      onClick={() => onUpdateStatus(ticket.id, 'RESOLVED')}
                    >
                      <CheckCircle size={16} /> Mark as Resolved
                    </button>
                  )}
                  {ticket.status === 'RESOLVED' && (
                    <button 
                      className="btn-icon" 
                      onClick={() => onUpdateStatus(ticket.id, 'OPEN')}
                    >
                      Reopen Ticket
                    </button>
                  )}
                  <button 
                    className="btn-icon" 
                    onClick={handleDelete}
                    disabled={isDeleting}
                    style={{ color: 'var(--status-closed)', borderColor: 'var(--status-closed)' }}
                  >
                    <Trash2 size={16} /> {isDeleting ? 'Deleting...' : 'Delete'}
                  </button>
                </div>
              </div>
            </div>

          </div>
        </div>
      )}
    </div>
  );
};

export default ChatArea;
