package com.Medicare.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int itemId;
	private String itemName;
	private double unitPrice;
	private String itemStatus;
	private String itemCompany;
	private String itemCategory;
	private int itemQuantity;
	private String itemImageUrl;
	private String itemImageName;
	private String itemImageType;

}
