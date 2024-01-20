package com.Medicare.Controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.Medicare.Beans.LoggedInUserDetails;
import com.Medicare.DAO.CartDAO;
import com.Medicare.DAO.ContactDAO;
import com.Medicare.DAO.ItemCategoryDAO;
import com.Medicare.DAO.ItemDAO;
import com.Medicare.DAO.LoggedInUserDetailsInDatabaseDAO;
import com.Medicare.DAO.OtpUserDAO;
import com.Medicare.DAO.UserDAO;
import com.Medicare.DAO.UserOtpMapDAO;
import com.Medicare.Entity.Cart;
import com.Medicare.Entity.Contact;
import com.Medicare.Entity.Item;
import com.Medicare.Entity.LoggedInUserDetailsInDatabase;
import com.Medicare.Entity.OtpUser;
import com.Medicare.Entity.User;
import com.Medicare.Entity.UserOtpMap;
import com.Medicare.Services.EmailService;
import com.Medicare.Utils.BasicUtils;

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
	private OtpUserDAO otpUserDao;
	@Autowired
	private UserOtpMapDAO userOtpMapDao;
	@Autowired
	private LoggedInUserDetailsInDatabaseDAO loggedInUserDetailsInDatabaseDao;
	@Autowired
	private EmailService emailService;
	private Map<OtpUser, String> otpUserMap = new HashMap<>();
	@Autowired
	List<LoggedInUserDetails> loggedInUsersDetailsList;
	
	
	
	@Value("${thisIsACustomPropertyInApplicationPropertiesFile}")
	private String thisIsACustomPropertyInApplicationPropertiesFile;
	
	@Value("${currentSystemOperatingSystemName}")
	private String currentSystemOperatingSystemName;
	
	
	

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("testModelAttribureName", "testModelAttribureValue");
		return "index";
	}
	
	@RequestMapping("/serveDemoAngularApplication/**")
	public String serveDemoAngularApplication(Model model) {
		model.addAttribute("testModelAttribureName", "testModelAttribureValue");
		model.addAttribute("demoAngularAppKeyAttribute", "DemoAngAppKey"+(int) Math.floor(Math.random()*10000)+"dateTime"+new Date().toGMTString().replaceAll(" ", "")+"KeyEnd");
		return "indexAngularDemoApp";
	}
	
	@RequestMapping("/serveMedicareAngularApplication/**")
	public String serveMedicareAngularApplication(Model model) {
		model.addAttribute("testModelAttribureName", "testModelAttribureValue");
		model.addAttribute("demoAngularAppKeyAttribute", "DemoAngAppKey"+(int) Math.floor(Math.random()*10000)+"dateTime"+new Date().toGMTString().replaceAll(" ", "")+"KeyEnd");
		return "indexMedicareAngularApp";
	}
	
	
	// Scheduler Task Will Run every 5 minutes and delete Users from LoggedInUsersDetailsInDatabase Table who are inactive for more than 15 mins or their session time has exceeded 4 hours.
	@Scheduled(fixedDelay = 300000)
	public void scheduledCheckForIdleUsersInLoggedInUserDao() {
		System.out.println("Scheduler checkForIdleUsersInLoggedInUserDao() Running....");
		System.out.println("OS Name - "+System.getProperty("os.name")+" - System.getProperty(\"os.name\")");
		System.out.println("OS Architecture - "+System.getProperty("os.name")+" - System.getProperty(\"os.name\")");
		System.out.println("Property Values Printing from application properties file : thisIsACustomPropertyInApplicationPropertiesFile - "+thisIsACustomPropertyInApplicationPropertiesFile);
		System.out.println("Property Values Printing from application properties file : currentSystemOperatingSystemName - "+currentSystemOperatingSystemName);
		System.out.println("Creating test.txt File");
		File file = new File("test.txt");
		try {
			file.createNewFile();
			System.out.println("file getAbsolutePath() "+file.getAbsolutePath());
			System.out.println("file getParent() "+file.getParent());
			System.out.println("file getPath() "+file.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<LoggedInUserDetailsInDatabase> iterator = loggedInUserDetailsInDatabaseDao.findAll().iterator();
		while(iterator.hasNext()) {
			LoggedInUserDetailsInDatabase loggedInUserDetailsInDatabase = iterator.next();
			Date currentDateAndTime = new Date();
			if(currentDateAndTime.getTime() - loggedInUserDetailsInDatabase.getLoggedInDateAndTime().getTime() > 14400000) {
				// Logging out when (Current Time - Login Time) > 4 hours 
				loggedInUserDetailsInDatabaseDao.deleteById(loggedInUserDetailsInDatabase.getLoggedInUserDetailsId());
			} else if(currentDateAndTime.getTime() - loggedInUserDetailsInDatabase.getLastActivityDateAndTime().getTime() > 900000) {
				// Logging out when (Current Time - Last Activity Time) > 15 mins
				loggedInUserDetailsInDatabaseDao.deleteById(loggedInUserDetailsInDatabase.getLoggedInUserDetailsId());
			}
		}
	}

	public String loginUserAndReturnToken(User u) {
		double randomDouble = Math.round(Math.random() * 1000);
		int randomInteger = (int) randomDouble;
		String token="";
		token=String.valueOf(randomInteger);
		Date d=new Date();
		token=token.concat(u.getUsername()+String.valueOf(d));
		token=token.replaceAll(" ", "");
//		loggedInUsersDetailsList.add(new LoggedInUserDetails(u, token));
		loggedInUserDetailsInDatabaseDao.save(new LoggedInUserDetailsInDatabase(0, u, token, new Date(), new Date()));
		return token;
	}
//	Below method checks if the user already logged in, then logout that user and login it again with new Token id
	public void checkForMultipleLogin(User user) {
//		int count=0;
//		LoggedInUserDetails liudcheck=new LoggedInUserDetails();
//		for (LoggedInUserDetails liud : loggedInUsersDetailsList) {
//			if (liud.getLoggedInUserObject().getUsername().equals(user.getUsername())) {
//				if (liud.getLoggedInUserObject().getPassword().equals(user.getPassword())) {
//					count++;
//					if(count==1) {liudcheck=liud;}
//				}
//			}
//		}
//		for(int i=0;i<count;i++) {
//			loggedInUsersDetailsList.remove(liudcheck);
//			System.out.println("Duplicate Removal Done!");
//		}
		LoggedInUserDetailsInDatabase loggedInUserDetailsInDatabase = loggedInUserDetailsInDatabaseDao.findByLoggedInUserObjectUsername(user.getUsername());
		if(loggedInUserDetailsInDatabase != null) {
			System.out.println("TESTING LOGIN DB DUPLICATE CHECK CALL");
			System.out.println(loggedInUserDetailsInDatabaseDao.findByLoggedInUserObjectUsername(user.getUsername()));
			loggedInUserDetailsInDatabaseDao.deleteById(loggedInUserDetailsInDatabase.getLoggedInUserDetailsId());
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
//			Check if same user can login from multiple browser or devices
//			Below method checks if the user already logged in, then logout that user and login it again with new Token id
//			If below method is commented, multi-login will be enabled else disabled
//			If multi-login is enabled, different login sessions are maintained separately
//			checkForMultipleLogin(loginUser);
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
	
	@RequestMapping(value = "/sendRegistrationOtp", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public String sendRegistrationOtp(@RequestBody OtpUser reg_user) {
		if (userDao.findByUsername(reg_user.getUsername()) != null) {
			System.out.println("Duplicate Username");
			return "{\"message\":\"Duplicate Username\"}";
		}
		if (userDao.findByPrimaryEmail(reg_user.getPrimaryEmail()) != null) {
			System.out.println("Duplicate Email");
			return "{\"message\":\"Duplicate Email\"}";
		}
		if (userDao.findByPrimaryPhoneNo(reg_user.getPrimaryPhoneNo()) != null) {
			System.out.println("Duplicate Primary PhoneNumber");
			return "{\"message\":\"Duplicate Primary PhoneNumber\"}";
		}
		
		int randomTwoDigits = (int) Math.floor(Math.random()*100);
		String otp= String.valueOf(reg_user.getUsername().charAt(0)) 
				+ String.valueOf(randomTwoDigits) 
				+ String.valueOf(reg_user.getUsername().charAt(reg_user.getUsername().length()-1))
				+ String.valueOf((int) Math.floor(Math.random()*100));
		try {
			emailService.sendSimpleTextEmail(reg_user.getPrimaryEmail(), "Medicare OTP", "Please Use the OTP - "+otp+" to authenticate yourself");
			
			UserOtpMap savedUserOtpMap = userOtpMapDao.save(new UserOtpMap(null, reg_user, otp));
			otpUserMap.put(reg_user, otp);
			int sleepTime = 30000;
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.currentThread().sleep(sleepTime);
						System.out.println("OTP Timeout For User " + reg_user);
						try {
							userOtpMapDao.delete(savedUserOtpMap);
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("Exception From Register API otp check while delete For User" + reg_user);
							System.out.println(e.getMessage());
						}
						otpUserMap.remove(reg_user);
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.println("Interrupted Exception From Thread For User" + reg_user);
						System.out.println(e.getMessage());
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Generic Exception From Thread For User" + reg_user);
						System.out.println(e.getMessage());
					}
				}
			});
			t1.start();
			System.out.println("OTP sent");
			return "{\"message\":\"OTP sent\",\"expirationTime\":\""+sleepTime+"\"}";
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println("OTP failure");
			return "{\"message\":\"OTP Failure! Check Email and Try Again!\"}";
		}
	}
	

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public String register(@RequestBody User reg_user,@RequestParam("otp") String otp) {
		System.out.println("User Details :: " + reg_user);

		if (otpUserMap.get(reg_user) == null || !otpUserMap.get(reg_user).equals(otp)) {
			otpUserMap.remove(reg_user);
			try {
				userOtpMapDao.delete(userOtpMapDao.findByUser(BasicUtils.convertUserToOtpUser(reg_user)));
			} catch (Exception e) {
				System.out.println("Exception From Register API otp check while delete For User " + reg_user);
				System.out.println(e.getMessage());
			}
			System.out.println("Invalid OTP !!");
			return "Invalid OTP !!";
		}
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

		otpUserMap.remove(reg_user);
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
//		for(LoggedInUserDetails ld:loggedInUsersDetailsList) {
//			if(ld.getAuthToken().equals(token)) {
//				check=1;
//				if(ld.getLoggedInUserObject().getAuthority().equals("ROLE_Admin")) {
//					rt.message="admin";
//					return ResponseEntity.ok(rt);
//				} else {
//					rt.message="user";
//					return ResponseEntity.ok(rt);
//				}
//					
//			}
//		}
//		if(check==0) {
//			rt.message="noAuthority";
//			return ResponseEntity.ok(rt);
//		}
		
		LoggedInUserDetailsInDatabase liudid = loggedInUserDetailsInDatabaseDao.findByAuthToken(token);
		if(liudid != null) {
			if(liudid.getLoggedInUserObject().getAuthority().equals("ROLE_Admin")) {
				liudid.setLastActivityDateAndTime(new Date());
				loggedInUserDetailsInDatabaseDao.save(liudid);
				rt.message="admin";
				return ResponseEntity.ok(rt);
			} else {
				liudid.setLastActivityDateAndTime(new Date());
				loggedInUserDetailsInDatabaseDao.save(liudid);
				rt.message="user";
				return ResponseEntity.ok(rt);
			}
		} else {
			rt.message="noAuthority";
			return ResponseEntity.ok(rt);
		}
		
//		return ResponseEntity.ok(rt);
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
		if(token!=null) {
//			for(int i=0;i<loggedInUsersDetailsList.size();i++) {
//				if(loggedInUsersDetailsList.get(i).authToken.equals(token)) {
//					System.out.println("Logging out - fname= "+loggedInUsersDetailsList.get(i).getLoggedInUserObject().getFirstName()+" username= "+loggedInUsersDetailsList.get(i).getLoggedInUserObject().getUsername());
//					loggedInUsersDetailsList.remove(i);
//				}
//			}
			if(loggedInUserDetailsInDatabaseDao.findByAuthToken(token) != null) {
				loggedInUserDetailsInDatabaseDao.deleteById(loggedInUserDetailsInDatabaseDao.findByAuthToken(token).getLoggedInUserDetailsId());
			}
		}
		return new ResponseToken("Logged Out Successfully!", null);
	}
	
	
	@RequestMapping(value = "/test", method = RequestMethod.GET /* , headers = { "AUTH_KEY" } */)
	@ResponseBody
	public String test(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Test Running, to print logged in users");
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
		return "run ok! List of logged in users printed in the console";
	}
}
