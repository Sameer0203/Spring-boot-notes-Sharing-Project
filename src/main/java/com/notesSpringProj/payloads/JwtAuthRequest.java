package com.notesSpringProj.payloads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthRequest {
	
	private String userId;
	private String password;

}
