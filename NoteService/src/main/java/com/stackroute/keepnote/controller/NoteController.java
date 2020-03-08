package com.stackroute.keepnote.controller;

import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized
 * format. Starting from Spring 4 and above, we can use @RestController annotation which
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class NoteController {

	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

	/*
     * Autowiring should be implemented for the NoteService. (Use Constructor-based
     * autowiring) Please note that we should not create any object using the new
     * keyword
     */
	@Autowired
	private NoteService noteService;


	public NoteController(NoteService noteService) {
		this.noteService = noteService;
	}

	/*
     * Define a handler method which will create a specific note by reading the
     * Serialized object from request body and save the note details in the
     * database.This handler method should return any one of the status messages
     * basis on different situations:
     * 1. 201(CREATED) - If the note created successfully.
     * 2. 409(CONFLICT) - If the noteId conflicts with any existing user.
     *
     * This handler method should map to the URL "/api/v1/note" using HTTP POST method
     */
	@RequestMapping(value = "/api/v1/note", method = RequestMethod.POST)
	public ResponseEntity<Note> createNote(@RequestBody final Note note)
	{
		boolean noteCreated = false;

		noteCreated = noteService.createNote(note);

		if (noteCreated)
		{
			return new ResponseEntity<Note>(HttpStatus.CREATED);
		}
		else
		{
			return new ResponseEntity<Note>(HttpStatus.CONFLICT);
		}
	}

	/*
     * Define a handler method which will delete a note from a database.
     * This handler method should return any one of the status messages basis
     * on different situations:
     * 1. 200(OK) - If the note deleted successfully from database.
     * 2. 404(NOT FOUND) - If the note with specified noteId is not found.
     *
     * This handler method should map to the URL "/api/v1/note/{id}" using HTTP Delete
     * method" where "id" should be replaced by a valid noteId without {}
     */
	@RequestMapping(value = "/api/v1/note/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Note> deleteAllNotes(@PathVariable final String id) {
		boolean isDeleted = false;

		try
		{
			isDeleted = noteService.deleteAllNotes(id);
		}
		catch (NoteNotFoundExeption noteNotFoundExeption)
		{
			isDeleted = false;
		}

		if (isDeleted)
		{
			return new ResponseEntity<Note>(HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Note>(HttpStatus.NOT_FOUND);
		}
	}

	/*
     * Define a handler method which will delete a note from a database.
     * This handler method should return any one of the status messages basis
     * on different situations:
     * 1. 200(OK) - If the note deleted successfully from database.
     * 2. 404(NOT FOUND) - If the note with specified noteId is not found.
     *
     * This handler method should map to the URL "/api/v1/note/{id}" using HTTP Delete
     * method" where "id" should be replaced by a valid noteId without {}
     */
	@RequestMapping(value = "/api/v1/note/{userId}/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Note> deleteNotes(@PathVariable final String userId, @PathVariable final int id)
	{
		boolean isDeleted = false;

		isDeleted = noteService.deleteNote(userId, id);

		if (isDeleted)
		{
			return new ResponseEntity<Note>(HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Note>(HttpStatus.NOT_FOUND);
		}
	}

	/*
     * Define a handler method which will update a specific note by reading the
     * Serialized object from request body and save the updated note details in a
     * database.
     * This handler method should return any one of the status messages
     * basis on different situations:
     * 1. 200(OK) - If the note updated successfully.
     * 2. 404(NOT FOUND) - If the note with specified noteId is not found.
     *
     * This handler method should map to the URL "/api/v1/note/{id}" using HTTP PUT method.
     */
	@RequestMapping(value = "/api/v1/note/{userId}/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Note> updateNote(@PathVariable final String userId, @PathVariable final int id, @RequestBody final Note note) {

		Note noteUpdated = null;

		try
		{
			noteUpdated = noteService.updateNote(note, id, userId);
		}
		catch (NoteNotFoundExeption noteNotFoundExeption)
		{
			LOG.info("Note not found to update");
		}

		if (noteUpdated != null)
		{
			return new ResponseEntity<Note>(note, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Note>(note, HttpStatus.NOT_FOUND);
		}
	}

	/*
     * Define a handler method which will get us the all notes by a userId.
     * This handler method should return any one of the status messages basis on
     * different situations:
     * 1. 200(OK) - If the note found successfully.
     *
     * This handler method should map to the URL "/api/v1/note" using HTTP GET method
     */
	@RequestMapping(value = "/api/v1/note/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<Note>> getListofNoteByUserId(@PathVariable final String userId) {

		List<Note> notes = null;

		notes = noteService.getAllNoteByUserId(userId);

		if (notes == null)
		{
			return new ResponseEntity<List<Note>>(notes, HttpStatus.OK);
		}

		if (notes != null && notes.size() > 0)
		{
			return new ResponseEntity<List<Note>>(notes, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<List<Note>>(notes, HttpStatus.NOT_FOUND);
		}
	}

	/*
     * Define a handler method which will show details of a specific note created by specific
     * user. This handler method should return any one of the status messages basis on
     * different situations:
     * 1. 200(OK) - If the note found successfully.
     * 2. 404(NOT FOUND) - If the note with specified noteId is not found.
     * This handler method should map to the URL "/api/v1/note/{userId}/{noteId}" using HTTP GET method
     * where "id" should be replaced by a valid reminderId without {}
     *
     */
	@RequestMapping(value = "/api/v1/note/{userId}/{noteId}", method = RequestMethod.GET)
	public ResponseEntity<Note> getNoteByUserId(@PathVariable final String userId, @PathVariable final int noteId) {

		Note note = null;
		try
		{
			note = noteService.getNoteByNoteId(userId, noteId);
		}
		catch (NoteNotFoundExeption noteNotFoundExeption)
		{
			LOG.info("Note not found for the given user id");
		}
		if (note != null)
		{
			return new ResponseEntity<Note>(note, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Note>(note, HttpStatus.NOT_FOUND);
		}

	}


}
