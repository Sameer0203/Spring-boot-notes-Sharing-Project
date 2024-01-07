package com.notesSpringProj.Repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.notesSpringProj.bean.Notes;
import com.notesSpringProj.bean.Role;
import com.notesSpringProj.bean.User;
import com.notesSpringProj.repository.UserRepo;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepoTest {
	
	@Autowired
	private UserRepo userRepo;
	
	@Test
	void findByEmail() {
		List<Notes> notes = new ArrayList<>();
		Set<Role> roles = new HashSet<>();
		User user = new User("testUser", "testUser@gmail.com", "test", "test123", notes, roles);
		this.userRepo.save(user);
		User actualResult = this.userRepo.findByEmail("testUser@gmail.com").orElseThrow();
		
		assertThat(actualResult.getEmail()).isEqualTo("testUser@gmail.com");
	}

	@AfterEach
	void tearDown() {
		System.out.println("Tearing down");
		User actualResult = this.userRepo.findByEmail("testUser@gmail.com").orElseThrow();
		this.userRepo.delete(actualResult);
	}
	
}
