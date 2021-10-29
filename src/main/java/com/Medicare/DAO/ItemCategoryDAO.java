package com.Medicare.DAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Medicare.Entity.ItemCategory;

@Repository
public interface ItemCategoryDAO extends CrudRepository<ItemCategory, Integer> {

}
