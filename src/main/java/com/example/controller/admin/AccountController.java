package com.example.controller.admin;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.helper.AccountTypeEnum;
import com.example.helper.DateHelper;
import com.example.helper.IntegerHelper;
import com.example.helper.MailTypeEnum;
import com.example.helper.StringHelper;
import com.example.model.SendMail;
import com.example.model.User;
import com.example.service.CookieService;
import com.example.service.MailerService;
import com.example.service.SendMailService;
import com.example.service.SessionService;
import com.example.service.UserService;
import com.example.util.ForgotPassword;
import com.example.util.Login;
import com.example.util.MailBody;
import com.example.util.MailInfo;
import com.example.util.Register;

@Controller
@RequestMapping("accountpage")
public class AccountController {
	Logger logger = LoggerFactory.getLogger(AccountController.class);

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

	@RequestMapping(value = "/confirm-password")
	public String confirmPasswordPage(Model model, @RequestParam(name = "sendMailId") String id) {
		model.addAttribute("title", "Confirm Password Page Admin");
		SendMail sendMail = sendMailService.findById(id);
		ForgotPassword forgotPassword = new ForgotPassword();
		forgotPassword.setSendMailId(sendMail.getId());
		forgotPassword.setEmail(sendMail.getEmailTo());
		forgotPassword.setCode("#####");
		forgotPassword.setNewPassword("**********");
		forgotPassword.setConfimPassword("**********");
		model.addAttribute("forgotPassword", forgotPassword);
		logger.info("confirmPasswordPage");
		return "/admin/account/confirm-password";
	}

	@RequestMapping(value = "/confirm-password/submit", method = RequestMethod.POST)
	public ModelAndView confirmPassword(ModelMap model,
			@Valid @ModelAttribute(name = "forgotPassword") ForgotPassword forgotPassword, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("error", "Data entry error !");
			logger.warn("Data entry error !");
			return new ModelAndView("/admin/account/confirm-password");
		}

		if (!StringHelper.equals(forgotPassword.getNewPassword(), forgotPassword.getConfimPassword())) {
			model.addAttribute("error", "Password incorrect !");
			logger.warn("Password incorrect !");
			return new ModelAndView("/admin/account/confirm-password");
		}

		SendMail sendMail = sendMailService.findById(forgotPassword.getSendMailId());
		if (!StringHelper.equals(sendMail.getCode(), forgotPassword.getCode())) {
			model.addAttribute("error", "Verification code does not match !");
			logger.warn("Verification code does not match !");
			return new ModelAndView("/admin/account/confirm-password");
		}

		if (!DateHelper.checkTime15P(sendMail.getExpirationDate(), DateHelper.dateNow())) {
			model.addAttribute("error", "The verification code has expired !");
			logger.warn("The verification code has expired !");
			return new ModelAndView("/admin/account/confirm-password");
		}

		User user = userService.findByEmail(forgotPassword.getEmail());
		user.setPassword(forgotPassword.getNewPassword());
		userService.saveOrUpdate(user);
		model.addAttribute("message", "You have successfully changed your password !");
		logger.info("You have successfully changed your password !");
		logger.info("confirmPassword-forward:/accountpage/login");
		return new ModelAndView("forward:/accountpage/login", model);
	}

	@RequestMapping(value = "/forgot-password")
	public String forgotPasswordPage(Model model, ForgotPassword forgotPassword) {
		model.addAttribute("title", "Forgot Password Page Admin");
		model.addAttribute("forgotPassword", forgotPassword);
		logger.info("forgotPasswordPage");
		return "/admin/account/forgot-password";
	}

	@RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
	public ModelAndView forgotPassword(ModelMap model, @RequestParam(name = "email") String email)
			throws MessagingException {
		User user = userService.findByEmail(email);
		if (user == null) {
			model.addAttribute("error", "Email account not registered !");
			logger.warn("Email account not registered !");
			return new ModelAndView("/admin/account/forgot-password");
		}
		if (!user.isRole()) {
			model.addAttribute("error", "You are not admin !");
			logger.warn("You are not admin !");
			return new ModelAndView("/admin/account/forgot-password");
		}
		int code = IntegerHelper.random5Number();
		MailBody mailBody = new MailBody(email, String.valueOf(code));
		String body = mailBody.forgotPassword();
		MailInfo mailInfo = new MailInfo(email, MailTypeEnum.FORGOT_PASSWORD.toString(), body);
		mailerService.send(mailInfo);

		SendMail sendMail = new SendMail();
		sendMail.setType(MailTypeEnum.FORGOT_PASSWORD.toString());
		sendMail.setEmailTo(email);
		sendMail.setSubject(MailTypeEnum.FORGOT_PASSWORD.toString());
		sendMail.setCode(String.valueOf(code));
		sendMail.setCreateDay(DateHelper.dateNow());
		sendMail.setExpirationDate(DateHelper.addMINUTE(15));
		SendMail send = sendMailService.save(sendMail);
		model.addAttribute("message", "Password change code has been sent to email " + email
				+ ", note the code is only valid for 15 minutes");
		logger.info("Password change code has been sent to email " + email
				+ ", note the code is only valid for 15 minutes");
		return new ModelAndView("forward:/accountpage/confirm-password?sendMailId=" + send.getId(), model);
	}

	@RequestMapping(value = "/register")
	public String registerPage(Model model, Register register) {
		model.addAttribute("title", "Register Page Admin");
		model.addAttribute("register", register);
		logger.info("registerPage");
		return "/admin/account/register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView register(@Valid @ModelAttribute(name = "register") Register register, BindingResult result,
			ModelMap model) throws MessagingException {
		if (result.hasErrors()) {
			model.addAttribute("error", "Data entry error !");
			logger.warn("Data entry error !");
			return new ModelAndView("/admin/account/register");
		}
		if (userService.findByEmail(register.getEmail()) != null) {
			register.setEmail("");
			model.addAttribute("error", "User email already exists !");
			logger.warn("User email already exists !");
			return new ModelAndView("/admin/account/register");
		}
		User user = new User();
		user.setId("");
		user.setEmail(register.getEmail());
		user.setFullName(register.getFullName());
		user.setAddress(register.getAddress());
		user.setBirthDay(register.getBirthDay());
		user.setAccountType(AccountTypeEnum.ACCOUNT_OFTEN.toString());
		user.setPassword("OnlineShop123");
		user.setRole(true);
		userService.saveOrUpdate(user);

		MailBody mailBody = new MailBody(register.getEmail(), "OnlineShop123",
				"http://localhost:8080/accountpage/login");
		String body = mailBody.register();
		MailInfo mailInfo = new MailInfo(user.getEmail(), MailTypeEnum.REGISTER.toString(), body);
		mailerService.send(mailInfo);

		SendMail sendMail = new SendMail();
		sendMail.setType(MailTypeEnum.REGISTER.toString());
		sendMail.setEmailTo(user.getEmail());
		sendMail.setSubject(MailTypeEnum.REGISTER.toString());
		sendMail.setCreateDay(DateHelper.dateNow());
		sendMailService.save(sendMail);

		model.addAttribute("message",
				"You have successfully created an account, please check your email for login information !");
		logger.info("You have successfully created an account, please check your email for login information !");
		return new ModelAndView("forward:/accountpage/login", model);
	}

	@RequestMapping(value = "/login")
	public String loginPage(Model model, Login login) {
		if (session.get("user") != null) {
			logger.info("loginPage-forward:/homepage");
			return "forward:/homepage";
		}
		model.addAttribute("title", "Login Admin Page");
		model.addAttribute("login", login);
		model.addAttribute("error", "Only admin account has access !");
		cookie.remove("email");
		cookie.remove("role");
		session.remove("user");
		logger.info("loginPage");
		return "/admin/account/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(ModelMap model, @Valid @ModelAttribute(name = "login") Login login,
			BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("error", "Data entry error !");
			logger.warn("Data entry error !");
			return new ModelAndView("/admin/account/login");
		}
		User user = userService.checkLogin(login.getEmail(), login.getPassword());
		if (user == null) {
			model.addAttribute("error", "Error email or password !");
			logger.warn("Error email or password !");
			return new ModelAndView("/admin/account/login");
		} else if (!user.isRole()) {
			model.addAttribute("error", "Only admin account has access !");
			logger.warn("Only admin account has access !");
			return new ModelAndView("/admin/account/login");
		}
		if (login.isRememberme()) {
			cookie.add("email", user.getEmail(), 24 * 60 * 60);
			cookie.add("role", String.valueOf(user.isRole()), 24 * 60 * 60);
		} else {
			cookie.add("email", user.getEmail(), 24);
			cookie.add("role", String.valueOf(user.isRole()), 24);
		}
		session.set("user", user);
		String uri = session.get("security-uri");
		logger.info("Login succeed");
		if (uri != null) {
			session.remove("security-uri");
			logger.info("login-redirect:" + uri);
			return new ModelAndView("redirect:" + uri);
		}
		logger.info("login-redirect:/homepage");
		return new ModelAndView("redirect:/homepage", model);
	}

	@RequestMapping(value = "/logout")
	public String logout(Model model) {
		cookie.remove("email");
		cookie.remove("role");
		session.remove("user");
		logger.info("logout-redirect:/accountpage/login");
		return "redirect:/accountpage/login";
	}

}
