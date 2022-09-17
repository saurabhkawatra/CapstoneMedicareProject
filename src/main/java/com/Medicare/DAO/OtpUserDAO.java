package com.Medicare.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Medicare.Entity.OtpUser;

@Repository
public interface OtpUserDAO extends JpaRepository<OtpUser, Integer>{

}
