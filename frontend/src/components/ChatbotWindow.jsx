import React, { useState, useRef, useEffect } from 'react';
import { Send, Bot, User, Loader2, Paperclip } from 'lucide-react';
import { aiService } from '../services/api';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';

const ChatbotWindow = ({ userId = 'defaultUser' }) => {
  const [messages, setMessages] = useState([
    { role: 'system', content: 'Hello! I am your AI Support Assistant. How can I help you today?' }
  ]);
  const [input, setInput] = useState('');
  const [attachedFile, setAttachedFile] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const fileInputRef = useRef(null);
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages, isLoading]);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setAttachedFile(file);
    }
  };

  const handleSend = async (e) => {
    e.preventDefault();
    if (!input.trim() && !attachedFile) return;

    const userMessage = input.trim() || 'Please process this document.';
    const currentFile = attachedFile;
    
    // Optimistic UI update
    setMessages(prev => [
      ...prev, 
      { role: 'user', content: currentFile ? `[Attached: ${currentFile.name}] ${userMessage}` : userMessage }
    ]);
    
    setInput('');
    setAttachedFile(null);
    setIsLoading(true);

    try {
      if (currentFile) {
        // Upload document first
        await aiService.uploadDocument(currentFile);
      }
      
      // Then send the prompt (the backend now has RAG configured to read the vector store)
      const promptToSend = currentFile ? `[Attached: ${currentFile.name}] ${userMessage}` : userMessage;
      const response = await aiService.chat(promptToSend, userId);
      setMessages(prev => [...prev, { role: 'system', content: response }]);
    } catch (error) {
      console.error(error);
      setMessages(prev => [...prev, { role: 'system', content: 'Sorry, I encountered an error. Please try again later.' }]);
    } finally {
      setIsLoading(false);
      if (fileInputRef.current) fileInputRef.current.value = '';
    }
  };

  return (
    <div className="chat-content" style={{ padding: '0', display: 'flex', flexDirection: 'column', height: '100%' }}>
      <div className="chat-messages" style={{ flex: 1, overflowY: 'auto', padding: '40px', width: '100%', maxWidth: 'none', gap: '24px' }}>
        {messages.map((msg, index) => (
          <div key={index} className={`message-row ${msg.role}`}>
            <div className={`avatar ${msg.role}`}>
              {msg.role === 'system' ? <Bot size={20} /> : <User size={20} />}
            </div>
            <div className="message-bubble" style={{ maxWidth: '60%' }}>
              {msg.role === 'system' ? (
                <div className="markdown-body">
                  <ReactMarkdown remarkPlugins={[remarkGfm]}>
                    {msg.content}
                  </ReactMarkdown>
                </div>
              ) : (
                msg.content
              )}
            </div>
          </div>
        ))}
        {isLoading && (
          <div className="message-row bot">
            <div className="avatar">
              <Bot size={20} />
            </div>
            <div className="message-bubble" style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <div className="typing-indicator">
                <div className="typing-dot"></div>
                <div className="typing-dot"></div>
                <div className="typing-dot"></div>
              </div>
            </div>
          </div>
        )}
        <div ref={messagesEndRef} />
      </div>

      <div className="input-container-wrapper">
        {attachedFile && (
          <div style={{ maxWidth: '800px', margin: '0 auto 12px auto', display: 'flex', alignItems: 'center', gap: '8px', padding: '8px 16px', backgroundColor: 'var(--bg-surface)', borderRadius: '8px', width: 'fit-content', border: '1px solid var(--primary-color)' }}>
            <Paperclip size={14} color="var(--primary-color)" />
            <span style={{ fontSize: '0.85rem' }}>{attachedFile.name}</span>
            <button type="button" onClick={() => setAttachedFile(null)} style={{ background: 'none', border: 'none', cursor: 'pointer', marginLeft: '8px', color: 'var(--text-secondary)' }}>&times;</button>
          </div>
        )}
        <form onSubmit={handleSend} className="chat-input-form">
          
          <button 
            type="button" 
            className="attach-btn" 
            onClick={() => fileInputRef.current?.click()}
            disabled={isLoading}
            title="Attach Document"
          >
            <Paperclip size={20} />
          </button>
          
          <input 
            type="file" 
            ref={fileInputRef}
            style={{ display: 'none' }}
            onChange={handleFileChange}
          />

          <input
            type="text"
            className="user-input"
            placeholder="Type your message here..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
            disabled={isLoading}
          />
          
          <button type="submit" className="send-btn" disabled={isLoading || (!input.trim() && !attachedFile)}>
            <Send size={18} style={{ marginLeft: '-2px' }} />
          </button>
        </form>
      </div>
    </div>
  );
};

export default ChatbotWindow;
