package com.notesSpringProj.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.notesSpringProj.exceptions.ResourceNotFoundException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// 1. get token
		String requestToken = request.getHeader("Authorization");
		
		String userId=null;
		String token=null;
		
		// Check if the request contains a valid JWT token
		if(requestToken!=null) {

			token = requestToken;
			try {
			userId = this.jwtTokenHelper.getUsernameFromToken(token);
			}catch (IllegalArgumentException e) {
				// Handle exception if unable to get JWT Token
				System.out.println("Unable to get JWT Token");
				throw new ResourceNotFoundException("Unable to get JWT Token", "error code", -1);
			}
			catch (ExpiredJwtException e) {
				// Handle exception if JWT Token has expired
				System.out.println("JWT Token has expired");
				throw new ResourceNotFoundException("JWT Token has expired", "error code", -1);
			}
			catch (MalformedJwtException e) {
				// Handle exception if JWT is invalid
				System.out.println("Invalid JWT");
				throw new ResourceNotFoundException("Invalid JWT", "error code", -1);
			}
			
		}else {
			// Handle case when JWT Token is null
			System.out.println("JWT Token is null");
		}
		
		// Once we get the token, now validate it
		if(token!=null) {
			if(userId!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails userDetails = this.customUserDetailService.loadUserByUsername(userId);
				if(this.jwtTokenHelper.validateToken(token, userDetails)) {
					// Authentication process if the token is valid
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}else {
					// Handle case when the JWT token is invalid
					System.out.println("Invalid JWT token");
					throw new ResourceNotFoundException("Invalid JWT token", "error code", -1);
				}
			}		
			else {
				// Handle case when username is null or context is not null
				System.out.println("Username is null or context is not null");
				throw new ResourceNotFoundException("JWT token", "error code", -1);
			}
		}
		filterChain.doFilter(request, response);
	}

}
