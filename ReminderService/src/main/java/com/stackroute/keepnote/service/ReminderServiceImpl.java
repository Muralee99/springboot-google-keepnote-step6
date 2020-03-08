package com.stackroute.keepnote.service;

import com.stackroute.keepnote.exception.ReminderNotCreatedException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.repository.ReminderRepository;
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
public class ReminderServiceImpl implements ReminderService
{
	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
	/*
     * Autowiring should be implemented for the ReminderRepository. (Use
     * Constructor-based autowiring) Please note that we should not create any
     * object using the new keyword.
     */
	@Autowired
	private ReminderRepository reminderRepository;

	/*
     * This method should be used to save a new reminder.Call the corresponding
     * method of Respository interface.
     */
	public Reminder createReminder(final Reminder reminder) throws ReminderNotCreatedException {

		Reminder reminder1 = reminderRepository.insert(reminder);

		if (reminder1 == null)
		{
			throw new ReminderNotCreatedException("Reminder Not found "+reminder.getReminderId());
		} else {
			return reminder1;
		}
	}

	/*
     * This method should be used to delete an existing reminder.Call the
     * corresponding method of Respository interface.
     */
	public boolean deleteReminder(final String reminderId) throws ReminderNotFoundException {

		boolean reminderDeleted = true;
		Reminder reminder = new Reminder();
		reminder.setReminderId(reminderId);

		try {
			reminderRepository.delete(reminder);
		}
		catch (NoSuchElementException e)
		{
			reminderDeleted = false;
			LOG.info("Requested reminder not deleted " + reminderId);
		}

		return reminderDeleted;
	}

	/*
     * This method should be used to update a existing reminder.Call the
     * corresponding method of Respository interface.
     */
	public Reminder updateReminder(final Reminder reminder, final String reminderId) throws ReminderNotFoundException {

		Optional<Reminder> options = reminderRepository.findById(reminderId);

		reminderRepository.insert(options.get());

		Optional<Reminder> reminderOptional = reminderRepository.findById(reminderId);

		return reminderOptional.get();

	}

	/*
     * This method should be used to get a reminder by reminderId.Call the
     * corresponding method of Respository interface.
     */
	public Reminder getReminderById(final String reminderId) throws ReminderNotFoundException {

		Optional<Reminder> reminders = reminderRepository.findById(reminderId);

		return reminders.get();
	}

	/*
     * This method should be used to get all reminders. Call the corresponding
	 * method of Respository interface.
	 */

	public List<Reminder> getAllReminders() {

		return reminderRepository.findAll();
	}

}
