package com.Medicare.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Medicare.Entity.Item;

@Repository
public interface ItemDAO extends CrudRepository<Item, Integer> {
		public Item findByItemId(int itemId);
		public Item findByItemName(String itemName);
		public Item findByItemCompany(String itemCompany);
		public Item findByItemCategory(String itemCategory);
		
}
