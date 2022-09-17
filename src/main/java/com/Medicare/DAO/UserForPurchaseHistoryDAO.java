package com.Medicare.DAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Medicare.Entity.UserForPurchaseHistory;

@Repository
public interface UserForPurchaseHistoryDAO extends CrudRepository<UserForPurchaseHistory, Integer> {

}
