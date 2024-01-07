package com.notesSpringProj.payloads;

import lombok.Data;

@Data
public class ShareResponse {

	private String message;
	private String status;
	private NotesDto notesDto;
	
}
