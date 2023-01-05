package com.devforever.catalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.devforever.catalog.dto.ProductDTO;
import com.devforever.catalog.repositories.ProductRepository;
import com.devforever.catalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional//NOS TESTES TRANSACIONAIS, APÓS CADA TESTE É FEITO UM ROLLBACK NO BANCO, DEIXANDO CADA TESTE INDEPENDENTE UM DO OUTRO
public class ProductServiceIntegrationTests {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;
		
	
	@BeforeEach
	public void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;		
	}
	
	@Test
	public void findAllPagedShoudReturnSortedPageWhenSortByName() {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro",result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer",result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa",result.getContent().get(2).getName());

	}
	
	@Test
	public void findAllPagedShoudReturnEmptyPageWhenPageDoesNotExist() {
		PageRequest page = PageRequest.of(50, 10);
		
		Page<ProductDTO> result = service.findAllPaged(page);
		
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findAllPagedShoudReturnPageWhenPage0Size10() {
		PageRequest page = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(page);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0,result.getNumber());//NUMERO DA PAGINA
		Assertions.assertEquals(10,result.getSize());//NUMERO DE ELEMENTOS NA PÁGINA
		Assertions.assertEquals(countTotalProducts,result.getTotalElements());//TOTAL DE ELEMENTOS NO BD
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		service.delete(existingId);
		Assertions.assertEquals(countTotalProducts - 1, repository.count());
	}
	
}
