package com.example.demo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

	@Repository
	public interface UserRepository extends CrudRepository<User, Long>
	{
		
//		@Query("from Employee where name=?1")
		List<User> findByEmail(String email);
		User findByUserid(String userid);

	}
	
