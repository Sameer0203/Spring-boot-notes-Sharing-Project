package com.notesSpringProj.service;

import java.util.List;

import com.notesSpringProj.payloads.NotesDto;
import com.notesSpringProj.payloads.NotesResponse;

public interface NotesService {
	
	NotesDto getNotes(Integer notesId);
	NotesResponse getAllNotess(String clientId);
	List<NotesDto> getNotesByUser(Integer userId);
	List<NotesDto> searchNotess(String keyword, String clientId);
	
	NotesDto createNotes(NotesDto notesDto);
	NotesDto updateNotes(NotesDto notesDto, Integer notesId);
	void deleteNotes(Integer notesId);
	NotesDto shareNotes(NotesDto notesDto, Integer userId);

}
