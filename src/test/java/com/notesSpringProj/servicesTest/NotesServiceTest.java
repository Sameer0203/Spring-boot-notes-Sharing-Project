package com.notesSpringProj.servicesTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.notesSpringProj.bean.Notes;
import com.notesSpringProj.bean.User;
import com.notesSpringProj.exceptions.ResourceNotFoundException;
import com.notesSpringProj.impl.NotesServiceImpl;
import com.notesSpringProj.impl.UserServiceImpl;
import com.notesSpringProj.payloads.NotesDto;
import com.notesSpringProj.payloads.NotesResponse;
import com.notesSpringProj.payloads.UserDto;
import com.notesSpringProj.repository.NotesRepo;
import com.notesSpringProj.repository.UserRepo;

@ExtendWith(MockitoExtension.class)
public class NotesServiceTest {
	
	@Mock
	private NotesRepo notesRepo;
	
	@Mock
	private UserRepo userRepo;
	
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	private NotesServiceImpl notesServiceImpl;

	@Test
    void testUpdateNotes() {
        int notesId = 1;
        NotesDto notesDto = new NotesDto();
        notesDto.setTitle("Test Updated Note");

        Notes existingNote = new Notes();
        existingNote.setNotesId(notesId);
        existingNote.setTitle("test");

        when(notesRepo.findById(notesId)).thenReturn(Optional.of(existingNote));
        when(notesRepo.save(any(Notes.class))).thenAnswer(invocation -> invocation.getArgument(0));

        this.notesServiceImpl.updateNotes(notesDto, notesId);

        Notes value = this.notesRepo.save(existingNote);       
        assertThat(value).isEqualTo(existingNote);
    }
    
    @Test
    void testGetNotes() {
        // Arrange
        int notesId = 1;
        Notes notes = new Notes();
        when(notesRepo.findById(notesId)).thenReturn(Optional.of(notes));
        when(modelMapper.map(any(), any())).thenReturn(new NotesDto());

        // Act
        NotesDto result = this.notesServiceImpl.getNotes(notesId);

        // Assert
        assertNotNull(result);
    }
    
    @Test
    void testGetAllNotes() {
        // Arrange
        String userId = "1";
        User user = new User();
        when(userRepo.findById(Integer.valueOf(userId))).thenReturn(Optional.of(user));
        when(notesRepo.findByUser(user)).thenReturn(Collections.singletonList(new Notes()));
        when(modelMapper.map(any(), any())).thenReturn(new NotesDto());

        // Act
        NotesResponse result = this.notesServiceImpl.getAllNotess(userId);

        // Assert
        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
    }
    
    @Test
    void testSearchNotesWithEmptyKeyword() {
        // Arrange
        String userId = "1";
        when(userRepo.findById(Integer.valueOf(userId))).thenReturn(Optional.of(new User()));
        when(notesRepo.findByUser(any())).thenReturn(Collections.singletonList(new Notes()));
        when(modelMapper.map(any(), any())).thenReturn(new NotesDto());

        // Act
        List<NotesDto> result = this.notesServiceImpl.searchNotess("", userId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testSearchNotesWithKeyword() {
        // Arrange
        String userId = "1";
        String keyword = "test";
        when(userRepo.findById(Integer.valueOf(userId))).thenReturn(Optional.of(new User()));
        when(notesRepo.findByKeyword(keyword, Integer.valueOf(userId))).thenReturn(Collections.singletonList(new Notes()));
        when(modelMapper.map(any(), any())).thenReturn(new NotesDto());

        // Act
        List<NotesDto> result = this.notesServiceImpl.searchNotess(keyword, userId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testSearchNotesWithInvalidKeyword() {
        // Arrange
        String userId = "1";
        String keyword = "invalid";
        when(userRepo.findById(Integer.valueOf(userId))).thenReturn(Optional.of(new User()));
        when(notesRepo.findByKeyword(keyword, Integer.valueOf(userId))).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> this.notesServiceImpl.searchNotess(keyword, userId));
    }
    
    @Test
    void testDeleteNotes() {
        // Arrange
        int notesId = 1;
        when(notesRepo.findById(notesId)).thenReturn(Optional.of(new Notes()));

        // Act & Assert
        assertDoesNotThrow(() -> this.notesServiceImpl.deleteNotes(notesId));
    }


}
