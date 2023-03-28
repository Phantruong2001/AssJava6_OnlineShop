package com.example.controller.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.helper.AccountTypeEnum;
import com.example.helper.StringHelper;
import com.example.model.User;
import com.example.service.SessionService;
import com.example.service.UserService;

@Controller
@RequestMapping("userpage")
public class UserController {
	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;

	@Autowired
	SessionService session;

	@RequestMapping(value = "/form")
	public String userForm(Model model, User user) {
		model.addAttribute("title", "User Form Page Admin");
		model.addAttribute("user", new User());
		logger.info("userForm");
		return "/admin/user/form";
	}

	@RequestMapping(value = "/form/create", method = RequestMethod.POST)
	public ModelAndView createUser(@Valid @ModelAttribute(name = "user") User user, BindingResult result,
			ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("error", "Data entry error !");
			logger.warn("Data entry error !");
			return new ModelAndView("/admin/user/form");
		}
		if (!StringHelper.equals(user.getId(), "")) {
			model.addAttribute("error", "User already exists, please select update !");
			logger.warn("User already exists, please select update !");
			return new ModelAndView("/admin/user/form");
		}
		if (userService.findByEmail(user.getEmail()) != null) {
			user.setEmail("");
			model.addAttribute("error", "User email already exists !");
			logger.warn("User email already exists !");
			return new ModelAndView("/admin/user/form");
		}
		user.setAccountType(AccountTypeEnum.ACCOUNT_OFTEN.toString());
		user.setPassword("OnlineShop123456789");
		userService.saveOrUpdate(user);
		model.addAttribute("message", "Create new user to public !");
		logger.info("Create new user to public");
		return new ModelAndView("forward:/userpage/list", model);
	}

	@RequestMapping(value = "/form/update", method = RequestMethod.POST)
	public ModelAndView updateUser(@Valid @ModelAttribute(name = "user") User user, BindingResult result,
			ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("error", "Data entry error !");
			logger.warn("Data entry error !");
			return new ModelAndView("/admin/user/form");
		}
		if (StringHelper.equals(user.getId(), "")) {
			model.addAttribute("error", "User already exists, please select create !");
			logger.warn("User already exists, please select create !");
			return new ModelAndView("/admin/user/form");
		}
		user.setAccountType(user.getAccountType());
		user.setPassword(user.getPassword());
		userService.saveOrUpdate(user);
		model.addAttribute("message", "Update new user to public !");
		logger.info("Update new user to public");
		return new ModelAndView("forward:/userpage/list", model);
	}

	@RequestMapping(value = "/edit")
	public String editUser(@RequestParam(name = "id") String id, ModelMap model) {
		model.addAttribute("title", "User Form Page Admin");
		User user = userService.findById(id);
		model.addAttribute("user", user);
		logger.info("editUser");
		return "/admin/user/form";
	}

	@RequestMapping(value = "/delete")
	public ModelAndView deleteUser(@RequestParam(name = "id") String id, ModelMap model) {
		userService.deleteById(id);
		model.addAttribute("message", "Deleted user successfully !");
		logger.info("Deleted user successfully");
		return new ModelAndView("forward:/userpage/list", model);
	}

	@RequestMapping(value = "/list")
	public String sortAndPage(Model model, @RequestParam(name = "field") Optional<String> field,
			@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size,
			@RequestParam(name = "userpagekeywords", defaultValue = "") Optional<String> userpagekeywords) {
		model.addAttribute("title", "User List Page Admin");

		String userpagekeyword = userpagekeywords.orElse(session.get("userpagekeywords"));
		session.set("userpagekeywords", userpagekeyword);

		Sort sort = Sort.by(Direction.DESC, field.orElse("id"));
		Pageable pageable = PageRequest.of(page.orElse(1) - 1, size.orElse(5), sort);
		Page<User> resultPage = userService.findAllByFullNameLike("%" + userpagekeyword + "%", pageable);

		int totalPages = resultPage.getTotalPages();
		int startPage = Math.max(1, page.orElse(1) - 2);
		int endPage = Math.min(page.orElse(1) + 2, totalPages);
		if (totalPages > 5) {
			if (endPage == totalPages)
				startPage = endPage - 5;
			else if (startPage == 1)
				endPage = startPage + 5;
		}
		List<Integer> pageNumbers = IntStream.rangeClosed(startPage, endPage).boxed().collect(Collectors.toList());

		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("field", field.orElse("id"));
		model.addAttribute("resultPage", resultPage);
		logger.info("sortAndPage");
		return "/admin/user/list";
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> viewUser(@PathVariable(name = "id") String id, ModelMap model) {
		User user = userService.findById(id);
		logger.info("viewUser");
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
}
