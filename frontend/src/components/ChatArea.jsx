import React, { useState } from 'react';
import { MessageSquare, PlusCircle, CheckCircle, Trash2, List, Edit2 } from 'lucide-react';

const ChatArea = ({ ticket, onUpdateStatus, onResolve, onEdit, onDelete, onNewTicket, onViewAll }) => {
  const [isDeleting, setIsDeleting] = useState(false);
  const [isResolvingMode, setIsResolvingMode] = useState(false);
  const [resolutionText, setResolutionText] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const [isEditing, setIsEditing] = useState(false);
  const [editFormData, setEditFormData] = useState({ summary: '', priority: 'LOW' });
  const [isSavingEdit, setIsSavingEdit] = useState(false);

  React.useEffect(() => {
    setIsResolvingMode(false);
    setResolutionText('');
    setIsEditing(false);
  }, [ticket?.id]);

  const handleDelete = () => {
    if (window.confirm('Are you sure you want to delete this ticket?')) {
      setIsDeleting(true);
      onDelete(ticket.id).finally(() => setIsDeleting(false));
    }
  };

  const handleEditClick = () => {
    setEditFormData({ summary: ticket.summary, priority: ticket.priority });
    setIsEditing(true);
  };

  const handleEditSubmit = async () => {
    setIsSavingEdit(true);
    await onEdit(ticket.id, editFormData);
    setIsSavingEdit(false);
    setIsEditing(false);
  };

  const handleResolveSubmit = async () => {
    if (!resolutionText.trim()) return;
    setIsSubmitting(true);
    await onResolve(ticket.id, resolutionText);
    setIsSubmitting(false);
    setIsResolvingMode(false);
    setResolutionText('');
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
                {ticket.username ? ticket.username.substring(0, 2).toUpperCase() : 'U'}
              </div>
              <div className="message-bubble" style={{ width: '100%' }}>
                <div style={{ fontWeight: 600, marginBottom: '8px', fontSize: '0.85rem', color: 'var(--text-secondary)', display: 'flex', justifyContent: 'space-between' }}>
                  <span>{ticket.username || 'Unknown'} • {new Date(ticket.createdOn).toLocaleString()}</span>
                  {!isEditing && (
                    <button className="btn-icon" onClick={handleEditClick} style={{ padding: '0', height: 'auto', background: 'none' }}>
                      <Edit2 size={14} /> Edit
                    </button>
                  )}
                </div>
                {isEditing ? (
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                      <label style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Priority:</label>
                      <select 
                        value={editFormData.priority} 
                        onChange={e => setEditFormData({...editFormData, priority: e.target.value})}
                        style={{ padding: '4px', borderRadius: '4px', border: '1px solid var(--border-color)', background: 'var(--bg-primary)' }}
                      >
                        <option value="LOW">LOW</option>
                        <option value="NORMAL">NORMAL</option>
                        <option value="HIGH">HIGH</option>
                      </select>
                    </div>
                    <textarea 
                      style={{ width: '100%', padding: '8px', borderRadius: '4px', resize: 'vertical', minHeight: '60px', border: '1px solid var(--border-color)', background: 'var(--bg-primary)', color: 'var(--text-primary)' }}
                      value={editFormData.summary}
                      onChange={e => setEditFormData({...editFormData, summary: e.target.value})}
                    />
                    <div style={{ display: 'flex', gap: '8px', justifyContent: 'flex-end' }}>
                      <button className="btn-icon" onClick={() => setIsEditing(false)}>Cancel</button>
                      <button className="btn-icon btn-primary" onClick={handleEditSubmit} disabled={isSavingEdit}>
                        {isSavingEdit ? 'Saving...' : 'Save'}
                      </button>
                    </div>
                  </div>
                ) : (
                  ticket.summary
                )}
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

                {ticket.status === 'RESOLVED' && ticket.solution && (
                  <div style={{ marginBottom: '16px', padding: '12px', backgroundColor: 'var(--bg-secondary)', borderRadius: '8px', borderLeft: '4px solid var(--primary-color)' }}>
                    <div style={{ fontWeight: 600, marginBottom: '4px', color: 'var(--text-primary)' }}>Resolution Solution:</div>
                    <div style={{ color: 'var(--text-secondary)', whiteSpace: 'pre-wrap', lineHeight: '1.5' }}>{ticket.solution}</div>
                  </div>
                )}
                
                <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                  {ticket.status !== 'RESOLVED' && !isResolvingMode && (
                    <button 
                      className="btn-icon btn-primary" 
                      onClick={() => setIsResolvingMode(true)}
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

                {isResolvingMode && (
                  <div style={{ marginTop: '16px', display: 'flex', flexDirection: 'column', gap: '8px' }}>
                    <textarea 
                      style={{ width: '100%', padding: '12px', borderRadius: '8px', border: '1px solid var(--border-color)', backgroundColor: 'var(--bg-primary)', color: 'var(--text-primary)', resize: 'vertical', minHeight: '80px', fontFamily: 'inherit' }}
                      placeholder="Type the solution here..."
                      value={resolutionText}
                      onChange={(e) => setResolutionText(e.target.value)}
                    />
                    <div style={{ display: 'flex', gap: '8px', justifyContent: 'flex-end' }}>
                      <button className="btn-icon" onClick={() => { setIsResolvingMode(false); setResolutionText(''); }}>Cancel</button>
                      <button className="btn-icon btn-primary" onClick={handleResolveSubmit} disabled={isSubmitting || !resolutionText.trim()}>
                        {isSubmitting ? 'Resolving...' : 'Submit Resolution'}
                      </button>
                    </div>
                  </div>
                )}
              </div>
            </div>

          </div>
        </div>
      )}
    </div>
  );
};

export default ChatArea;
