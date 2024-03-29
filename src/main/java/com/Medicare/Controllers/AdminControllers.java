package com.Medicare.Controllers;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.Medicare.Beans.LoggedInUserDetails;
import com.Medicare.DAO.CartDAO;
import com.Medicare.DAO.ContactDAO;
import com.Medicare.DAO.ItemCategoryDAO;
import com.Medicare.DAO.ItemDAO;
import com.Medicare.DAO.LoggedInUserDetailsInDatabaseDAO;
import com.Medicare.DAO.PurchaseHistoryDAO;
import com.Medicare.DAO.UserDAO;
import com.Medicare.Entity.Item;
import com.Medicare.Entity.ItemCategory;
import com.Medicare.Entity.LoggedInUserDetailsInDatabase;
import com.Medicare.Entity.PurchaseHistory;
import com.Medicare.Entity.User;

@Controller
@RequestMapping(path = "/admin")
public class AdminControllers {
	
	
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
	
	String baseURL="http://localhost:8084";
//	String baseURL="http://54.157.10.242:8084";
	
	public String adminAuthentication(HttpServletRequest request) {
		System.out.println("Checking Admin Authority");
		String token=request.getHeader("AUTH_TOKEN");
		System.out.println("Checking Admin Authority :: Token ="+token);
		if(token==null)
			return "AuthenticationFailure";
//		for(LoggedInUserDetails ld:loggedInUsersDetailsList) {
//			if(ld.authToken.equals(token)) {
//				if(ld.loggedInUserObject.getAuthority().equals("ROLE_Admin")) {
//					System.out.println("AdminAuthenticated");
//					return "AdminAuthenticated";
//				}	
//			}
//		}
		LoggedInUserDetailsInDatabase liudid = loggedInUserDetailsInDatabaseDao.findByAuthToken(token);
		if(liudid != null) {
			if(liudid.getLoggedInUserObject().getAuthority().equals("ROLE_Admin")) {
				liudid.setLastActivityDateAndTime(new Date());
				loggedInUserDetailsInDatabaseDao.save(liudid);
				return "AdminAuthenticated";
			}
		}
		return "AuthenticationFailure";
	}
	
	
	
//	For ALL HANDLERS FOLLOW FOLLOWING TEMPLATE
//	
//	
//	String authority_authentication=adminAuthentication(request);
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
	
	
	
	
	
	@RequestMapping(path = "/getAllActivatedItems",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public List<Item> getallactivateditems(HttpServletRequest request,HttpServletResponse response) {
		
		String authority_authentication=adminAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
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
	
	
	@RequestMapping(path = "/getAllItems",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public List<Item> getallItems(HttpServletRequest request,HttpServletResponse response) {
		
		String authority_authentication=adminAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			return null;
		} else {
			List<Item> ItemsList = new ArrayList<Item>();
			itemDao.findAll().forEach(item -> {
						ItemsList.add(item);
					});
			System.out.println(ItemsList);
			return ItemsList;
		}
	}
	
	@RequestMapping(path = "/getUserDetails",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public User getuserdetails(HttpServletRequest request,HttpServletResponse response) {
		String authority_authentication=adminAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from Admin getuserdetails");
			return null;
		} else {
			String token=request.getHeader("AUTH_TOKEN");
			User returnUser=new User();
//			for(LoggedInUserDetails ld : loggedInUsersDetailsList) {
//				if(ld.authToken.equals(token))
//					returnUser=ld.getLoggedInUserObject();
//			}
			returnUser=loggedInUserDetailsInDatabaseDao.findByAuthToken(token).getLoggedInUserObject();
			System.out.println("Returning User from Admin getuserdetails");
			return returnUser;
		}
	}
	
	@RequestMapping(path = "/activateDeactivateItem",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public ResponseToken activateDeactivateItem(@RequestBody int itemId,HttpServletRequest request,HttpServletResponse response) {
		String authority_authentication=adminAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from Admin activateDeactivateItem");
			return null;
		} else {
			System.out.println("Item Id for Activate/De-activate - "+itemId);
			Item item = itemDao.findByItemId(itemId);
			ResponseToken rt= new ResponseToken("Item Status Not Proper In DB",null);
			
			if(item.getItemStatus().equals("active")) {
				item.setItemStatus("deactivated");
				itemDao.save(item);
				System.out.println("Item Id - "+item.getItemId()+" De-Activated");
				rt.message="Item Id - "+item.getItemId()+" De-Activated";
			} else if(item.getItemStatus().equals("deactivated")) {
				item.setItemStatus("active");
				itemDao.save(item);
				System.out.println("Item Id - "+item.getItemId()+" Activated");
				rt.message="Item Id - "+item.getItemId()+" Activated";
			}
			
			System.out.println("Returning Active/Deactive Success from Admin activateDeactivateItem");
			return rt;
		}
	}
	
	@RequestMapping(path = "/getAllCategories",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public List<ItemCategory> getAllCategories(HttpServletRequest request,HttpServletResponse response) {
		String authority_authentication=adminAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from Admin getAllCategories()");
			return null;
		} else {
			List<ItemCategory> itemCategoryList = new ArrayList<ItemCategory>();
			itemCategoryDao.findAll().forEach(itemCategory->{itemCategoryList.add(itemCategory);});
			System.out.println("Returning all Item Categories from Admin getAllCategories()");
			return itemCategoryList;
		}
	}
	
	@RequestMapping(path = "/addNewItem",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	@CrossOrigin("*")
	public ResponseToken addNewItem(@ModelAttribute Item item,@RequestParam(name = "imageFiles" , required = false) MultipartFile[] imageFiles ,HttpServletRequest request,HttpServletResponse response) {
		String authority_authentication=adminAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from Admin getAllCategories()");
			return null;
		} else {
			System.out.println("Adding New Item...");
			System.out.println(item);
			
			item.setItemStatus("active");
			itemDao.save(item);
			
			if(imageFiles==null) {
				System.out.println("No Image Found in request");
				System.out.println("Item Saved without Image");
			} else {
				String concatinatedItemImageName = "";
				String concatinatedItemImageType = "";
				String concatinatedItemImageUrl = "";
				for(int i=0; i<imageFiles.length; i++) {
					System.out.println("Image Found in request");
					String imgname=item.getItemId()+item.getItemName()+"ImageNo"+i+"."+imageFiles[i].getContentType().split("/")[1];
					imgname = imgname.replace(" ", "");
					System.out.println("Generated Image Name="+imgname);
					concatinatedItemImageName = concatinatedItemImageName == "" ? imgname : concatinatedItemImageName + "<::||||::>" + imgname;
					concatinatedItemImageType = concatinatedItemImageType == "" ? imageFiles[i].getContentType() : concatinatedItemImageType + "<::||||::>" + imageFiles[i].getContentType();
					concatinatedItemImageUrl = concatinatedItemImageUrl == "" ? baseURL+"/UploadedItemImages/"+imgname : concatinatedItemImageUrl + "<::||||::>" + baseURL+"/UploadedItemImages/"+imgname;
					File file = new File(request.getRealPath(""));
					System.out.println("request.getRealPath(\"\") - "+request.getRealPath(""));
					System.out.println("file getAbsolutePath() "+file.getAbsolutePath());
					System.out.println("file getParent() "+file.getParent());
					System.out.println("file getPath() "+file.getPath());
					File pfile;
//					File pfile=new File(file.getParent()+"\\resources\\static\\UploadedItemImages\\"+imgname);
//					System Dependent Path Separator
					if(System.getProperty("os.name").toLowerCase().contains("Windows".toLowerCase())) {
//						For Windows
						pfile=new File(file.getParent()+File.separator+"resources"+File.separator+"static"+File.separator+"UploadedItemImages"+File.separator+imgname);
						System.out.println("pfile getAbsolutePath() "+pfile.getAbsolutePath());
						System.out.println("pfile getParent() "+pfile.getParent());
						System.out.println("pfile getPath() "+pfile.getPath());
					} else {
//						For Ubuntu
						pfile=new File("UploadFolder"+File.separator+"UploadedItemImages"+File.separator+imgname);
						System.out.println("pfile getAbsolutePath() "+pfile.getAbsolutePath());
						System.out.println("pfile getParent() "+pfile.getParent());
						System.out.println("pfile getPath() "+pfile.getPath());
					}
					
					
					System.out.println("Uploading Image to the server");
					try {
//						pfile.createNewFile();
//						imageFiles[i].transferTo(newFilePath);
						System.out.println("pfile getAbsolutePath() "+pfile.getAbsolutePath());
						System.out.println("pfile getParent() "+pfile.getParent());
						System.out.println("pfile getPath() "+pfile.getPath());
						Path newFilePath = Paths.get(pfile.getAbsolutePath());
						Files.write(newFilePath,imageFiles[i].getBytes());
					}
					catch(Exception e) {System.out.println(e.getMessage());e.printStackTrace();}
				}
				item.setItemImageName(concatinatedItemImageName);
				item.setItemImageType(concatinatedItemImageType);
				item.setItemImageUrl(concatinatedItemImageUrl);
				itemDao.save(item);
				System.out.println("Item Saved with Image");
			}
			
			return new ResponseToken("Item Added Successfully!", null);
		}
	}
	
	@RequestMapping(path = "/addNewCategory",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public ResponseToken addNewCategory(@RequestBody String categoryName, HttpServletRequest request,HttpServletResponse response) {
		String authority_authentication=adminAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from Admin addNewCategory()");
			return null;
		} else {
			ItemCategory category=new ItemCategory(0, categoryName);
			ItemCategory itemCategory=itemCategoryDao.save(category);
			System.out.println("New Category Added :: "+itemCategory);
			System.out.println("Returning message for new category addition from Admin addNewCategory()");
			return new ResponseToken("Category Added Successfully!!", null);
		}
	}
	
	
	@RequestMapping(path = "/getitembyId/{id}",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public Item getitembyId(@PathVariable(name = "id",required = true) int id,HttpServletRequest request,HttpServletResponse response) {
		String authority_authentication=adminAuthentication(request);
		
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
			System.out.println("Returning Item from Admin getitembyId()");
			return itemDao.findByItemId(id);
		}
	}
	
	
	@RequestMapping(path = "/updateItem",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	@CrossOrigin("*")
	public ResponseToken upadateItem(@ModelAttribute Item item,@RequestParam(name = "imageFiles" , required = false) MultipartFile[] imageFiles ,HttpServletRequest request,HttpServletResponse response) {
		String authority_authentication=adminAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from Admin getAllCategories()");
			return null;
		} else {
			System.out.println("Update Item...");
			System.out.println(item);
			Item updatedItem=itemDao.findByItemId(item.getItemId());
			updatedItem.setItemName(item.getItemName());
			updatedItem.setItemCategory(item.getItemCategory());
			updatedItem.setItemCompany(item.getItemCompany());
			updatedItem.setItemQuantity(item.getItemQuantity());
			updatedItem.setUnitPrice(item.getUnitPrice());
			
			itemDao.save(updatedItem);
			
			if(imageFiles==null) {
				System.out.println("No Image Found in request");
				System.out.println("Item Saved without Image");
			} else {
//				Removing Old Image Files
				if(updatedItem.getItemImageName() != null) {
					System.out.println("Removing Old Image Files");
					String[] fileNamesArray = updatedItem.getItemImageName().split("\\<\\:\\:\\|\\|\\|\\|\\:\\:\\>");
					for(int i=0; i < fileNamesArray.length; i++) {
						File file = new File(request.getRealPath(""));
						System.out.println("request.getRealPath(\"\") - "+request.getRealPath(""));
						System.out.println("file getAbsolutePath() "+file.getAbsolutePath());
						System.out.println("file getParent() "+file.getParent());
						System.out.println("file getPath() "+file.getPath());
//						File pfile = new File(file.getParent()+"\\resources\\static\\UploadedItemImages\\"+fileNamesArray[i]);
//						pfile.delete();
						File pfile;
//						System Dependent Path Separator
						if(System.getProperty("os.name").toLowerCase().contains("Windows".toLowerCase())) {
							pfile = new File(file.getParent()+File.separator+"resources"+File.separator+"static"+File.separator+"UploadedItemImages"+File.separator+fileNamesArray[i]);
							System.out.println("pfile getAbsolutePath() "+pfile.getAbsolutePath());
							System.out.println("pfile getParent() "+pfile.getParent());
							System.out.println("pfile getPath() "+pfile.getPath());
							pfile.delete();
						} else {
							pfile = new File("UploadFolder"+File.separator+"UploadedItemImages"+File.separator+fileNamesArray[i]);
							System.out.println("pfile getAbsolutePath() "+pfile.getAbsolutePath());
							System.out.println("pfile getParent() "+pfile.getParent());
							System.out.println("pfile getPath() "+pfile.getPath());
							pfile.delete();
						}
					}
				}
				String concatinatedItemImageName = "";
				String concatinatedItemImageType = "";
				String concatinatedItemImageUrl = "";
				for(int i=0; i<imageFiles.length; i++) {
					System.out.println("Image Found in request");
					String imgname=item.getItemId()+item.getItemName()+"ImageNo"+i+"."+imageFiles[i].getContentType().split("/")[1];
					imgname = imgname.replace(" ", "");
					System.out.println("Generated Image Name="+imgname);
					concatinatedItemImageName = concatinatedItemImageName == "" ? imgname : concatinatedItemImageName + "<::||||::>" + imgname;
					concatinatedItemImageType = concatinatedItemImageType == "" ? imageFiles[i].getContentType() : concatinatedItemImageType + "<::||||::>" + imageFiles[i].getContentType();
					concatinatedItemImageUrl = concatinatedItemImageUrl == "" ? baseURL+"/UploadedItemImages/"+imgname : concatinatedItemImageUrl + "<::||||::>" + baseURL+"/UploadedItemImages/"+imgname;
					File file = new File(request.getRealPath(""));
//					File pfile=new File(file.getParent()+"\\resources\\static\\UploadedItemImages\\"+imgname);
					File pfile;
//					System Dependent Path Separator
					if(System.getProperty("os.name").toLowerCase().contains("Windows".toLowerCase())) {
//						For Windows
						pfile=new File(file.getParent()+File.separator+"resources"+File.separator+"static"+File.separator+"UploadedItemImages"+File.separator+imgname);
						System.out.println("pfile getAbsolutePath() "+pfile.getAbsolutePath());
						System.out.println("pfile getParent() "+pfile.getParent());
						System.out.println("pfile getPath() "+pfile.getPath());
					} else {
//						For Ubuntu
						pfile=new File("UploadFolder"+File.separator+"UploadedItemImages"+File.separator+imgname);
						System.out.println("pfile getAbsolutePath() "+pfile.getAbsolutePath());
						System.out.println("pfile getParent() "+pfile.getParent());
						System.out.println("pfile getPath() "+pfile.getPath());
					}
					

					System.out.println("Uploading Image to the server");
					try {
//						pfile.createNewFile();
//						imageFiles[i].transferTo(pfile);
						System.out.println("pfile getAbsolutePath() "+pfile.getAbsolutePath());
						System.out.println("pfile getParent() "+pfile.getParent());
						System.out.println("pfile getPath() "+pfile.getPath());
						Path newFilePath = Paths.get(pfile.getAbsolutePath());
						Files.write(newFilePath,imageFiles[i].getBytes());
					}
					catch(Exception e) {System.out.println(e.getMessage());e.printStackTrace();}
				}
				updatedItem.setItemImageName(concatinatedItemImageName);
				updatedItem.setItemImageType(concatinatedItemImageType);
				updatedItem.setItemImageUrl(concatinatedItemImageUrl);
				itemDao.save(updatedItem);
				System.out.println("Item Updated with Image");
			}
			
			return new ResponseToken("Item Updated Successfully!", null);
		}
	}
	
	@RequestMapping(path = "/getallusers",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public List<User> getallusers(HttpServletRequest request,HttpServletResponse response) {
		String authority_authentication=adminAuthentication(request);
		
		if(authority_authentication.equals("AuthenticationFailure")) {
			try {
				response.sendError(401);
				System.out.println("AuthenticationFailure");
			} catch (Exception e) {
				System.out.println("AuthenticationFailure Catch");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning null from Admin getallusers()");
			return null;
		} else {
			return userDao.findByAuthority("ROLE_User");
		}
	}
	
	@RequestMapping(path = "/getPurchaseHistoryListForUser/{userId}",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin("*")
	public List<PurchaseHistory> getPurchaseHistoryListForUser(HttpServletRequest request,HttpServletResponse response,
			@PathVariable("userId") int userId) {
		String authority_authentication=adminAuthentication(request);
		
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
			return purchaseHistoryDao.findByUserId(userId);
		}
	}
	
	
		@RequestMapping(path = {"/test"})
		@ResponseBody
		public String test() {
			System.out.println(loggedInUsersDetailsList);
			System.out.println("Number of Logged In Users in the List="+loggedInUsersDetailsList.size());
			return "admin test run ok";
		}	

}
