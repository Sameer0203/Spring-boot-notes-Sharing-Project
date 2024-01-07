package com.notesSpringProj.Repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.notesSpringProj.bean.Notes;
import com.notesSpringProj.bean.Role;
import com.notesSpringProj.bean.User;
import com.notesSpringProj.repository.NotesRepo;
import com.notesSpringProj.repository.UserRepo;

@DataJpaTest
@ActiveProfiles("test")
public class NotesRepoTest {
	
	@Autowired
	NotesRepo notesRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Test
	void testFindByKeyword() {

		User user = new User();
		user.setName("testUser");
		user.setEmail("test@gmail.com");
		
		this.userRepo.save(user);
		
		String title = "testNote";
		String content = "this is test content";
		Integer userId = 1;
		Set<Role> roles = new HashSet<>();
		Notes notes = new Notes();
		notes.setUser(user);
		notes.setTitle(title);
		notes.setContent(content);
		
		this.notesRepo.save(notes);
		List<Notes> actualResult = this.notesRepo.findByUser(user);
		
		assertThat(actualResult.get(0).getTitle()).isEqualTo(title);
		assertThat(actualResult.get(0).getContent()).isEqualTo(content);
		assertThat(actualResult.get(0).getUser()).isEqualTo(user);
	}


}
