package com.Medicare.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BasicControllers {
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}

}
