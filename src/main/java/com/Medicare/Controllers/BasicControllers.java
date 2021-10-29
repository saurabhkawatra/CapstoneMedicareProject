package com.Medicare.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.Medicare.Beans.LoggedInUserDetails;
import com.Medicare.DAO.CartDAO;
import com.Medicare.DAO.ContactDAO;
import com.Medicare.DAO.ItemCategoryDAO;
import com.Medicare.DAO.ItemDAO;
import com.Medicare.DAO.UserDAO;
import com.Medicare.Entity.Cart;
import com.Medicare.Entity.Contact;
import com.Medicare.Entity.Item;
import com.Medicare.Entity.User;

@Controller
public class BasicControllers {

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
	List<LoggedInUserDetails> loggedInUsersDetailsList;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	public String loginUserAndReturnToken(User u) {
		double randomDouble = Math.round(Math.random() * 1000);
		int randomInteger = (int) randomDouble;
		String token="";
		token=String.valueOf(randomInteger);
		Date d=new Date();
		token=token.concat(u.getUsername()+String.valueOf(d));
		token=token.replaceAll(" ", "");
		loggedInUsersDetailsList.add(new LoggedInUserDetails(u, token));
		return token;
	}
	public void checkForMultipleLogin(User user) {
		int count=0;
		LoggedInUserDetails liudcheck=new LoggedInUserDetails();
		for (LoggedInUserDetails liud : loggedInUsersDetailsList) {
			if (liud.getLoggedInUserObject().getUsername().equals(user.getUsername())) {
				if (liud.getLoggedInUserObject().getPassword().equals(user.getPassword())) {
					count++;
					if(count==1) {liudcheck=liud;}
				}
			}
		}
		for(int i=0;i<count;i++) {
			loggedInUsersDetailsList.remove(liudcheck);
			System.out.println("Duplicate Removal Done!");
		}
	}

	public String getAuthenticationAndReceiveToken(@ModelAttribute User u) {
		String check = "";
		for (LoggedInUserDetails liud : loggedInUsersDetailsList) {
			if (liud.getLoggedInUserObject().getUsername().equals(u.getUsername())) {
				if (liud.getLoggedInUserObject().getPassword().equals(u.getPassword())) {
					check = "user_already_logged_in";
				} else {
					check = "user_already_logged_in_username_match_password_incorrect";
				}
			}
		}
		return "";
	}

//	@RequestMapping(value = "/login", method = RequestMethod.POST)
//	@ResponseBody
//	@CrossOrigin(origins = "*")
//	public ResponseEntity<?> login(@RequestBody User user, @RequestParam("para1") String parameter,
//			@RequestHeader(name = "API_KEY") HttpHeaders header) {
//
//		System.out.println("user :: " + user);
//		System.out.println("Parameter para1 = " + parameter);
//		System.out.println("Hearder value =" + header.getFirst("API_KEY"));
//		System.out.println("usrname - " + user.getUsername() + " passwd - " + user.getPassword());
//		ResponseToken restoken = new ResponseToken();
//		restoken.setToken("login_success");
//		return ResponseEntity.ok(restoken);
//	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin(origins = "*")
	public ResponseEntity<?> login(@RequestBody User user) {

		User loginUser = userDao.findByUsername(user.getUsername());
		
		ResponseToken restoken = new ResponseToken();
		
		if( loginUser!=null && loginUser.getPassword().equals(user.getPassword())) {
			checkForMultipleLogin(loginUser);
			String authToken=loginUserAndReturnToken(loginUser);
			if(loginUser.getAuthority().equals("ROLE_User")) {
				restoken.setMessage("user_login_success");
				restoken.setToken(authToken);
				System.out.println("user_login_success");
			} else {
				System.out.println("admin_login_success");
				restoken.setMessage("admin_login_success");
				restoken.setToken(authToken);
			}
		} else {
			restoken.setMessage("login_failure");
		}
		
		return ResponseEntity.ok(restoken);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public String register(@RequestBody User reg_user) {
		System.out.println("User Details :: " + reg_user);

		if (userDao.findByUsername(reg_user.getUsername()) != null) {
			System.out.println("Duplicate Username");
			return "Duplicate Username";
		}
		if (userDao.findByPrimaryEmail(reg_user.getPrimaryEmail()) != null) {
			System.out.println("Duplicate Email");
			return "Duplicate Email";
		}
		if (userDao.findByPrimaryPhoneNo(reg_user.getPrimaryPhoneNo()) != null) {
			System.out.println("Duplicate Primary PhoneNumber");
			return "Duplicate Primary PhoneNumber";
		}

		reg_user.setCart(new Cart());
		reg_user.setAuthority("ROLE_User");
		reg_user.setContacts(new ArrayList<>());
		Contact defaultContact= new Contact(0, reg_user.getPrimaryEmail(), reg_user.getPrimaryPhoneNo(), null, null);
		contactDao.save(defaultContact);
		reg_user.getContacts().add(defaultContact);
		System.out.println(reg_user);
		User u = userDao.save(reg_user);
		System.out.println(u);
		return "Registration Success";

	}
	
	@RequestMapping(value="/checkforauthority",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public ResponseEntity<?> checkauthority(HttpServletRequest request) {
		
		String token=request.getHeader("AUTH_TOKEN");
		System.out.println("testing checkauthority...token= "+token);
		int check=0;
		String authority;
		ResponseToken rt = new ResponseToken("noAuthority", null);
		if(token==null) {
			rt.message="noAuthority";
			return ResponseEntity.ok(rt);
		}
		for(LoggedInUserDetails ld:loggedInUsersDetailsList) {
			if(ld.getAuthToken().equals(token)) {
				check=1;
				if(ld.getLoggedInUserObject().getAuthority().equals("ROLE_Admin")) {
					rt.message="admin";
					return ResponseEntity.ok(rt);
				} else {
					rt.message="user";
					return ResponseEntity.ok(rt);
				}
					
			}
		}
		if(check==0) {
			rt.message="noAuthority";
			return ResponseEntity.ok(rt);
		}
		
		return ResponseEntity.ok(rt);
	}

	@RequestMapping(value = "/checkforusername/{username}", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public String checkusername(@PathVariable(name = "username") String username) {
		if (userDao.findByUsername(username) != null) {
			System.out.println("UsernameNotAvailable");
			return "UsernameNotAvailable";
		} else {
			System.out.println("UsernameAvailable");
			return "UsernameAvailable";
		}
	}

	@RequestMapping(value = "/checkforprimaryphoneno/{phno}", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public String checkprimaryphoneno(@PathVariable(name = "phno") String phno) {
		if (userDao.findByPrimaryPhoneNo(phno) != null) {
			System.out.println("PhonenoDuplicate");
			return "PhonenoDuplicate";
		} else {
			System.out.println("PhonenoAvailable");
			return "PhonenoAvailable";
		}
	}

	@RequestMapping(value = "/checkforprimaryemail/{email}", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public String checkprimaryemail(@PathVariable(name = "email") String email) {
		if (userDao.findByPrimaryEmail(email) != null) {
			System.out.println("EmailDuplicate");
			return "EmailDuplicate";
		} else {
			System.out.println("EmailAvailable");
			return "EmailAvailable";

		}
	}
	
	@RequestMapping(value="/logout",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public ResponseToken logout(HttpServletRequest request) {
		String token=request.getHeader("AUTH_TOKEN");
		if(token!=null)
		for(int i=0;i<loggedInUsersDetailsList.size();i++) {
			if(loggedInUsersDetailsList.get(i).authToken.equals(token)) {
				System.out.println("Logging out - fname= "+loggedInUsersDetailsList.get(i).getLoggedInUserObject().getFirstName()+" username= "+loggedInUsersDetailsList.get(i).getLoggedInUserObject().getUsername());
				loggedInUsersDetailsList.remove(i);
			}
		}
		return new ResponseToken("Logged Out Successfully!", null);
	}
	@RequestMapping(value = "/test", method = RequestMethod.GET /* , headers = { "AUTH_KEY" } */)
	@ResponseBody
	public String test(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Test Running");
		System.out.println(loggedInUsersDetailsList);
		System.out.println("Number of Logged In Users in the List="+loggedInUsersDetailsList.size());
		
//		User u=userDao.findByUserId(1);
//		String s=loginUserAndReturnToken(u);
//		System.out.println(s);

//		String Key=request.getHeader("AUTH_KEY");
//		if(Key!=null) {
//			System.out.println("Key = "+Key);
//		} else {
//			System.out.println("AUTH_KEY not found");
//		}

//		Item i=new Item(0, "Test Item", "Pfizer", "custom Category", 10, "xyz.jpg", "jpg");
//		itemDao.save(i);
//		Cart cart=new Cart();
//		List<Item> itemlist = new ArrayList<Item>();
//		itemlist.add(i);
//		cart.setItemsInCart(itemlist);
//		User u1= new User(0,"testing" , "testing", "username", "pass", "@mail.com", "1234", "admin", null, null, cart);
//		User opuser = userDao.save(u1);
//		System.out.println(opuser);

//		userDao.delete(userDao.findByUserId(24));

//		User u = userDao.findByUserId(1);
//		u.getCart().getItemsInCart().add(itemDao.findByItemId(23));
//		User saveduser = userDao.save(u);
//		System.out.println(saveduser);

//		Item i1=new Item(0, "Test Item 4", 25.75, "active", "Pfizer4", "custom Category1", 10, "url4" , "xyasz.jpg1", "jpg1");
//		Item i2=new Item(0, "Test Item 5", 30.89, "active", "Pfizer5", "custom Category2", 10, "url5", "xysdsdz.jpg2", "jpg2");
//		Item i3=new Item(0, "Test Item 6", 30.74, "active", "Pfizer6", "custom Category3", 10, "url6", "xysdz.jpg3", "jpg3");
//		itemDao.save(i1);
//		itemDao.save(i2);
//		itemDao.save(i3);

//		User u = userDao.findByUserId(2);
//		u.getCart().getItemsInCart().add(itemDao.findByItemId(4));
//		u.getCart().getItemsInCart().add(itemDao.findByItemId(5));
//		u.getCart().getItemsInCart().add(itemDao.findByItemId(6));
//		u.getCart().getItemsInCart().add(itemDao.findByItemId(6));
//		User saveduser = userDao.save(u);
//		System.out.println(saveduser);

//		User u = userDao.findByUserId(1);
//		System.out.println(u.getCart());

//		User u = userDao.findByUserId(1);
//		System.out.println(u.getCart().getItemsInCart().size());

//		itemDao.delete(itemDao.findByItemId(26));
//		--->Didn't Work --> item needs to be removed from where ever it used

//		User u = userDao.findByUserId(1);
//		List<Item> itemsInCart = u.getCart().getItemsInCart();
//		System.out.println("Before Delete :: "+u);
//		int count=0;
//		
//		for(int i=0;i<itemsInCart.size();i++) {
//			if(itemsInCart.get(i).getItemId()==23) {count++;}
//		}
//		for(int i=0;i<count;i++) {itemsInCart.remove(itemDao.findByItemId(23));}
//		userDao.save(u);
//		itemDao.delete(itemDao.findByItemId(23));
//		
		return "run ok";
	}
}
