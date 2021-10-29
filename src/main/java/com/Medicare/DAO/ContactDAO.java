package com.Medicare.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Medicare.Entity.Contact;

public interface ContactDAO extends JpaRepository<Contact, Integer>{

}
