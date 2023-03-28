package com.example.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("shop/about-us")
public class ShopAboutController {
	@RequestMapping(value = "")
	public String about(Model model) {
		model.addAttribute("title", "Shop About Us");
		return "/user/about/about-us";
	}
}
