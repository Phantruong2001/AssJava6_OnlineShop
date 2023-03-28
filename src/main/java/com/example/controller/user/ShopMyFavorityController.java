package com.example.controller.user;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.helper.StringHelper;
import com.example.model.Category;
import com.example.model.Favority;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.CategoryService;
import com.example.service.FavorityService;
import com.example.service.ProductService;
import com.example.service.SessionService;

@Controller
@RequestMapping("shop/my-favorites")
public class ShopMyFavorityController {
	Logger logger = LoggerFactory.getLogger(ShopMyFavorityController.class);

	@Autowired
	ProductService productService;

	@Autowired
	FavorityService favorityService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	SessionService session;

	@ModelAttribute("categories")
	public List<Category> listCategory() {
		List<Category> list = categoryService.findAll();
		return list;
	}

	@RequestMapping(value = "")
	public String myFavorites(Model model, @RequestParam(name = "category", defaultValue = "") String category) {
		model.addAttribute("title", "Shop My Favority");
		List<Product> list;
		User user = session.get("user");
		if (user != null) {
			if (StringHelper.equals(category, "")) {
				list = productService.findFavorityByUserId(user.getId());
			} else {
				list = productService.findFavorityByUserIdAndCateId(user.getId(), category);
			}
		} else
			list = null;
		model.addAttribute("list", list);
		model.addAttribute("category", category);
		logger.info("myFavorites");
		return "/user/account/my-favorites";
	}

	@RequestMapping(value = "/like/{id}")
	public ResponseEntity<Product> viewLike(@PathVariable(name = "id") String id, ModelMap model) {
		Product product = productService.findById(id);
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/liked")
	public ModelAndView liked(@RequestParam(name = "id") String id, ModelMap model) {
		User user = session.get("user");
		Product product = productService.findById(id);
		if (favorityService.findByUserIdAndProductId(user.getId(), id) != null) {
			model.addAttribute("message", "You like this product !");
			logger.warn("You like this product !");
			return new ModelAndView("forward:/shop/my-favorites", model);
		}
		Favority favority = new Favority();
		favority.setUser(user);
		favority.setProduct(product);
		favority.setCreateDay(new Date());
		favorityService.save(favority);
		model.addAttribute("message", "Product added to favorites !");
		logger.info("Product added to favorites !");
		return new ModelAndView("forward:/shop/my-favorites", model);
	}

	@RequestMapping(value = "/disLike/{id}")
	public ResponseEntity<Product> viewDisLike(@PathVariable(name = "id") String id, ModelMap model) {
		Product product = productService.findById(id);
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/disLiked")
	public ModelAndView disLiked(@RequestParam(name = "id") String id, ModelMap model) {
		User user = session.get("user");
		Favority favority = favorityService.findByUserIdAndProductId(user.getId(), id);
		favorityService.deleteById(favority.getId());
		model.addAttribute("message", "Disliked the product !");
		logger.info("Disliked the product !");
		return new ModelAndView("forward:/shop/my-favorites", model);
	}
}
