package com.Medicare.Utils;

import com.Medicare.Entity.Item;
import com.Medicare.Entity.ItemForCart;
import com.Medicare.Entity.ItemForPurchaseHistory;
import com.Medicare.Entity.OtpUser;
import com.Medicare.Entity.User;
import com.Medicare.Entity.UserForPurchaseHistory;

public class BasicUtils {
	
	public static User convertOtpUserToUser(OtpUser otpUser) {
		return new User(0, otpUser.getFirstName(), otpUser.getLastName(), otpUser.getUsername(), otpUser.getPassword(), otpUser.getPrimaryEmail(), otpUser.getPrimaryPhoneNo(), otpUser.getAuthority(), otpUser.getDateOfBirth(), null, null, null) ;
	}
	
	public static OtpUser convertUserToOtpUser(User user) {
		return new OtpUser(0, user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getPrimaryEmail(), user.getPrimaryPhoneNo(), user.getAuthority(), user.getDateOfBirth());
	}
	
	public static UserForPurchaseHistory convertUserToUserForPurchaseHistory(User user) {
		return new UserForPurchaseHistory(0, user.getUserId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getPrimaryEmail(), user.getPrimaryPhoneNo(), user.getAuthority(), user.getDateOfBirth());
	}
	
	public static ItemForPurchaseHistory convertItemToItemForPurchaseHistory(Item item) {
		return new ItemForPurchaseHistory(0, item.getItemId(), item.getItemName(), item.getUnitPrice(), item.getItemStatus(), item.getItemCompany(), item.getItemCategory(), item.getItemQuantity(), item.getItemImageUrl(), item.getItemImageName(), item.getItemImageType());
	}
	
	public static ItemForPurchaseHistory convertItemForCartToItemForPurchaseHistory(ItemForCart item) {
		return new ItemForPurchaseHistory(0, item.getItemId(), item.getItemName(), item.getUnitPrice(), item.getItemStatus(), item.getItemCompany(), item.getItemCategory(), item.getItemQuantity(), item.getItemImageUrl(), item.getItemImageName(), item.getItemImageType());
	}
	
	public static ItemForCart convertItemToItemForCart(Item item) {
		return new ItemForCart(0, item.getItemId(), item.getItemName(), item.getUnitPrice(), item.getItemStatus(), item.getItemCompany(), item.getItemCategory(), item.getItemQuantity(), item.getItemImageUrl(), item.getItemImageName(), item.getItemImageType());
	}

}
