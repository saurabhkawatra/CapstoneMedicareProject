package com.Medicare.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Medicare.Entity.Cart;

@Repository
public interface CartDAO extends CrudRepository<Cart, Integer> {

}
