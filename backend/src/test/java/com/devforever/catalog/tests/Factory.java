package com.devforever.catalog.tests;

import java.time.Instant;

import com.devforever.catalog.dto.CategoryDTO;
import com.devforever.catalog.dto.ProductDTO;
import com.devforever.catalog.entities.Category;
import com.devforever.catalog.entities.Product;

public class Factory {
	
	public static Product createProduct() {
		Product product = new Product(1L,"phone","good phone",800.0,"https://img.com/img.png",Instant.parse("2022-10-12T02:00:00Z"));
		product.getCategories().add(createCategory());
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
	
	public static Category createCategory() {
		Category category = new Category(2L,"Electronics");
		return category;
	}
	
	public static CategoryDTO createCategoryDTO() {
		Category category = createCategory();
		return new CategoryDTO(category);
	}
}
