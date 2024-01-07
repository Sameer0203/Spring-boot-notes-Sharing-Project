package com.notesSpringProj.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.notesSpringProj.bean.Notes;
import com.notesSpringProj.bean.User;
import com.notesSpringProj.exceptions.ResourceNotFoundException;
import com.notesSpringProj.payloads.NotesDto;
import com.notesSpringProj.payloads.NotesResponse;
import com.notesSpringProj.repository.NotesRepo;
import com.notesSpringProj.repository.UserRepo;
import com.notesSpringProj.service.NotesService;

@Service
public class NotesServiceImpl implements NotesService {

    // Autowired to inject instances of NotesRepo, ModelMapper, and UserRepo
    @Autowired
    NotesRepo notesRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepo userRepo;

    // Method to retrieve notes by ID
    @Override
    public NotesDto getNotes(Integer notesId) {
        // Find notes by ID or throw ResourceNotFoundException
        Notes notes = this.notesRepo.findById(notesId)
                .orElseThrow(() -> new ResourceNotFoundException("Notes", " notesId ", notesId));
        return this.modelMapper.map(notes, NotesDto.class);
    }

    // Method to retrieve all notes for a given user
    @Override
    public NotesResponse getAllNotess(String userId) {

        // Find the user by ID or throw an exception
        User user = this.userRepo.findById(Integer.valueOf(userId)).orElseThrow();

        // Retrieve notes for the user
        List<Notes> notes = this.notesRepo.findByUser(user);

        // Convert the list of notes to a list of NotesDto using ModelMapper
        List<NotesDto> notessDto = new ArrayList<>();
        for (int i = 0; i < notes.size(); i++) {
            notessDto.add(this.modelMapper.map(notes.get(i), NotesDto.class));
        }

        // Create a NotesResponse object and set its content
        NotesResponse notesResponse = new NotesResponse();
        notesResponse.setContent(notessDto);

        return notesResponse;
    }

    // Method to retrieve notes by user ID
    @Override
    public List<NotesDto> getNotesByUser(Integer userId) {
        // Find the user by ID or throw ResourceNotFoundException
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " userId ", userId));

        // Retrieve notes for the user
        List<Notes> notes = this.notesRepo.findByUser(user);

        // Convert the list of notes to a list of NotesDto using ModelMapper and streams
        List<NotesDto> notessDto = notes.stream().map((note) -> this.modelMapper.map(note, NotesDto.class))
                .collect(Collectors.toList());

        return notessDto;
    }

    // Method to create notes
    @Override
    public NotesDto createNotes(NotesDto notesDto) {
        // Get the user ID from the notesDto
        Integer userId = notesDto.getUserId();
        System.out.println(userId);

        // Find the user by ID or throw ResourceNotFoundException
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " userId ", userId));

        // Map the notesDto to Notes and set user and addedDate
        Notes notes = this.modelMapper.map(notesDto, Notes.class);
        notes.setAddedDate(new Date());
        notes.setUser(user);

        // Save the notes and map the result to NotesDto
        Notes createdNotes = this.notesRepo.save(notes);
        NotesDto createdNotesDto = this.modelMapper.map(createdNotes, NotesDto.class);
        createdNotesDto.setUserId(userId);

        return createdNotesDto;
    }

    // Method to update notes
    @Override
    public NotesDto updateNotes(NotesDto notesDto, Integer notesId) {
        // Find notes by ID or throw ResourceNotFoundException
        Notes notes = this.notesRepo.findById(notesId)
                .orElseThrow(() -> new ResourceNotFoundException("Notes", " notesId ", notesId));

        // Update notes with data from notesDto
        notes.setTitle(notesDto.getTitle());
        notes.setContent(notesDto.getContent());

        // Save the updated notes and map the result to NotesDto
        Notes updatedNotes = this.notesRepo.save(notes);
        return this.modelMapper.map(updatedNotes, NotesDto.class);
    }

    // Method to delete notes by ID
    @Override
    public void deleteNotes(Integer notesId) {
        // Find notes by ID or throw ResourceNotFoundException
        Notes notes = this.notesRepo.findById(notesId)
                .orElseThrow(() -> new ResourceNotFoundException("Notes", " notesId ", notesId));

        // Delete the notes
        this.notesRepo.delete(notes);
    }

    // Method to search notes by keyword for a given user
    @Override
    public List<NotesDto> searchNotess(String keyword, String userId) {
        // Find the user by ID or throw an exception
        User user = this.userRepo.findById(Integer.valueOf(userId)).orElseThrow();

        // Initialize a list to store notes
        List<Notes> notes = new ArrayList<>();

        // Check if the keyword is empty, if yes, retrieve all notes for the user
        if (keyword.equals(""))
            notes = this.notesRepo.findByUser(user);
        else
            // Otherwise, search notes by keyword and user ID
            notes = this.notesRepo.findByKeyword(keyword, Integer.valueOf(userId));

        // If no notes are found, throw a ResourceNotFoundException
        if (notes.size() == 0)
            throw new ResourceNotFoundException("notes", "keyword : " + keyword, -1);

        // Convert the list of notes to a list of NotesDto using ModelMapper and streams
        List<NotesDto> notesDtos = notes.stream().map((note) -> this.modelMapper.map(note, NotesDto.class))
                .collect(Collectors.toList());

        return notesDtos;
    }

    // Method to share notes with another user
    @Override
    public NotesDto shareNotes(NotesDto notesDto, Integer userId) {
        // Find the user by ID or throw ResourceNotFoundException
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        // Map the notesDto to Notes and set user and addedDate
        Notes notes = this.modelMapper.map(notesDto, Notes.class);
        notes.setAddedDate(new Date());
        notes.setUser(user);

        // Save the shared notes and map the result to NotesDto
        Notes savedNotes = this.notesRepo.save(notes);
        NotesDto savedNotesDto = this.modelMapper.map(savedNotes, NotesDto.class);
        savedNotesDto.setUserId(userId);

        return savedNotesDto;
    }

}
