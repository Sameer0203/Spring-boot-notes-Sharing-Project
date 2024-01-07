package com.notesSpringProj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.notesSpringProj.bean.Notes;
import com.notesSpringProj.bean.User;

public interface NotesRepo extends JpaRepository<Notes, Integer> {
	
	List<Notes> findByUser(User user);

	@Query(value = "SELECT * FROM notes WHERE "
			+ "to_tsvector(notes_title || ' ' || content) "
			+ "@@ websearch_to_tsquery(?1) AND user_id = ?2", nativeQuery = true)
	List<Notes> findByKeyword(String keyword, Integer userId);

}
