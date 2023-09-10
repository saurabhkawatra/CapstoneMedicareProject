package com.Medicare.Entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoggedInUserDetailsInDatabase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer loggedInUserDetailsId;
	@OneToOne
	private User loggedInUserObject;
	private String authToken;
	private Date loggedInDateAndTime;
	private Date lastActivityDateAndTime;

}
