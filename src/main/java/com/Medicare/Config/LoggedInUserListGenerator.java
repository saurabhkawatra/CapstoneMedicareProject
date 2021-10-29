package com.Medicare.Config;

import java.util.ArrayList;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.Medicare.Beans.LoggedInUserDetails;

@Configuration
public class LoggedInUserListGenerator {
	
	@Bean(name = "loggedInUsersDetailsList")
	public List<LoggedInUserDetails> getLoggedInUsersDetailsList() {
		List<LoggedInUserDetails> loggedInUserDetailsList = new ArrayList<>();
		return loggedInUserDetailsList;
	}

}
