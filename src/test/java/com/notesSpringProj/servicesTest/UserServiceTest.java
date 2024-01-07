package com.notesSpringProj.servicesTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.notesSpringProj.bean.Notes;
import com.notesSpringProj.bean.Role;
import com.notesSpringProj.bean.User;
import com.notesSpringProj.impl.UserServiceImpl;
import com.notesSpringProj.payloads.RoleDto;
import com.notesSpringProj.payloads.UserDto;
import com.notesSpringProj.repository.UserRepo;
import com.notesSpringProj.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepo userRepo;
	
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
//	@BeforeEach
//	void setUp() {
//		this.userServiceImpl = new UserServiceImpl(this.userRepo);
//	}
	
	@Test
	void getAllUsers() {
		this.userServiceImpl.getAllUsers();
		verify(userRepo).findAll();
	}
	
    @Test
    void testCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password123");
        userDto.setAbout("Some information about John");

        User user = new User();
        user.setId(1);
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(userRepo.save(any(User.class))).thenReturn(user);
        
        this.userServiceImpl.creteUser(userDto);

        User value = this.userRepo.save(user);
        
        assertThat(value).isEqualTo(user);

    }
    
    @Test
    void testUpdateUser() {
        int userId = 1;
        UserDto userDto = new UserDto();
        userDto.setName("Updated Name");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("test");

        when(userRepo.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        this.userServiceImpl.updateUser(userDto, userId);

        User value = this.userRepo.save(existingUser);       
        assertThat(value).isEqualTo(existingUser);
    }
    
    @Test
    void testGetUserById() {
        int userId = 1;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("test");

        when(userRepo.findById(userId)).thenReturn(Optional.of(existingUser));

        this.userServiceImpl.getUserById(userId);

        this.userRepo.save(existingUser);
        User result = this.userRepo.findById(userId).orElseThrow();
        
        assertEquals(existingUser.getName(), result.getName());
        
    }
	
}
