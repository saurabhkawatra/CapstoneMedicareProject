package com.Medicare.Entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int userId;
	private String cardNo;
	private String cvv;
	private Date cardExpireDate;
	private String cardHolderName;
	private Date purchaseDate;
	
	
	@OneToOne(cascade = CascadeType.ALL)
	private UserForPurchaseHistory userForPurchaseHistory;
	@OneToMany(cascade = CascadeType.ALL)
	private List<ItemForPurchaseHistory> itemForPurchaseHistoryList;
	@ManyToOne
	private Contact deliveryContact;
	
	
}
