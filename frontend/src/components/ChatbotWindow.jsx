import React, { useState, useRef, useEffect } from 'react';
import { Send, Bot, User, Paperclip, Sparkles, ChevronDown, ChevronRight, BrainCircuit, Activity } from 'lucide-react';
import { aiService } from '../services/api';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';

// Helper component for Reasoning block
const ReasoningBlock = ({ reasoning, isComplete }) => {
  const [isOpen, setIsOpen] = useState(false);
  
  if (!reasoning) return null;

  return (
    <div style={{ marginBottom: '16px', borderRadius: '12px', border: '1px solid var(--border-color)', backgroundColor: 'rgba(0,0,0,0.2)', overflow: 'hidden' }}>
      <button 
        onClick={() => setIsOpen(!isOpen)}
        style={{ width: '100%', display: 'flex', alignItems: 'center', gap: '8px', padding: '12px 16px', background: 'transparent', border: 'none', color: 'var(--text-secondary)', cursor: 'pointer', fontSize: '0.9rem', fontWeight: '500', fontFamily: 'inherit' }}
      >
        {isOpen ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
        <BrainCircuit size={16} color="var(--primary-color)" />
        {isComplete ? 'Reasoning Process' : 'Reasoning...'}
      </button>
      {isOpen && (
        <div style={{ padding: '0 16px 16px 16px', color: 'var(--text-muted)', fontSize: '0.9rem', whiteSpace: 'pre-wrap', fontFamily: 'var(--font-body)', lineHeight: '1.6' }}>
          <ReactMarkdown remarkPlugins={[remarkGfm]}>
            {reasoning}
          </ReactMarkdown>
        </div>
      )}
    </div>
  );
};

const ChatbotWindow = ({ userId = 'defaultUser' }) => {
  const [messages, setMessages] = useState([
    { role: 'system', content: 'Hello! I am your AI Support Assistant. How can I help you today?', reasoning: '', timestamp: new Date() }
  ]);
  const [input, setInput] = useState('');
  const [attachedFile, setAttachedFile] = useState(null);
  
  // States: 'idle', 'thinking' (before first chunk), 'reasoning' (inside <think>), 'generating' (outside <think>)
  const [botState, setBotState] = useState('idle'); 
  
  const fileInputRef = useRef(null);
  const messagesEndRef = useRef(null);
  const textareaRef = useRef(null);

  const adjustTextareaHeight = () => {
    const textarea = textareaRef.current;
    if (textarea) {
      textarea.style.height = 'auto';
      textarea.style.height = `${Math.min(textarea.scrollHeight, 200)}px`;
    }
  };

  useEffect(() => {
    adjustTextareaHeight();
  }, [input]);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages, botState]);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setAttachedFile(file);
    }
  };

  const handleSend = async (e) => {
    if (e && e.preventDefault) e.preventDefault();
    if (!input.trim() && !attachedFile) return;

    const userMessage = input.trim() || 'Please process this document.';
    const currentFile = attachedFile;

    // Optimistic UI update
    setMessages(prev => [
      ...prev,
      { role: 'user', content: currentFile ? `[Attached: ${currentFile.name}] \n\n${userMessage}` : userMessage, timestamp: new Date() }
    ]);

    setInput('');
    setAttachedFile(null);
    setBotState('thinking');

    try {
      if (currentFile) {
        await aiService.uploadDocument(currentFile);
      }

      // Add empty system message placeholder for the stream
      setMessages(prev => [...prev, { role: 'system', content: '', reasoning: '', rawBuffer: '', isReasoningComplete: false, timestamp: new Date() }]);

      const promptToSend = currentFile ? `[Attached: ${currentFile.name}] ${userMessage}` : userMessage;

      await aiService.chatStream(promptToSend, userId, (chunk) => {
        setMessages(prev => {
          const newMessages = [...prev];
          const lastIndex = newMessages.length - 1;
          let currentMsg = { ...newMessages[lastIndex] };
          
          currentMsg.rawBuffer += chunk;
          
          // Simple parser for <think>...</think> or <reasoning>...</reasoning>
          let raw = currentMsg.rawBuffer;
          let reasoningText = '';
          let contentText = '';
          let inReasoning = false;
          let reasoningComplete = false;

          // Check if we have <think> or <reasoning>
          const thinkMatch = raw.match(/<(?:think|reasoning)>([\s\S]*?)(?:<\/(?:think|reasoning)>|$)/);
          
          if (thinkMatch) {
            inReasoning = !raw.includes('</think>') && !raw.includes('</reasoning>');
            reasoningComplete = !inReasoning;
            reasoningText = thinkMatch[1];
            
            // Extract everything after the closing tag as content
            const afterThink = raw.split(/<\/(?:think|reasoning)>/);
            if (afterThink.length > 1) {
              contentText = afterThink[1];
            } else {
              // If there's content before <think> (rare but possible)
              const beforeThink = raw.split(/<(?:think|reasoning)>/);
              if (beforeThink[0].trim()) {
                contentText = beforeThink[0];
              }
            }
          } else {
            // No tags found, everything is content
            contentText = raw;
          }

          // Update Status State
          if (inReasoning) {
            setBotState('reasoning');
          } else if (raw.length > 0) {
            setBotState('generating');
          }

          currentMsg.reasoning = reasoningText;
          currentMsg.content = contentText;
          currentMsg.isReasoningComplete = reasoningComplete;

          newMessages[lastIndex] = currentMsg;
          return newMessages;
        });
      });
      setBotState('idle');
    } catch (error) {
      console.error(error);
      setBotState('idle');
      setMessages(prev => {
        const newMsgs = [...prev];
        const lastIndex = newMsgs.length - 1;
        newMsgs[lastIndex] = {
          role: 'system',
          content: 'Sorry, I encountered an error. Please try again later.',
          reasoning: '',
          timestamp: new Date()
        };
        return newMsgs;
      });
    } finally {
      setBotState('idle');
      if (fileInputRef.current) fileInputRef.current.value = '';
      if (textareaRef.current) textareaRef.current.style.height = 'auto';
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend(e);
    }
  };

  const renderStatusIndicator = () => {
    if (botState === 'idle') return null;
    
    let text = "Thinking...";
    let Icon = Sparkles;
    let color = "var(--primary-color)";
    
    if (botState === 'reasoning') {
      text = "Reasoning...";
      Icon = BrainCircuit;
      color = "var(--accent-color)";
    } else if (botState === 'generating') {
      text = "Generating response...";
      Icon = Activity;
      color = "#10b981"; // Emerald green
    }

    return (
      <div className="message-row bot" style={{ animation: 'none' }}>
        <div className="avatar" style={{ background: `linear-gradient(135deg, ${color}, rgba(0,0,0,0.5))` }}>
          <Icon size={20} />
        </div>
        <div className="message-bubble" style={{ display: 'flex', alignItems: 'center', gap: '12px', padding: '16px 24px', border: `1px solid ${color}40`, background: `${color}10` }}>
          <span style={{ fontSize: '0.95rem', fontWeight: '500', color: color }}>{text}</span>
          <div className="typing-indicator" style={{ background: 'transparent', border: 'none', padding: 0 }}>
            <div className="typing-dot" style={{ background: color }}></div>
            <div className="typing-dot" style={{ background: color }}></div>
            <div className="typing-dot" style={{ background: color }}></div>
          </div>
        </div>
      </div>
    );
  };

  return (
    <div className="chat-content" style={{ padding: '0', display: 'flex', flexDirection: 'column', height: '100%' }}>
      <div className="chat-messages" style={{ flex: 1, overflowY: 'auto', padding: '40px', width: '100%', maxWidth: 'none', gap: '24px' }}>
        {messages.map((msg, index) => (
          <div key={index} className={`message-row ${msg.role}`}>
            <div className={`avatar ${msg.role}`}>
              {msg.role === 'system' ? <Sparkles size={20} /> : <User size={20} />}
            </div>
            <div className="message-bubble" style={{ maxWidth: '75%', width: msg.role === 'system' ? '100%' : 'fit-content' }}>
              {msg.role === 'system' ? (
                <>
                  <ReasoningBlock reasoning={msg.reasoning} isComplete={msg.isReasoningComplete || botState === 'idle'} />
                  {msg.content && (
                    <div className="markdown-body">
                      <ReactMarkdown remarkPlugins={[remarkGfm]}>
                        {msg.content}
                      </ReactMarkdown>
                    </div>
                  )}
                </>
              ) : (
                <div style={{ whiteSpace: 'pre-wrap' }}>{msg.content}</div>
              )}
              {msg.timestamp && (
                <div style={{ fontSize: '0.7rem', color: msg.role === 'user' ? 'rgba(0,0,0,0.5)' : 'var(--text-muted)', textAlign: msg.role === 'user' ? 'right' : 'left', marginTop: '8px' }}>
                  {msg.timestamp.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                </div>
              )}
            </div>
          </div>
        ))}
        
        {renderStatusIndicator()}
        
        <div ref={messagesEndRef} style={{ height: '40px' }} />
      </div>

      <div className="input-container-wrapper">
        {attachedFile && (
          <div style={{ maxWidth: '850px', margin: '0 auto 16px auto', display: 'flex', alignItems: 'center', gap: '12px', padding: '12px 20px', backgroundColor: 'var(--bg-surface)', borderRadius: '16px', width: 'fit-content', border: '1px solid var(--primary-color)', boxShadow: '0 8px 32px rgba(0,0,0,0.2)' }}>
            <Paperclip size={16} color="var(--primary-color)" />
            <span style={{ fontSize: '0.9rem', fontWeight: '500' }}>{attachedFile.name}</span>
            <button type="button" onClick={() => setAttachedFile(null)} style={{ background: 'rgba(255,255,255,0.1)', border: 'none', cursor: 'pointer', marginLeft: '12px', color: 'var(--text-secondary)', width: '24px', height: '24px', borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>&times;</button>
          </div>
        )}
        <form onSubmit={handleSend} className="chat-input-form">
          <button
            type="button"
            className="attach-btn"
            onClick={() => fileInputRef.current?.click()}
            disabled={botState !== 'idle'}
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

          <textarea
            ref={textareaRef}
            className="user-input"
            placeholder="Type your message here..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={handleKeyDown}
            disabled={botState !== 'idle'}
            rows={1}
            style={{
              resize: 'none',
              overflowY: 'auto',
              minHeight: '28px',
              maxHeight: '200px',
              paddingTop: '6px',
              paddingBottom: '6px'
            }}
          />

          <button type="submit" className="send-btn" disabled={botState !== 'idle' || (!input.trim() && !attachedFile)}>
            <Send size={18} style={{ marginLeft: '-2px' }} />
          </button>
        </form>
      </div>
    </div>
  );
};

export default ChatbotWindow;
