package com.stackroute.keepnote.service;

import com.stackroute.keepnote.exceptions.UserAlreadyExistsException;
import com.stackroute.keepnote.exceptions.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class UserServiceImpl implements UserService {

	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

	/*
     * Autowiring should be implemented for the UserRepository. (Use
     * Constructor-based autowiring) Please note that we should not create any
     * object using the new keyword.
     */
	@Autowired
	private UserRepository userRepository;

	/*
     * This method should be used to save a new user.Call the corresponding method
	 * of Respository interface.
	 */

	public User registerUser(final User user) throws UserAlreadyExistsException {

		User registeredUser = userRepository.insert(user);

		if (registeredUser != null)
		{
			return registeredUser;
		}
		else {
			throw new UserAlreadyExistsException("User Already exists in DB "+user.getUserId());
		}
	}

	/*
	 * This method should be used to update a existing user.Call the corresponding
	 * method of Respository interface.
	 */

	public User updateUser(final String userId, final User user) throws UserNotFoundException {

		Optional<User> userOptional = userRepository.findById(userId);

		userRepository.insert(user);

		Optional<User> getUserOptional = userRepository.findById(userId);

		return getUserOptional.get();
	}

	/*
	 * This method should be used to delete an existing user. Call the corresponding
	 * method of Respository interface.
	 */

	public boolean deleteUser(final String userId) throws UserNotFoundException
	{
		boolean isDeleted = false;
		User user = new User();
		user.setUserId(userId);

		try
		{
			userRepository.delete(user);
			isDeleted = true;
		}
		catch (Exception exception)
		{
			isDeleted = false;
		}

		return isDeleted;

	}

	/*
	 * This method should be used to get a user by userId.Call the corresponding
	 * method of Respository interface.
	 */

	public User getUserById(final String userId) throws UserNotFoundException {

		Optional<User> user = userRepository.findById(userId);
		return user.get();
	}

}
