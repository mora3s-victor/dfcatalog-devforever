package com.devforever.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devforever.catalog.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	
	User findByEmail(String email);
}
