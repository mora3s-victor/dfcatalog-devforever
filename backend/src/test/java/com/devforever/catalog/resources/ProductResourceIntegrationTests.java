package com.devforever.catalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.devforever.catalog.dto.ProductDTO;
import com.devforever.catalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIntegrationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private ProductDTO productDTO;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		countTotalProducts = 25L;
		productDTO = Factory.createProductDTO();
	}
	
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		String jsonBody = objectMapper.writeValueAsString(productDTO);		
		
		ResultActions result = 
				mockMvc.perform(put("/products/{id}",nonExistingId)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception{
		String jsonBody = objectMapper.writeValueAsString(productDTO);		
		String expectedName = productDTO.getName();
		String description = productDTO.getDescription();
		
		ResultActions result = 
				mockMvc.perform(put("/products/{id}",existingId)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.description").value(description));		
	}

	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortByName() throws Exception{
		ResultActions result = 
				mockMvc.perform(get("/products?page=0&size=3&sort=name,asc")
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
		result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
		
	}
		
}





