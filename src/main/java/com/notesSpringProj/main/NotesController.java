package com.notesSpringProj.main;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.notesSpringProj.config.AppConstants;
import com.notesSpringProj.exceptions.TooManyRequestsException;
import com.notesSpringProj.payloads.ApiResponse;
import com.notesSpringProj.payloads.NotesDto;
import com.notesSpringProj.payloads.NotesResponse;
import com.notesSpringProj.payloads.ShareResponse;
import com.notesSpringProj.rateLimiting.RateLimiter;
import com.notesSpringProj.security.JwtTokenHelper;
import com.notesSpringProj.service.NotesService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class NotesController {
	
	// RateLimiter instance injected through constructor
	private final RateLimiter rateLimiter;

    @Autowired
    public NotesController(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    // JWT secret key provided from application properties
    @Value("${jwt.secret}")
    private String jwtSecret;
    
	@Autowired
	NotesService notesService;
	
	@Autowired
	JwtTokenHelper jwtTokenHelper;
	
	// API endpoint to get notes by ID
	@GetMapping("/notes/{notesId}")
	public ResponseEntity<NotesDto> getNotessById(@PathVariable("notesId") Integer notesId,
			@RequestHeader("Authorization") String authorizationHeader){
		// Retrieve client ID from authorization header
		String clientId = getClientId(authorizationHeader);
		
		// Check rate limits
        if (!rateLimiter.allowRequest(clientId)) {
            throw new TooManyRequestsException("Too many requests, try after some time.");
        }
		
		// Get notes by ID and return the response
		NotesDto notesDto = this.notesService.getNotes(notesId);
		return new ResponseEntity<NotesDto>(notesDto, HttpStatus.FOUND);
	}
	
	// API endpoint to get all notes
	@GetMapping("/notes") 
	public ResponseEntity<NotesResponse> getAllNotess(
			@RequestHeader("Authorization") String authorizationHeader){

		// Retrieve client ID from authorization header
		String clientId = getClientId(authorizationHeader);
		
		// Check rate limits
        if (!rateLimiter.allowRequest(clientId)) {
            throw new TooManyRequestsException("Too many requests, try after some time.");
        }
		
		// Get all notes for the user and return the response
		NotesResponse notesDtos = this.notesService.getAllNotess(clientId);
		return new ResponseEntity<NotesResponse>(notesDtos, HttpStatus.FOUND);
	}
	
	// API endpoint to create notes
	@PostMapping("/notes") 
	public ResponseEntity<NotesDto> createNotes(@RequestBody NotesDto notesDto, @RequestHeader("Authorization") String authorizationHeader){
		// Retrieve client ID from authorization header
		String clientId = getClientId(authorizationHeader);
		
		// Check rate limits
        if (!rateLimiter.allowRequest(clientId)) {
            throw new TooManyRequestsException("Too many requests, try after some time.");
        }
		
		// Create notes and return the response
		return new ResponseEntity<NotesDto>(
				this.notesService.createNotes(notesDto), HttpStatus.CREATED);
	}
	
	// API endpoint to update notes
	@PutMapping("/notes")
	public ResponseEntity<NotesDto> updateNotes(@Valid @RequestBody NotesDto notesDto,
			@RequestHeader("Authorization") String authorizationHeader){
		// Retrieve client ID from authorization header
		String clientId = getClientId(authorizationHeader);
		
		// Check rate limits
        if (!rateLimiter.allowRequest(clientId)) {
            throw new TooManyRequestsException("Too many requests, try after some time.");
        }
		
		// Update notes and return the response
		Integer notesId = notesDto.getNotesId();
		NotesDto updatedNotes = this.notesService.updateNotes(notesDto, notesId);
		return new ResponseEntity<NotesDto>(updatedNotes, HttpStatus.OK);
	}
	
	// API endpoint to delete notes
	@DeleteMapping("/notes/{notesId}")
	public ResponseEntity<ApiResponse> deleteNotes(@Valid @PathVariable("notesId") Integer notesId,
			@RequestHeader("Authorization") String authorizationHeader){
		// Retrieve client ID from authorization header
		String clientId = getClientId(authorizationHeader);
		
		// Check rate limits
        if (!rateLimiter.allowRequest(clientId)) {
            throw new TooManyRequestsException("Too many requests, try after some time.");
        }
		
		// Delete notes and return the response
		this.notesService.deleteNotes(notesId);
		return new ResponseEntity<ApiResponse>(
				new ApiResponse("Notes deleted Successfully", true), HttpStatus.OK
				);
	}
	
	// Search API endpoint
	@GetMapping("/search")
	public ResponseEntity<List<NotesDto>> searchNotesByTitle(
			@RequestParam(value = "q", defaultValue = AppConstants.SEARCH_KEYWORD, required = false) String q,
			@RequestHeader("Authorization") String authorizationHeader) {
		// Retrieve client ID from authorization header
		String clientId = getClientId(authorizationHeader);
		
		// Check rate limits
        if (!rateLimiter.allowRequest(clientId)) {
            throw new TooManyRequestsException("Too many requests, try after some time.");
        }
		
		// Search notes and return the response
		List<NotesDto> result = this.notesService.searchNotess(q, clientId);
		return new ResponseEntity<List<NotesDto>>(result, HttpStatus.OK);
	}
	
	// Share API endpoint
	@PostMapping("/notes/{userId}/share")
	public ResponseEntity<ShareResponse> shareNotes(@RequestBody NotesDto notesDto, @PathVariable("userId") Integer userId,
			@RequestHeader("Authorization") String authorizationHeader){
		// Retrieve client ID from authorization header
		String clientId = getClientId(authorizationHeader);
		
		// Check rate limits
        if (!rateLimiter.allowRequest(clientId)) {
            throw new TooManyRequestsException("Too many requests, try after some time.");
        }

		// Share notes and return the response
		NotesDto shareNotes = this.notesService.shareNotes(notesDto, userId);
		ShareResponse shareResponse = new ShareResponse();
		shareResponse.setMessage("Notes shared successfully with the user : "+userId);
		shareResponse.setStatus("0");
		shareResponse.setNotesDto(shareNotes);
		return new ResponseEntity<ShareResponse>(shareResponse, HttpStatus.OK);
	}
	
	// Helper method to extract client ID from the JWT token
	private String getClientId(String authorizationHeader) {
        if (authorizationHeader != null) {
            String token = authorizationHeader; // Remove "Bearer " prefix

            // Decode JWT token and retrieve claims
            Claims claims = this.jwtTokenHelper.getAllClaimsFromToken(token);

            return claims.getSubject(); // Assuming the client ID is stored in the subject claim
        }

        throw new IllegalArgumentException("Invalid Authorization header");
    }
}
