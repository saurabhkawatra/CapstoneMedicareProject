package com.Medicare.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Medicare.Entity.OtpUser;
import com.Medicare.Entity.UserOtpMap;

@Repository
public interface UserOtpMapDAO extends JpaRepository<UserOtpMap, Integer>{
	
	public UserOtpMap findByUser(OtpUser otpUser);

}
