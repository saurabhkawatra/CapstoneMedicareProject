package com.Medicare.DAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Medicare.Entity.ItemForPurchaseHistory;

@Repository
public interface ItemForPurchaseHistoryDAO extends CrudRepository<ItemForPurchaseHistory, Integer> {

}
