package com.stackroute.keepnote.controller;

import com.stackroute.keepnote.exception.CategoryDoesNoteExistsException;
import com.stackroute.keepnote.exception.CategoryNotCreatedException;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.service.CategoryService;
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
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized
 * format. Starting from Spring 4 and above, we can use @RestController annotation which
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class CategoryController {

	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

	/*
     * Autowiring should be implemented for the CategoryService. (Use
     * Constructor-based autowiring) Please note that we should not create any
     * object using the new keyword
     */
	@Autowired
	private CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	/*
     * Define a handler method which will create a category by reading the
     * Serialized category object from request body and save the category in
     * database. Please note that the careatorId has to be unique.This
     * handler method should return any one of the status messages basis on
     * different situations:
     * 1. 201(CREATED - In case of successful creation of the category
     * 2. 409(CONFLICT) - In case of duplicate categoryId
     *
     *
     * This handler method should map to the URL "/api/v1/category" using HTTP POST
     * method".
     */
	@RequestMapping(value = "/api/v1/category", method = RequestMethod.POST)
	public ResponseEntity<Category> createCategory(@RequestBody final Category category)
	{

		Category createCategory = null;

		try
		{
			createCategory = categoryService.createCategory(category);
		}
		catch (CategoryNotCreatedException e)
		{
			LOG.info("Requested Category not created "+category.getId());
		}

		if (createCategory != null)
		{
			return new ResponseEntity<Category>(HttpStatus.CREATED);
		}
		else
		{
			return new ResponseEntity<Category>(HttpStatus.CONFLICT);
		}
	}

	/*
     * Define a handler method which will delete a category from a database.
     *
     * This handler method should return any one of the status messages basis on
     * different situations: 1. 200(OK) - If the category deleted successfully from
     * database. 2. 404(NOT FOUND) - If the category with specified categoryId is
     * not found.
     *
     * This handler method should map to the URL "/api/v1/category/{id}" using HTTP Delete
     * method" where "id" should be replaced by a valid categoryId without {}
     */
	@RequestMapping(value = "/api/v1/category/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Category> deleteCategory(@PathVariable final String id)
	{
		boolean isDeleted = false;

		try
		{
			isDeleted = categoryService.deleteCategory(id);
		}
		catch (CategoryDoesNoteExistsException e) {
			LOG.info("Requested Category not exists in data base " + id);
		}

		if (isDeleted)
		{
			return new ResponseEntity<Category>(HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
		}
	}

	/*
     * Define a handler method which will update a specific category by reading the
     * Serialized object from request body and save the updated category details in
     * database. This handler method should return any one of the status
     * messages basis on different situations: 1. 200(OK) - If the category updated
     * successfully. 2. 404(NOT FOUND) - If the category with specified categoryId
     * is not found.
     * This handler method should map to the URL "/api/v1/category/{id}" using HTTP PUT
     * method.
     */
	@RequestMapping(value = "/api/v1/category/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Category> updateCategory(@PathVariable final String id, @RequestBody final Category category)
	{
		Category categoryUpdated = null;

		categoryUpdated = categoryService.updateCategory(category, id);

		if (categoryUpdated != null)
		{
			return new ResponseEntity<Category>(category, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Category>(category, HttpStatus.CONFLICT);
		}
	}

	/*
     * Define a handler method which will get us the category by a userId.
     *
     * This handler method should return any one of the status messages basis on
     * different situations: 1. 200(OK) - If the category found successfully.
     *
     *
     * This handler method should map to the URL "/api/v1/category" using HTTP GET method
     */
	@RequestMapping(value = "/api/v1/category/{id}", method = RequestMethod.GET)
	public ResponseEntity<Category> getCategoryByUserId(@PathVariable final String id)
	{
		Category category = null;
		try
		{
			category = categoryService.getCategoryById(id);
		}
		catch (CategoryNotFoundException e)
		{
			LOG.info("Category not exists in data base for give user " + id);
		}

		if (category != null)
		{
			return new ResponseEntity<Category>(category, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Category>(category, HttpStatus.NOT_FOUND);
		}
	}

}
