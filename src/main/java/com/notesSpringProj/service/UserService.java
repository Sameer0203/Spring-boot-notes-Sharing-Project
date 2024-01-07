package com.notesSpringProj.service;

import java.util.List;

import com.notesSpringProj.payloads.UserDto;

public interface UserService {
	
	UserDto registerNewUser(UserDto user, String role);
	UserDto creteUser(UserDto user);
	UserDto updateUser(UserDto user, Integer userId);
	UserDto getUserById(Integer userId);
	List<UserDto> getAllUsers();
	void deleteUser(Integer userId);

}
