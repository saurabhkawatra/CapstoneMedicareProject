package com.Medicare.DAO;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Medicare.Entity.PurchaseHistory;

@Repository
public interface PurchaseHistoryDAO extends JpaRepository<PurchaseHistory, Integer> {
	
	public PurchaseHistory findById(int id);
	public List<PurchaseHistory> findByUserId(int userId);
	public List<PurchaseHistory> findByPurchaseDateAndUserId(Date purchaseDate,int userId);

}
