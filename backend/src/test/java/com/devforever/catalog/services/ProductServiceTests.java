package com.devforever.catalog.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devforever.catalog.dto.ProductDTO;
import com.devforever.catalog.entities.Category;
import com.devforever.catalog.entities.Product;
import com.devforever.catalog.repositories.CategoryRepository;
import com.devforever.catalog.repositories.ProductRepository;
import com.devforever.catalog.services.exceptions.DatabaseException;
import com.devforever.catalog.services.exceptions.ResourceNotFoundException;
import com.devforever.catalog.tests.Factory;

//USA-SE QUANDO NÃO É NECESSÁRIO CARREGAR O CONTEXTO DA APLICAÇÃO
@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	// USA-SE QUANDO NÃO É NECESSÁRIO CARREGAR O CONTEXTO DA APLICAÇÃO.
	// DIFERENTEMENTE DO @MockBean
	@Mock
	private ProductRepository repository;
	@Mock
	private CategoryRepository categoryRepository;
	// TIPO CONCRETO QUE REPRESENTA UMA PÁGINA DE DADOS
	private PageImpl<Product> page;
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private Product product;
	private ProductDTO productDTO;
	private Category category;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		product = Factory.createProduct();
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(product));
		category = Factory.createCategory();

		Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.getOne(existingId)).thenReturn(product);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		ProductDTO updated = service.update(existingId, productDTO);
		Assertions.assertNotNull(updated);
		Mockito.verify(repository, Mockito.times(1)).getOne(existingId);
		Mockito.verify(repository, Mockito.times(1)).save(product);
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, productDTO);
		});
		Mockito.verify(repository, Mockito.times(1)).getOne(nonExistingId);
		Mockito.verify(repository, Mockito.times(0)).save(product);		
	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO dto = service.findById(existingId);
		Assertions.assertNotNull(dto);
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);

	}

	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<ProductDTO> result = service.findAllPaged(pageable);

		Assertions.assertNotNull(result);

		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	}

	@Test
	public void deleteShouldThrowEmptyResultDataAccesExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}

	@Test
	public void deleteShouldDoNothinWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}

}
