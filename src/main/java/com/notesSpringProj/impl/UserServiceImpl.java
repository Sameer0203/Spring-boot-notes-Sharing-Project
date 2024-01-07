package com.notesSpringProj.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.notesSpringProj.bean.Role;
import com.notesSpringProj.bean.User;
import com.notesSpringProj.config.AppConstants;
import com.notesSpringProj.exceptions.ResourceNotFoundException;
import com.notesSpringProj.payloads.UserDto;
import com.notesSpringProj.repository.RoleRepo;
import com.notesSpringProj.repository.UserRepo;
import com.notesSpringProj.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    // Autowired to inject instances of UserRepo, ModelMapper, PasswordEncoder, and RoleRepo
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    // Method to create a new user
    @Override
    public UserDto creteUser(UserDto userDto) {
        // Map UserDto to User entity and save it
        User user = this.dtoToUser(userDto);
        User savedUser = this.userRepo.save(user);

        // Map the saved User entity back to UserDto and return
        return this.userToDto(savedUser);
    }

    // Method to update an existing user
    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        // Find user by ID or throw ResourceNotFoundException
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " id ", userId));

        // Update user with data from UserDto
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        // Save the updated user and map the result to UserDto
        User updatedUser = this.userRepo.save(user);
        return this.userToDto(updatedUser);
    }

    // Method to get a user by ID
    @Override
    public UserDto getUserById(Integer userId) {
        // Find user by ID or throw ResourceNotFoundException
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " id ", userId));

        // Map the user to UserDto and return
        return this.userToDto(user);
    }

    // Method to get all users
    @Override
    public List<UserDto> getAllUsers() {
        // Retrieve all users
        List<User> users = this.userRepo.findAll();
        List<UserDto> usersDto = new ArrayList<UserDto>();

        // Map each user to UserDto and add to the list
        for (int i = 0; i < users.size(); i++) {
            usersDto.add(this.userToDto(users.get(i)));
        }

        return usersDto;
    }

    // Method to delete a user by ID
    @Override
    public void deleteUser(Integer userId) {
        // Find user by ID or throw ResourceNotFoundException
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " id ", userId));

        // Delete the user
        this.userRepo.delete(user);
    }

    // Method to convert UserDto to User entity
    public User dtoToUser(UserDto userDto) {
        // Use ModelMapper to map UserDto to User entity
        User user = this.modelMapper.map(userDto, User.class);
        return user;
    }

    // Method to convert User entity to UserDto
    public UserDto userToDto(User user) {
        // Use ModelMapper to map User entity to UserDto
        UserDto userDto = this.modelMapper.map(user, UserDto.class);
        return userDto;
    }

    // Method to register a new user
    @Override
    public UserDto registerNewUser(UserDto userDto, String role) {
        // Map UserDto to User entity
        User user = this.modelMapper.map(userDto, User.class);

        // Encode the user's password
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        Role roleUser = null;
        // Get the "NORMAL_USER" role from the database
        if(role.equalsIgnoreCase("normal")) roleUser = this.roleRepo.findById(AppConstants.NORMAL_USER).get();
        else if(role.equalsIgnoreCase("admin")) roleUser = this.roleRepo.findById(AppConstants.ADMIN_USER).get();
        
        // Add the role to the user's roles
        user.getRoles().add(roleUser);

        // Save the user and map the result to UserDto
        User saveUser = this.userRepo.save(user);
        return this.modelMapper.map(saveUser, UserDto.class);
    }

}
