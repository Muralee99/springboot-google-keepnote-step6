package com.stackroute.keepnote.service;

import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.NoteUser;
import com.stackroute.keepnote.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/*
* Service classes are used here to implement additional business logic/validation
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently
* provide any additional behavior over the @Component annotation, but it's a good idea
* to use @Service over @Component in service-layer classes because it specifies intent
* better. Additionally, tool support and additional behavior might rely on it in the
* future.
* */

@Service
public class NoteServiceImpl implements NoteService {

	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

	/*
     * Autowiring should be implemented for the NoteRepository and MongoOperation.
     * (Use Constructor-based autowiring) Please note that we should not create any
     * object using the new keyword.
     */
	@Autowired
	private NoteRepository noteRepository;

	public NoteServiceImpl(NoteRepository noteRepository) {
		this.noteRepository = noteRepository;
	}

	/*
         * This method should be used to save a new note.
         */
	public boolean createNote(final Note note)
	{
		NoteUser newNote = noteRepository.insert(note);

		if (newNote == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/* This method should be used to delete an existing note. */


	public boolean deleteNote(final String userId, final int noteId)
	{
		Note note = new Note();

		note.setNoteId(noteId);

		Optional<NoteUser> notes = noteRepository.findById(userId);

		//noteRepository.save(notes.get().getNotes().get(0));

		if(notes.isPresent())
		{
			noteRepository.delete(note);
			return true;
		}

		return false;
	}

	/* This method should be used to delete all notes with specific userId. */


	public boolean deleteAllNotes(final String userId)
	{
		noteRepository.deleteAll();

		if(noteRepository.findAll().isEmpty())
		{
			return true;
		}
		return false;
	}

	/*
     * This method should be used to update a existing note.
     */
	public Note updateNote(final Note note, final int id, final String userId) throws NoteNotFoundExeption {

		Optional<NoteUser> notes = null;

		try
		{
			notes = noteRepository.findById(userId);
		}
		catch (NoSuchElementException exception)
		{
			throw new NoteNotFoundExeption("Entered Note not found to update");
		}

		if (notes.get().getNotes().size() > 0)
		{
			noteRepository.insert(note);
			return note;
		}
		else {
			throw new NoteNotFoundExeption("Entered Note not found to update");
		}
	}

	/*
     * This method should be used to get a note by noteId created by specific user
     */
	public Note getNoteByNoteId(final String userId, final int noteId) throws NoteNotFoundExeption {

		Optional<NoteUser> optional = null;

		try
		{
			optional = noteRepository.findById(userId);
		}
		catch (NoSuchElementException exception)
		{
			throw new NoteNotFoundExeption("Entered Note not found to update");
		}

		return optional.get().getNotes().get(0);
	}

	/*
     * This method should be used to get all notes with specific userId.
     */
	public List<Note> getAllNoteByUserId(final String userId)
	{
		Optional<NoteUser> optional = noteRepository.findById(userId);
		return optional.get().getNotes();

	}

}
