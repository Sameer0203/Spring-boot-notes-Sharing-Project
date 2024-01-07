package com.notesSpringProj.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.notesSpringProj.config.AppConstants;
import com.notesSpringProj.exceptions.InvalidUserDetailsException;
import com.notesSpringProj.payloads.JwtAuthRequest;
import com.notesSpringProj.payloads.JwtAuthResponse;
import com.notesSpringProj.payloads.UserDto;
import com.notesSpringProj.security.JwtTokenHelper;
import com.notesSpringProj.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
	
	// Autowired instances for dependencies
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	

	// API endpoint for user login
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {
		// Authenticate user credentials
		this.authenticate(request.getUserId(), request.getPassword());
		
		// Retrieve user details and generate JWT token
		UserDto userDto = this.userService.getUserById(Integer.valueOf(request.getUserId()));
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUserId());
		String token = this.jwtTokenHelper.generateToken(userDetails);
		
		// Build and return the response with JWT token and user details
		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		response.setUserDto(userDto);
		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
	}

	// Helper method for user authentication
	private void authenticate(String userId, String password) throws Exception {
		// Use Spring Security's AuthenticationManager to authenticate user credentials
		UsernamePasswordAuthenticationToken authenticationToken = new 
				UsernamePasswordAuthenticationToken(userId, password);
		try {
			this.authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			// Throw exception for invalid user details
			throw new InvalidUserDetailsException("Password", password, -1);
		}
	}
	
	// API endpoint for user registration
	@PostMapping("/signup")
	public ResponseEntity<UserDto> registerUser(@Valid 
			@RequestParam(value = "role", defaultValue = AppConstants.ROLE, required = false) String role,
			@RequestBody UserDto userDto) {
		// Register a new user and return the registered user details
		UserDto registerNewUser = this.userService.registerNewUser(userDto,role);
		return new ResponseEntity<UserDto>(registerNewUser, HttpStatus.CREATED);
	}

}
