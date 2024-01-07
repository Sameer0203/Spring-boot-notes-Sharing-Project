package com.notesSpringProj.payloads;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthResponse {
	
	private String token;
	private UserDto userDto;

}
