package com.stackroute.keepnote.controller;

import com.stackroute.keepnote.exception.ReminderNotCreatedException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.service.ReminderService;
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
public class ReminderController
{
	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

	/*
     * From the problem statement, we can understand that the application requires
	 * us to implement five functionalities regarding reminder. They are as
	 * following:
	 *
	 * 1. Create a reminder
	 * 2. Delete a reminder
	 * 3. Update a reminder
	 * 4. Get all reminders by userId
	 * 5. Get a specific reminder by id.
	 *
	 */

	/*
     * Autowiring should be implemented for the ReminderService. (Use
     * Constructor-based autowiring) Please note that we should not create any
     * object using the new keyword
     */
	@Autowired
	private ReminderService reminderService;

	public ReminderController(ReminderService reminderService) {
		this.reminderService = reminderService;
	}

	/*
     * Define a handler method which will create a reminder by reading the
     * Serialized reminder object from request body and save the reminder in
     * database. Please note that the reminderId has to be unique. This handler
     * method should return any one of the status messages basis on different
     * situations:
     * 1. 201(CREATED - In case of successful creation of the reminder
     * 2. 409(CONFLICT) - In case of duplicate reminder ID
     *
     * This handler method should map to the URL "/api/v1/reminder" using HTTP POST
     * method".
     */
	@RequestMapping(value = "/api/v1/reminder", method = RequestMethod.POST)
	public ResponseEntity<Reminder> createReminder(@RequestBody final Reminder reminder) {
		Reminder newReminder = null;

		try {
			newReminder = reminderService.createReminder(reminder);
		}
		catch (ReminderNotCreatedException e)
		{
			LOG.info("Requested reminder not created "+reminder.getReminderId());
		}

		if (newReminder != null)
		{
			return new ResponseEntity<Reminder>(HttpStatus.CREATED);
		}
		else
		{
			return new ResponseEntity<Reminder>(HttpStatus.CONFLICT);
		}
	}

	/*
     * Define a handler method which will delete a reminder from a database.
     *
     * This handler method should return any one of the status messages basis on
     * different situations:
     * 1. 200(OK) - If the reminder deleted successfully from database.
     * 2. 404(NOT FOUND) - If the reminder with specified reminderId is not found.
     *
     * This handler method should map to the URL "/api/v1/reminder/{id}" using HTTP Delete
     * method" where "id" should be replaced by a valid reminderId without {}
     */
	@RequestMapping(value = "/api/v1/reminder/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Reminder> deleteReminder(@PathVariable final String id)
	{
		boolean isDeleted = false;

		try
		{
			isDeleted = reminderService.deleteReminder(id);
		}
		catch (ReminderNotFoundException e)
		{
			LOG.info("Requested reminder not deleted " + id);
		}

		if (isDeleted)
		{
			return new ResponseEntity<Reminder>(HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Reminder>(HttpStatus.NOT_FOUND);
		}
	}

	/*
     * Define a handler method which will update a specific reminder by reading the
     * Serialized object from request body and save the updated reminder details in
     * a database. This handler method should return any one of the status messages
     * basis on different situations:
     * 1. 200(OK) - If the reminder updated successfully.
     * 2. 404(NOT FOUND) - If the reminder with specified reminderId is not found.
     *
     * This handler method should map to the URL "/api/v1/reminder/{id}" using HTTP PUT
     * method.
     */
	@RequestMapping(value = "/api/v1/reminder/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Reminder> updateReminder(@PathVariable final String id, @RequestBody final Reminder reminder) {

		Reminder reminderUpdated = null;

		try
		{
			reminderUpdated = reminderService.updateReminder(reminder, id);
		}
		catch (ReminderNotFoundException e)
		{
			LOG.info("Requested reminder not updated " + reminder.getReminderId());
		}


		if (reminderUpdated != null)
		{
			return new ResponseEntity<Reminder>(reminder, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Reminder>(reminder, HttpStatus.NOT_FOUND);
		}
	}

	/*
     * Define a handler method which will show details of a specific reminder. This
     * handler method should return any one of the status messages basis on
     * different situations:
     * 1. 200(OK) - If the reminder found successfully.
     * 2. 404(NOT FOUND) - If the reminder with specified reminderId is not found.
     *
     * This handler method should map to the URL "/api/v1/reminder/{id}" using HTTP GET method
     * where "id" should be replaced by a valid reminderId without {}
     */
	@RequestMapping(value = "/api/v1/reminder", method = RequestMethod.GET)
	public ResponseEntity<List<Reminder>> getListofReminerByUserId()
	{

		List<Reminder> reminders = null;

		reminders = reminderService.getAllReminders();

		if (reminders.size() > 0) {
			return new ResponseEntity<List<Reminder>>(reminders, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<List<Reminder>>(reminders, HttpStatus.NOT_FOUND);
		}
	}

	/*
     * Define a handler method which will get us the all reminders.
     * This handler method should return any one of the status messages basis on
     * different situations:
     * 1. 200(OK) - If the reminder found successfully.
     * 2. 404(NOT FOUND) - If the reminder with specified reminderId is not found.
     *
     * This handler method should map to the URL "/api/v1/reminder" using HTTP GET method
     */
	@RequestMapping(value = "/api/v1/reminder/{id}", method = RequestMethod.GET)
	public ResponseEntity<Reminder> getReminderByUserId(@PathVariable final String id) {

		Reminder reminder = null;
		try
		{
			reminder = reminderService.getReminderById(id);
		}
		catch (ReminderNotFoundException e) {
			LOG.info("Requested not found for given user id " + id);
		}

		if (reminder != null)
		{
			return new ResponseEntity<Reminder>(reminder, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Reminder>(reminder, HttpStatus.NOT_FOUND);
		}
	}
}
