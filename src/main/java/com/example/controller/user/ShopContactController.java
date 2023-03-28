package com.example.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("shop/contact-us")
public class ShopContactController {
	@RequestMapping(value = "")
	public String contact(Model model) {
		model.addAttribute("title", "Shop Contact Us");
		return "/user/contact/contact-us";
	}
}
