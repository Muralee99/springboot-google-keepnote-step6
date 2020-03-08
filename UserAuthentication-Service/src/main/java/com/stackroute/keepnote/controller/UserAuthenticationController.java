package com.stackroute.keepnote.controller;


import com.stackroute.keepnote.exception.UserAlreadyExistsException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserAuthenticationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class UserAuthenticationController {

    /*
	 * Autowiring should be implemented for the UserAuthenticationService. (Use Constructor-based
	 * autowiring) Please note that we should not create an object using the new
	 * keyword
	 */
    private UserAuthenticationService userAuthenticationService;

    public UserAuthenticationController(UserAuthenticationService authicationService) {
        this.userAuthenticationService = userAuthenticationService;
	}

/*
	 * Define a handler method which will create a specific user by reading the
	 * Serialized object from request body and save the user details in the
	 * database. This handler method should return any one of the status messages
	 * basis on different situations:
	 * 1. 201(CREATED) - If the user created successfully. 
	 * 2. 409(CONFLICT) - If the userId conflicts with any existing user
	 * 
	 * This handler method should map to the URL "/api/v1/auth/register" using HTTP POST method
	 */
    @RequestMapping(value = "/api/v1/auth/register", method = RequestMethod.POST)
    public ResponseEntity<User> registerUser(@RequestBody final User user) {

        boolean savedUser = true;

        try
        {
            userAuthenticationService.saveUser(user);
        }
        catch (UserAlreadyExistsException e)
        {
            savedUser = false;
        }

        if (savedUser)
        {
            return new ResponseEntity<User>(HttpStatus.CREATED);
        }
        else
        {
            return new ResponseEntity<User>(HttpStatus.CONFLICT);
        }
    }

	/* Define a handler method which will authenticate a user by reading the Serialized user
	 * object from request body containing the username and password. The username and password should be validated 
	 * before proceeding ahead with JWT token generation. The user credentials will be validated against the database entries. 
	 * The error should be return if validation is not successful. If credentials are validated successfully, then JWT
	 * token will be generated. The token should be returned back to the caller along with the API response.
	 * This handler method should return any one of the status messages basis on different
	 * situations:
	 * 1. 200(OK) - If login is successful
	 * 2. 401(UNAUTHORIZED) - If login is not successful
	 * 
	 * This handler method should map to the URL "/api/v1/auth/login" using HTTP POST method
	*/
    @RequestMapping(value = "/api/v1/auth/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> loginUser(@RequestBody final User user) {

        boolean loginStatus = true;
        String token = "";

        try {
            token = getToken(user.getUserId(), user.getUserPassword());
            new LoginResponse(token);
        }
        catch (UserAlreadyExistsException e)
        {
            loginStatus = false;
        }
        catch (Exception exception)
        {
            loginStatus = false;
        }

        if (loginStatus)
        {
            return new ResponseEntity<LoginResponse>(HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<LoginResponse>(HttpStatus.CONFLICT);
        }
    }

    @SuppressWarnings("unused")
    private static class LoginResponse {
        public String token;

        public LoginResponse(final String token) {
            this.token = token;
        }
    }



// Generate JWT token
	public String getToken(final String username, final String password) throws Exception
    {
        User user = null;
        if (username != null && password != null)
        {
            try
            {
                user = userAuthenticationService.findByUserIdAndPassword(username, password);
            }
            catch (UserNotFoundException userNotFoundException)
            {
                throw new UserNotFoundException("Invalid User Login");
            }
        }
        else
        {
            throw new UserNotFoundException("Invalid User Login");
        }

        return  (Jwts.builder().setSubject(username)
                .claim(user.getUserRole(), user.getUserId()).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact());

    }


}




