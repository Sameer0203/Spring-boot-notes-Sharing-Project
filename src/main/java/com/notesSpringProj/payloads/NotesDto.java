package com.notesSpringProj.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.notesSpringProj.bean.User;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotesDto {

	private Integer notesId;
	private Integer userId;	
	@NotEmpty
	@Size(min = 4, message = "Title must be greater than 4 characters")
	private String title;
	@NotEmpty
	@Size(min = 10, message = "Notes Content must be greater than 10 characters")
	private String content;
	private Date addedDate;

}
