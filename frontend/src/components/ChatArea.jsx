import React, { useState } from 'react';
import { Sparkles, PlusCircle, CheckCircle, Trash2, List, Edit2, ShieldAlert, ArrowLeft } from 'lucide-react';

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
    <div className="main-area" style={{ height: '100%', background: 'transparent', border: 'none', boxShadow: 'none' }}>
      {!ticket ? (
        <div className="empty-state">
          <div style={{ padding: '30px', background: 'rgba(255,255,255,0.03)', borderRadius: '30px', border: '1px solid var(--border-color)', marginBottom: '30px' }}>
            <Sparkles size={64} color="var(--primary-color)" />
          </div>
          <h2 style={{ fontFamily: 'var(--font-heading)', fontSize: '2rem', marginBottom: '16px' }}>Ready to help.</h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '1.1rem', maxWidth: '400px', textAlign: 'center' }}>Select a ticket from the sidebar to view its details or switch to the AI Support Assistant to get immediate help.</p>
        </div>
      ) : (
        <div className="chat-content" style={{ height: '100%' }}>
          <div className="chat-messages" style={{ maxWidth: '900px' }}>
            
            {/* User Message Bubble */}
            <div className="message-row user">
              <div className="avatar">
                {ticket.username ? ticket.username.substring(0, 2).toUpperCase() : 'U'}
              </div>
              <div className="message-bubble" style={{ width: '100%' }}>
                <div style={{ fontWeight: 600, marginBottom: '12px', fontSize: '0.85rem', color: 'var(--text-secondary)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <span style={{ color: 'var(--primary-color)' }}>{ticket.username || 'Unknown'}</span>
                    <span style={{ color: 'var(--border-light)' }}>•</span>
                    <span>{new Date(ticket.createdOn).toLocaleString()}</span>
                  </div>
                  {!isEditing && (
                    <button className="btn-icon" onClick={handleEditClick} style={{ padding: '6px 12px', fontSize: '0.8rem' }}>
                      <Edit2 size={14} /> Edit
                    </button>
                  )}
                </div>
                {isEditing ? (
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                    <div className="form-group" style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                      <label style={{ fontSize: '0.9rem', color: 'var(--text-secondary)', fontWeight: '500' }}>Priority</label>
                      <select 
                        value={editFormData.priority} 
                        onChange={e => setEditFormData({...editFormData, priority: e.target.value})}
                        style={{ padding: '8px 16px', maxWidth: '200px' }}
                      >
                        <option value="LOW">LOW</option>
                        <option value="NORMAL">NORMAL</option>
                        <option value="HIGH">HIGH</option>
                      </select>
                    </div>
                    <div className="form-group">
                      <textarea 
                        style={{ width: '100%', resize: 'vertical', minHeight: '80px' }}
                        value={editFormData.summary}
                        onChange={e => setEditFormData({...editFormData, summary: e.target.value})}
                      />
                    </div>
                    <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end', marginTop: '8px' }}>
                      <button className="btn-icon" onClick={() => setIsEditing(false)}>Cancel</button>
                      <button className="btn-icon btn-primary" onClick={handleEditSubmit} disabled={isSavingEdit}>
                        {isSavingEdit ? 'Saving...' : 'Save Changes'}
                      </button>
                    </div>
                  </div>
                ) : (
                  <div style={{ fontSize: '1.15rem', lineHeight: '1.6', fontWeight: '500', color: 'var(--text-primary)' }}>
                    {ticket.summary}
                  </div>
                )}
              </div>
            </div>

            {/* System/Support Response Bubble */}
            <div className="message-row system">
              <div className="avatar system">
                <ShieldAlert size={22} />
              </div>
              <div className="message-bubble" style={{ width: '100%', maxWidth: '100%' }}>
                <div style={{ fontWeight: 600, marginBottom: '16px', fontSize: '0.85rem', color: 'var(--text-secondary)', display: 'flex', gap: '8px' }}>
                  <span style={{ color: 'var(--accent-color)' }}>Support System</span>
                  <span style={{ color: 'var(--border-light)' }}>•</span>
                  <span>{new Date(ticket.updatedOn).toLocaleString()}</span>
                </div>
                
                <div style={{ display: 'flex', gap: '16px', marginBottom: '24px', flexWrap: 'wrap' }}>
                  <div style={{ background: 'rgba(255,255,255,0.02)', border: '1px solid var(--border-color)', padding: '16px', borderRadius: '16px', flex: '1', minWidth: '200px' }}>
                    <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginBottom: '8px', textTransform: 'uppercase', letterSpacing: '0.05em', fontWeight: '600' }}>Priority</div>
                    <div style={{ fontSize: '1.1rem', fontWeight: '600' }}>{ticket.priority}</div>
                  </div>
                  <div style={{ background: 'rgba(255,255,255,0.02)', border: '1px solid var(--border-color)', padding: '16px', borderRadius: '16px', flex: '1', minWidth: '200px' }}>
                    <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginBottom: '8px', textTransform: 'uppercase', letterSpacing: '0.05em', fontWeight: '600' }}>Status</div>
                    <div><span className={`ticket-status ${ticket.status?.toLowerCase() || 'open'}`}>{ticket.status}</span></div>
                  </div>
                </div>

                {ticket.status === 'RESOLVED' && ticket.solution && (
                  <div style={{ marginBottom: '24px', padding: '24px', backgroundColor: 'rgba(16, 185, 129, 0.05)', borderRadius: '16px', border: '1px solid rgba(16, 185, 129, 0.2)', borderLeft: '4px solid #10b981' }}>
                    <div style={{ fontWeight: 700, marginBottom: '12px', color: '#34d399', display: 'flex', alignItems: 'center', gap: '8px' }}>
                      <CheckCircle size={18} /> Resolution
                    </div>
                    <div style={{ color: 'var(--text-primary)', whiteSpace: 'pre-wrap', lineHeight: '1.7', fontSize: '1.05rem' }}>{ticket.solution}</div>
                  </div>
                )}
                
                <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
                  {ticket.status !== 'RESOLVED' && !isResolvingMode && (
                    <button 
                      className="btn-icon btn-primary" 
                      onClick={() => setIsResolvingMode(true)}
                    >
                      <CheckCircle size={18} /> Mark as Resolved
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
                    style={{ color: '#f87171', borderColor: 'rgba(239, 68, 68, 0.3)', background: 'rgba(239, 68, 68, 0.05)' }}
                  >
                    <Trash2 size={18} /> {isDeleting ? 'Deleting...' : 'Delete Ticket'}
                  </button>
                </div>

                {isResolvingMode && (
                  <div style={{ marginTop: '24px', display: 'flex', flexDirection: 'column', gap: '16px', background: 'rgba(0,0,0,0.2)', padding: '24px', borderRadius: '20px', border: '1px solid var(--border-light)' }}>
                    <div style={{ fontWeight: '600', color: 'var(--text-primary)' }}>Provide Resolution Details</div>
                    <div className="form-group">
                      <textarea 
                        style={{ width: '100%', resize: 'vertical', minHeight: '120px' }}
                        placeholder="Type the solution here..."
                        value={resolutionText}
                        onChange={(e) => setResolutionText(e.target.value)}
                      />
                    </div>
                    <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end' }}>
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
