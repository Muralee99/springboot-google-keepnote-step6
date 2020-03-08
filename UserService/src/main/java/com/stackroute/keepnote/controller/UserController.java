package com.stackroute.keepnote.controller;

import com.stackroute.keepnote.exceptions.UserAlreadyExistsException;
import com.stackroute.keepnote.exceptions.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;
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

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized
 * format. Starting from Spring 4 and above, we can use @RestController annotation which
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class UserController {

	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
	/*
     * Autowiring should be implemented for the UserService. (Use Constructor-based
     * autowiring) Please note that we should not create an object using the new
     * keyword
     */
	@Autowired
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/*
     * Define a handler method which will create a specific user by reading the
     * Serialized object from request body and save the user details in the
     * database. This handler method should return any one of the status messages
     * basis on different situations:
     * 1. 201(CREATED) - If the user created successfully.
     * 2. 409(CONFLICT) - If the userId conflicts with any existing user
     *
     * This handler method should map to the URL "/user" using HTTP POST method
     */
	@RequestMapping(value = "/api/v1/user", method = RequestMethod.POST)
	public ResponseEntity<User> registerUser(@RequestBody final User user) {

		User newUser = null;
		try
		{
			newUser = userService.registerUser(user);
		}
		catch (UserAlreadyExistsException e)
		{
			LOG.info("User Already exists in db "+user.getUserId());
		}

		if (newUser != null)
		{
			return new ResponseEntity<User>(HttpStatus.CREATED);
		}
		else
		{
			return new ResponseEntity<User>(HttpStatus.CONFLICT);
		}
	}

	/*
     * Define a handler method which will update a specific user by reading the
     * Serialized object from request body and save the updated user details in a
     * database. This handler method should return any one of the status messages
     * basis on different situations:
     * 1. 200(OK) - If the user updated successfully.
     * 2. 404(NOT FOUND) - If the user with specified userId is not found.
     *
     * This handler method should map to the URL "/api/v1/user/{id}" using HTTP PUT method.
     */
	@RequestMapping(value = "/api/v1/user/{id}", method = RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@PathVariable final String id, @RequestBody final User user) {

		User updatedUser = null;

		try {
			updatedUser = userService.updateUser(id, user);
		}
		catch (Exception exception) {
			LOG.info("Exception occured while updating user " + user.getUserId());
		}

		if (updatedUser != null)
		{
			return new ResponseEntity<User>(HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
	}

	/*
     * Define a handler method which will delete a user from a database.
     * This handler method should return any one of the status messages basis on
     * different situations:
     * 1. 200(OK) - If the user deleted successfully from database.
     * 2. 404(NOT FOUND) - If the user with specified userId is not found.
     *
     * This handler method should map to the URL "/api/v1/user/{id}" using HTTP Delete
     * method" where "id" should be replaced by a valid userId without {}
     */
	@RequestMapping(value = "/api/v1/user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteUser(@PathVariable final String id) {
		boolean updatedUser = false;

		try
		{
			updatedUser = userService.deleteUser(id);
		}
		catch (UserNotFoundException e)
		{
			LOG.info("User not found to delete from DB " + id);
		}

		if (updatedUser)
		{
			return new ResponseEntity<User>(HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
	}

	/*
     * Define a handler method which will show details of a specific user. This
     * handler method should return any one of the status messages basis on
     * different situations:
     * 1. 200(OK) - If the user found successfully.
     * 2. 404(NOT FOUND) - If the user with specified userId is not found.
     * This handler method should map to the URL "/api/v1/user/{id}" using HTTP GET method where "id" should be
     * replaced by a valid userId without {}
     */
	@RequestMapping(value = "/api/v1/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> detailsOfUser(@PathVariable final String id) {

		User userDetails = null;

		try
		{
			userDetails = userService.getUserById(id);
		}
		catch (Exception exception) {
			LOG.info("Exception occured while getting user details form db " + id);
		}

		if (userDetails != null)
		{
			return new ResponseEntity<User>(userDetails, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
	}
}
