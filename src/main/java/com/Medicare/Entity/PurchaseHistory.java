package com.Medicare.Entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

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
	
	@ManyToOne
	private User user;
	@ManyToMany
	private List<Item> itemList;
	@ManyToOne
	private Contact deliveryContact;
	
	
}
