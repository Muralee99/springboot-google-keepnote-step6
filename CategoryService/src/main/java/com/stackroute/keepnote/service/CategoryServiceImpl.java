package com.stackroute.keepnote.service;

import com.stackroute.keepnote.exception.CategoryDoesNoteExistsException;
import com.stackroute.keepnote.exception.CategoryNotCreatedException;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.repository.CategoryRepository;
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
public class CategoryServiceImpl implements CategoryService {

	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

	/*
     * Autowiring should be implemented for the CategoryRepository. (Use
     * Constructor-based autowiring) Please note that we should not create any
     * object using the new keyword.
     */
	@Autowired
	private CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	/*
     * This method should be used to save a new category.Call the corresponding
     * method of Respository interface.
     */
	public Category createCategory(final Category category) throws CategoryNotCreatedException {

		Category newCategory = categoryRepository.insert(category);

		if (newCategory != null)
		{
			return newCategory;
		}
		else
		{
			LOG.info("Requested Category not created " + category.getId());
			throw new CategoryNotCreatedException("Requested Category not created");
		}
	}

	/*
     * This method should be used to delete an existing category.Call the
     * corresponding method of Respository interface.
     */
	public boolean deleteCategory(final String categoryId) throws CategoryDoesNoteExistsException {

		boolean isDeleted = false;

		categoryRepository.deleteById(categoryId);

		Optional<Category> category = categoryRepository.findById(categoryId);

		if(category.get() != null)
		{
			isDeleted = true;
		}

		return isDeleted;
	}

	/*
     * This method should be used to update a existing category.Call the
     * corresponding method of Respository interface.
     */
	public Category updateCategory(final Category category, final String categoryId) {

		Optional<Category> options = categoryRepository.findById(category.getId());

		if(options.get() != null)
		{
			categoryRepository.insert(category);

			Optional<Category> cat =  categoryRepository.findById(categoryId);

			return cat.get();
		}
		else
		{
			return null;
		}
	}

	/*
     * This method should be used to get a category by categoryId.Call the
     * corresponding method of Respository interface.
     */
	public Category getCategoryById(final String categoryId) throws CategoryNotFoundException
	{
		Category category = new Category();
		category.setId(categoryId);

		Optional<Category> cat = null;

		try
		{
			cat = categoryRepository.findById(categoryId);
		}
		catch(NoSuchElementException exception)
		{
			throw new CategoryNotFoundException("Requested Category Not found "+categoryId);
		}

		if (cat.get() != null)
		{
			return cat.get();
		}
		else
		{
			throw new CategoryNotFoundException("Requested Category Not found "+categoryId);
		}
	}

	/*
     * This method should be used to get a category by userId.Call the corresponding
     * method of Respository interface.
     */
	public List<Category> getAllCategoryByUserId(final String userId) {

		return categoryRepository.findAllCategoryByCategoryCreatedBy(userId);
	}

}
