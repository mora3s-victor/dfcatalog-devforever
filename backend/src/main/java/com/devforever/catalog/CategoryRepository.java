package com.devforever.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devforever.catalog.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{

}
