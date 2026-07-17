package com.project.helpdesk.tools;

import java.util.UUID;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.project.helpdesk.models.Ticket;
import com.project.helpdesk.service.SupportService;

@Component
public class SupportServiceTools {

	private SupportService supportService;
	
	SupportServiceTools(SupportService supportService){
		this.supportService = supportService;
	}
	
	@Tool(description="resolve a ticket. Ticket solution needs to be provided by you based on the summary.")
	public Ticket resolveTicket(@ToolParam(description="Ticket details") UUID id, @ToolParam(description="Ticket summary") String summary, @ToolParam(description="Ticket solution which needs to be provided b you based on the summary.") String solution) {
		return this.supportService.resolveTicket(id, solution);
	}
}
