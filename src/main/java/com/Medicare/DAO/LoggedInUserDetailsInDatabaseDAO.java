package com.Medicare.DAO;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Medicare.Entity.LoggedInUserDetailsInDatabase;
import com.Medicare.Entity.User;

@Repository
public interface LoggedInUserDetailsInDatabaseDAO extends CrudRepository<LoggedInUserDetailsInDatabase, Integer>{
	
	public LoggedInUserDetailsInDatabase findByAuthToken(String authToken);
	public LoggedInUserDetailsInDatabase findByLoggedInUserObjectUsername(String username);
	public void deleteByLoggedInUserObjectUsername(String username);
}
