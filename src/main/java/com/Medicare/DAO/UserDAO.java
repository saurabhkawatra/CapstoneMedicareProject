package com.Medicare.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Medicare.Entity.User;

@Repository
public interface UserDAO extends CrudRepository<User, Integer> {
	
	public User findByUsername(String username);
	public User findByPrimaryEmail(String email);
	public User findByPrimaryPhoneNo(String phoneno);
	public User findByUserId(int userId);
	
}
