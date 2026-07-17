package com.project.helpdesk.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.project.helpdesk.models.SupportServiceEvent;
import com.project.helpdesk.models.Ticket;
import com.project.helpdesk.tools.SupportServiceTools;

import reactor.core.publisher.Flux;

@Service
public class SupportAiService {
	
	@Autowired
	private ChatClient chatClient;
	
	@Autowired
	private SupportServiceTools supportServiceTools;
	
	@Value("classpath:/prompts/supportPrompt")
	private Resource supportPrompt;
	
	@Async
	@EventListener
	public void chatResponse(SupportServiceEvent event){
		
		String userQuery = """
				You have been assigned a new ticket. You MUST resolve this ticket using your tools immediately. Do not just acknowledge it.
				
				ticket id : %s
				
				summary : %s
				
				""".formatted(event.getTicket().getId(), event.getTicket().getSummary());
		
		this.chatClient
				.prompt()
				.system(system->system.text(this.supportPrompt))
				.user(user->user.text(userQuery))
				.advisors(a->a.param(ChatMemory.CONVERSATION_ID, event.getUserId()))
				.tools(this.supportServiceTools)
				.call()
				.content();
	
	}
}
