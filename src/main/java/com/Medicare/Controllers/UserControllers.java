package com.Medicare.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Medicare.Beans.LoggedInUserDetails;
import com.Medicare.DAO.CartDAO;
import com.Medicare.DAO.ContactDAO;
import com.Medicare.DAO.ItemCategoryDAO;
import com.Medicare.DAO.ItemDAO;
import com.Medicare.DAO.PurchaseHistoryDAO;
import com.Medicare.DAO.UserDAO;
import com.Medicare.Entity.Item;
import com.Medicare.Entity.PurchaseHistory;
import com.Medicare.Entity.User;

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
	List<LoggedInUserDetails> loggedInUsersDetailsList;
	
	public String userAuthentication(HttpServletRequest request) {
		System.out.println("Checking User Authority");
		String token=request.getHeader("AUTH_TOKEN");
		System.out.println("Checking User Authority :: Token ="+token);
		if(token==null)
			return "AuthenticationFailure";
		for(LoggedInUserDetails ld:loggedInUsersDetailsList) {
			if(ld.authToken.equals(token)) {
				if(ld.loggedInUserObject.getAuthority().equals("ROLE_Admin")||ld.loggedInUserObject.getAuthority().equals("ROLE_User")) {
					System.out.println("AdminAuthenticated");
					return "UserAuthenticated";
				}	
			}
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
			for(LoggedInUserDetails ld:loggedInUsersDetailsList) {
				if(ld.authToken.equals(token)) {
					user=userDao.findByUserId(ld.getLoggedInUserObject().getUserId());
					user.getCart().getItemsInCart().add(item);
					userDao.save(user);
					ld.setLoggedInUserObject(user);
					System.out.println("item Added");
				}
			}
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
			for(LoggedInUserDetails ld:loggedInUsersDetailsList) {
				if(ld.authToken.equals(token)) {
					user=userDao.findByUserId(ld.getLoggedInUserObject().getUserId());
					user.getCart().getItemsInCart().remove(item);
					userDao.save(user);
					ld.setLoggedInUserObject(user);
					System.out.println("item Removed");
				}
			}
			return new ResponseToken("Item Removed From Cart!!", null);
		}
	}
	
	
	@RequestMapping(path = "/getItemsInCart",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public List<Item> getItemsInCart(HttpServletRequest request,HttpServletResponse response) {
		
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
			for(LoggedInUserDetails ld:loggedInUsersDetailsList) {
				if(ld.authToken.equals(token)) {
					user=userDao.findByUserId(ld.getLoggedInUserObject().getUserId());
				}
			}
			
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
			
			for(LoggedInUserDetails ld:loggedInUsersDetailsList) {if(ld.getAuthToken().equals(token)) u=ld.getLoggedInUserObject();}
			
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
			for(LoggedInUserDetails ld:loggedInUsersDetailsList) {if(ld.getAuthToken().equals(token)) u=ld.getLoggedInUserObject();}
				if(u.getCart().getItemsInCart().size()==0) {
					System.out.println("Order Failure!! No Items In Cart!!");
					return new ResponseToken("Order Failure!! No Items In Cart!!", null);
				} else {
					ph.getCardExpireDate().setHours(0);
					ph.getCardExpireDate().setMinutes(0);
					ph.getCardExpireDate().setSeconds(0);
					List<Item> cartItems=new ArrayList<>();
					cartItems.addAll(u.getCart().getItemsInCart());
					ph.setItemList(cartItems);
					ph.setPurchaseDate(new Date());
					ph.setUser(u);
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
			for(LoggedInUserDetails ld:loggedInUsersDetailsList) {if(ld.getAuthToken().equals(token)) u=ld.getLoggedInUserObject();}
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
			for(LoggedInUserDetails ld:loggedInUsersDetailsList) {if(ld.getAuthToken().equals(token)) u=ld.getLoggedInUserObject();}
				List<PurchaseHistory> phList=purchaseHistoryDao.findByUserId(u.getUserId());
				if(phList==null) {
					return null;
				} else {
					return phList;
				}
		}
	}
	
	
	
	
	
	
	
	
	
	

}
