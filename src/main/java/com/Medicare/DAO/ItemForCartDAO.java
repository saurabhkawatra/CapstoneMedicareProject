package com.Medicare.DAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Medicare.Entity.ItemForCart;

@Repository
public interface ItemForCartDAO extends CrudRepository<ItemForCart, Integer> {

}
