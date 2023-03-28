package com.example.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("homepage")
public class HomeController {
	Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "")
	public String homePage(Model model) {
		logger.info("homePage");
		model.addAttribute("title", "Home Page Admin");
		return "/admin/home/home";
	}
}
