import React, { useState } from 'react';
import { X, PlusCircle } from 'lucide-react';

const NewTicketModal = ({ onClose, onSubmit }) => {
  const [formData, setFormData] = useState({
    summary: '',
    priority: 'LOW'
  });
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.summary) return;
    
    setIsSubmitting(true);
    try {
      await onSubmit(formData);
      onClose();
    } catch (err) {
      console.error(err);
      alert('Failed to create ticket.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px', borderBottom: '1px solid var(--border-light)', paddingBottom: '20px' }}>
          <h2 style={{ fontSize: '1.5rem', display: 'flex', alignItems: 'center', gap: '12px' }}>
            <div style={{ background: 'rgba(59, 130, 246, 0.2)', padding: '10px', borderRadius: '12px', color: 'var(--accent-color)', display: 'flex' }}>
              <PlusCircle size={24} />
            </div>
            Create New Ticket
          </h2>
          <button className="btn-icon" onClick={onClose} style={{ padding: '10px', borderRadius: '50%' }}>
            <X size={20} />
          </button>
        </div>
        
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          <div className="form-group" style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            <label style={{ fontSize: '0.9rem', color: 'var(--text-secondary)', fontWeight: '600' }}>Priority Level</label>
            <select 
              value={formData.priority}
              onChange={e => setFormData({...formData, priority: e.target.value})}
              style={{ width: '100%' }}
            >
              <option value="LOW">Low - General query or minor issue</option>
              <option value="NORMAL">Normal - Feature not working as expected</option>
              <option value="HIGH">High - System failure or major bug</option>
            </select>
          </div>
          
          <div className="form-group" style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            <label style={{ fontSize: '0.9rem', color: 'var(--text-secondary)', fontWeight: '600' }}>Issue Description</label>
            <textarea 
              required 
              placeholder="Describe the issue in detail. What happened? What did you expect to happen?"
              value={formData.summary}
              onChange={e => setFormData({...formData, summary: e.target.value})}
              style={{ minHeight: '120px', resize: 'vertical', width: '100%' }}
            />
          </div>
          
          <div style={{ display: 'flex', gap: '16px', justifyContent: 'flex-end', marginTop: '16px' }}>
            <button type="button" className="btn-icon" onClick={onClose} disabled={isSubmitting}>
              Cancel
            </button>
            <button type="submit" className="btn-icon btn-primary" disabled={isSubmitting} style={{ padding: '12px 32px' }}>
              {isSubmitting ? 'Creating...' : 'Submit Ticket'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default NewTicketModal;
