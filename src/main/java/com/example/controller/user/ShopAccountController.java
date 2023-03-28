package com.example.controller.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.model.User;
import com.example.service.CookieService;
import com.example.service.MailerService;
import com.example.service.SendMailService;
import com.example.service.SessionService;
import com.example.service.UserService;
import com.example.util.Login;

@Controller
@RequestMapping("shop/account")
public class ShopAccountController {
	@Autowired
	UserService userService;

	@Autowired
	SendMailService sendMailService;

	@Autowired
	MailerService mailerService;

	@Autowired
	SessionService session;

	@Autowired
	CookieService cookie;

	@RequestMapping(value = "/login")
	public String loginPage(Model model, Login login) {
		model.addAttribute("title", "Shop Login");
		model.addAttribute("login", login);
		return "/user/account/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(ModelMap model, @Valid @ModelAttribute("login") Login login, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("error", "Data entry error !");
			return new ModelAndView("/user/account/login");
		}
		User user = userService.checkLogin(login.getEmail(), login.getPassword());
		if (user == null) {
			model.addAttribute("error", "Error email or password !");
			return new ModelAndView("/user/account/login");
		}
		if (login.isRememberme()) {
			cookie.add("email", user.getEmail(), 24 * 60 * 60);
			cookie.add("role", String.valueOf(user.isRole()), 24 * 60 * 60);
		} else {
			cookie.add("email", user.getEmail(), 24);
			cookie.add("role", String.valueOf(user.isRole()), 24);
		}
		session.set("user", user);
		model.addAttribute("message", "Login succeed !");
		return new ModelAndView("forward:/shop", model);
	}

	@RequestMapping(value = "/register")
	public String registerPage(Model model) {
		model.addAttribute("title", "Shop Register");
		return "/user/account/register";
	}

	@RequestMapping(value = "/forgot-password")
	public String forgotPasswordPage(Model model) {
		model.addAttribute("title", "Shop Forgot Password");
		return "/user/account/forgot-password";
	}

	@RequestMapping(value = "/logout")
	public String logout(Model model) {
		cookie.remove("email");
		cookie.remove("role");
		session.remove("user");
		return "redirect:/shop";
	}
}
