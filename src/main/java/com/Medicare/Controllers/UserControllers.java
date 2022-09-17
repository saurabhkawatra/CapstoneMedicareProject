package com.Medicare.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Medicare.Beans.LoggedInUserDetails;
import com.Medicare.DAO.CartDAO;
import com.Medicare.DAO.ContactDAO;
import com.Medicare.DAO.ItemCategoryDAO;
import com.Medicare.DAO.ItemDAO;
import com.Medicare.DAO.LoggedInUserDetailsInDatabaseDAO;
import com.Medicare.DAO.PurchaseHistoryDAO;
import com.Medicare.DAO.UserDAO;
import com.Medicare.Entity.Item;
import com.Medicare.Entity.ItemForCart;
import com.Medicare.Entity.ItemForPurchaseHistory;
import com.Medicare.Entity.LoggedInUserDetailsInDatabase;
import com.Medicare.Entity.PurchaseHistory;
import com.Medicare.Entity.User;
import com.Medicare.Utils.BasicUtils;

@Controller
@RequestMapping("/user")
public class UserControllers {
	
	@Autowired
	private UserDAO userDao;
	@Autowired
	private CartDAO cartDao;
	@Autowired
	private ItemDAO itemDao;
	@Autowired
	private ItemCategoryDAO itemCategoryDao;
	@Autowired
	private ContactDAO contactDao;
	@Autowired
	private PurchaseHistoryDAO purchaseHistoryDao;
	@Autowired
	private LoggedInUserDetailsInDatabaseDAO loggedInUserDetailsInDatabaseDao;
	@Autowired
	List<LoggedInUserDetails> loggedInUsersDetailsList;
	private User user;
	
	public String userAuthentication(HttpServletRequest request) {
		System.out.println("Checking User Authority");
		String token=request.getHeader("AUTH_TOKEN");
		System.out.println("Checking User Authority :: Token ="+token);
		if(token==null)
			return "AuthenticationFailure";
//		for(LoggedInUserDetails ld:loggedInUsersDetailsList) {
//			if(ld.authToken.equals(token)) {
//				if(ld.loggedInUserObject.getAuthority().equals("ROLE_Admin")||ld.loggedInUserObject.getAuthority().equals("ROLE_User")) {
//					System.out.println("AdminAuthenticated");
//					return "UserAuthenticated";
//				}	
//			}
//		}
		LoggedInUserDetailsInDatabase liudid = loggedInUserDetailsInDatabaseDao.findByAuthToken(token);
		if(liudid != null) {
			if(liudid.getLoggedInUserObject().getAuthority().equals("ROLE_Admin") || liudid.getLoggedInUserObject().getAuthority().equals("ROLE_User"))
				return "AdminAuthenticated";
		}
		return "AuthenticationFailure";
	}
	
	
//	For ALL HANDLERS FOLLOW FOLLOWING TEMPLATE
//	
//	
//	String authority_authentication=userAuthentication(request);
//	
//	if(authority_authentication.equals("AuthenticationFailure")) {
//		try {
//			response.sendError(401);
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}
//		return null;
//	} else { }
	
	
	
	@RequestMapping(path = "/getAllItems",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public List<Item> getallactivateditems(HttpServletRequest request,HttpServletResponse response) {
		
		String authority_authentication=userAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from User getAllItems");
			return null;
		} else {
			List<Item> activatedItemsList = new ArrayList<Item>();
			itemDao.findAll().forEach(item -> {
						if(item.getItemStatus().equals("active"))
						activatedItemsList.add(item);
					});
			System.out.println(activatedItemsList);
			return activatedItemsList;
		}
	}
	@RequestMapping(path = "/getitembyId/{id}",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public Item getitembyId(@PathVariable(name = "id",required = true) int id,HttpServletRequest request,HttpServletResponse response) {
		String authority_authentication=userAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from Admin getitembyId()");
			return null;
		} else {
			System.out.println("Received Item id");
			System.out.println("Returning Item from User getitembyId()");
			return itemDao.findByItemId(id);
		}
	}
	
	@RequestMapping(path = "/addItemToCart",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public ResponseToken addItemToCart(@RequestBody Item item,HttpServletRequest request,HttpServletResponse response) {
		
		String authority_authentication=userAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from User addItemToCart");
			return null;
		} else {
			User user=new User();
			String token=request.getHeader("AUTH_TOKEN");
//			for(LoggedInUserDetails ld:loggedInUsersDetailsList) {
//				if(ld.authToken.equals(token)) {
//					user=userDao.findByUserId(ld.getLoggedInUserObject().getUserId());
//					user.getCart().getItemsInCart().add(BasicUtils.convertItemToItemForCart(item));
//					userDao.save(user);
//					ld.setLoggedInUserObject(user);
//					System.out.println("item Added");
//				}
//			}
			user = loggedInUserDetailsInDatabaseDao.findByAuthToken(token).getLoggedInUserObject();
			user.getCart().getItemsInCart().add(BasicUtils.convertItemToItemForCart(item));
			userDao.save(user);
			return new ResponseToken("Item Added to Cart!!", null);
		}
	}
	
	@RequestMapping(path = "/removeItemFromCart",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public ResponseToken removeItemFromCart(@RequestBody Item item,HttpServletRequest request,HttpServletResponse response) {
		
		String authority_authentication=userAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from User removeItemFromCart");
			return null;
		} else {
			User user=new User();
			String token=request.getHeader("AUTH_TOKEN");
//			for(LoggedInUserDetails ld:loggedInUsersDetailsList) {
//				if(ld.authToken.equals(token)) {
//					user=userDao.findByUserId(ld.getLoggedInUserObject().getUserId());
////					user.getCart().getItemsInCart().remove(BasicUtils.convertItemToItemForCart(item));
//					user.getCart().getItemsInCart().remove(
//							user.getCart().getItemsInCart().stream().filter(filterItem -> {
//								if(filterItem.getItemId() == item.getItemId()) 
//									return true;
//								else
//									return false;
//								}).collect(Collectors.toList()).get(
//										user.getCart().getItemsInCart().stream().filter(filterItem -> {
//									if(filterItem.getItemId() == item.getItemId()) 
//										return true;
//									else
//										return false;
//									}).collect(Collectors.toList()).size()-1)
//							);
//					userDao.save(user);
//					ld.setLoggedInUserObject(user);
//					System.out.println("item Removed");
//				}
//			}
			user = loggedInUserDetailsInDatabaseDao.findByAuthToken(token).getLoggedInUserObject();
			user.getCart().getItemsInCart().remove(
					user.getCart().getItemsInCart().stream().filter(filterItem -> {
						if(filterItem.getItemId() == item.getItemId()) 
							return true;
						else
							return false;
						}).collect(Collectors.toList()).get(
								user.getCart().getItemsInCart().stream().filter(filterItem -> {
							if(filterItem.getItemId() == item.getItemId()) 
								return true;
							else
								return false;
							}).collect(Collectors.toList()).size()-1)
					);
			userDao.save(user);
			System.out.println("item Removed");
			return new ResponseToken("Item Removed From Cart!!", null);
		}
	}
	
	
	@RequestMapping(path = "/getItemsInCart",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public List<ItemForCart> getItemsInCart(HttpServletRequest request,HttpServletResponse response) {
		
		String authority_authentication=userAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from User getItemsInCart");
			return null;
		} else {
			User user=new User();
			String token=request.getHeader("AUTH_TOKEN");
//			for(LoggedInUserDetails ld:loggedInUsersDetailsList) {
//				if(ld.authToken.equals(token)) {
//					user=userDao.findByUserId(ld.getLoggedInUserObject().getUserId());
//				}
//			}
			user = loggedInUserDetailsInDatabaseDao.findByAuthToken(token).getLoggedInUserObject();
			
			return user.getCart().getItemsInCart();
		}
	}
	
	
	@RequestMapping(path = "/getUserDetails",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public User getUserDetails(HttpServletRequest request,HttpServletResponse response) {
		
		String authority_authentication=userAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from User getuserdetails");
			return null;
		} else {
			String token=request.getHeader("AUTH_TOKEN");
			User u=new User();
			
//			for(LoggedInUserDetails ld:loggedInUsersDetailsList) {if(ld.getAuthToken().equals(token)) u=ld.getLoggedInUserObject();}
			u = loggedInUserDetailsInDatabaseDao.findByAuthToken(token).getLoggedInUserObject();
			return u;
		}
	}
	
	
	@RequestMapping(path = "/paymentMade",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public ResponseToken paymentMade(@RequestBody PurchaseHistory ph,HttpServletRequest request,HttpServletResponse response) {
		
		String authority_authentication=userAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from User paymentMade");
			return null;
		} else {
			String token=request.getHeader("AUTH_TOKEN");
			User u=new User();
//			for(LoggedInUserDetails ld:loggedInUsersDetailsList) {if(ld.getAuthToken().equals(token)) u=ld.getLoggedInUserObject();}
			u = loggedInUserDetailsInDatabaseDao.findByAuthToken(token).getLoggedInUserObject();
				if(u.getCart().getItemsInCart().size()==0) {
					System.out.println("Order Failure!! No Items In Cart!!");
					return new ResponseToken("Order Failure!! No Items In Cart!!", null);
				} else {
					ph.getCardExpireDate().setHours(0);
					ph.getCardExpireDate().setMinutes(0);
					ph.getCardExpireDate().setSeconds(0);
					List<ItemForPurchaseHistory> cartItemsConverted=new ArrayList<>();
					cartItemsConverted = u.getCart().getItemsInCart().stream().map(item -> {
						return BasicUtils.convertItemForCartToItemForPurchaseHistory(item);
					}).collect(Collectors.toList());
					ph.setItemForPurchaseHistoryList(cartItemsConverted);
					ph.setPurchaseDate(new Date());
					ph.setUserForPurchaseHistory(BasicUtils.convertUserToUserForPurchaseHistory(u));
					ph.setUserId(u.getUserId());
					purchaseHistoryDao.save(ph);
					u.getCart().getItemsInCart().clear();
					userDao.save(u);
					System.out.println("Purchase History Object :: "+ph);
					System.out.println("User Object :: "+u);
					return new ResponseToken("Order Placed!!", null);
				}
		}
	}
	
	
	@RequestMapping(path = "/getLatestPurchaseDetails",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public PurchaseHistory getLatestPurchaseDetails(HttpServletRequest request,HttpServletResponse response) {
		
		String authority_authentication=userAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from User getLatestPurchaseDetails");
			return null;
		} else {
			String token=request.getHeader("AUTH_TOKEN");
			User u=new User();
//			for(LoggedInUserDetails ld:loggedInUsersDetailsList) {if(ld.getAuthToken().equals(token)) u=ld.getLoggedInUserObject();}
			u = loggedInUserDetailsInDatabaseDao.findByAuthToken(token).getLoggedInUserObject();
				List<PurchaseHistory> phList=purchaseHistoryDao.findByUserId(u.getUserId());
				if(phList==null) {
					return null;
				} else {
					return phList.get(phList.size()-1);
				}
		}
	}
	
	
	@RequestMapping(path = "/getListOfPurchases",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public List<PurchaseHistory> getListOfPurchases(HttpServletRequest request,HttpServletResponse response) {
		
		String authority_authentication=userAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from User getListOfPurchases");
			return null;
		} else {
			String token=request.getHeader("AUTH_TOKEN");
			User u=new User();
//			for(LoggedInUserDetails ld:loggedInUsersDetailsList) {if(ld.getAuthToken().equals(token)) u=ld.getLoggedInUserObject();}
			u = loggedInUserDetailsInDatabaseDao.findByAuthToken(token).getLoggedInUserObject();
				List<PurchaseHistory> phList=purchaseHistoryDao.findByUserId(u.getUserId());
				if(phList==null) {
					return null;
				} else {
					return phList;
				}
		}
	}
	
	
	
	
	
	
	
	
	
	

}
