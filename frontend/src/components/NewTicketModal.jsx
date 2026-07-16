import React, { useState } from 'react';
import { X } from 'lucide-react';

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
        <div className="modal-header">
          <h2>Create New Ticket</h2>
          <button className="close-btn" onClick={onClose}>
            <X size={24} />
          </button>
        </div>
        
        <form onSubmit={handleSubmit}>
          
          <div className="form-group">
            <label>Priority</label>
            <select 
              value={formData.priority}
              onChange={e => setFormData({...formData, priority: e.target.value})}
            >
              <option value="LOW">Low</option>
              <option value="NORMAL">Normal</option>
              <option value="HIGH">High</option>
            </select>
          </div>
          
          <div className="form-group">
            <label>Issue Description</label>
            <textarea 
              required 
              placeholder="Describe the issue in detail..."
              value={formData.summary}
              onChange={e => setFormData({...formData, summary: e.target.value})}
            />
          </div>
          
          <div className="action-buttons">
            <button type="button" className="btn-secondary" onClick={onClose} disabled={isSubmitting}>
              Cancel
            </button>
            <button type="submit" className="btn-primary" disabled={isSubmitting}>
              {isSubmitting ? 'Creating...' : 'Create Ticket'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default NewTicketModal;
