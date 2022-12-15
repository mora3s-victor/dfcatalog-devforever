package com.devforever.catalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devforever.catalog.CategoryRepository;
import com.devforever.catalog.entities.Category;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	
	@Transactional(readOnly = true)
	public List<Category> findAll() {
		return repository.findAll();
	}
}
