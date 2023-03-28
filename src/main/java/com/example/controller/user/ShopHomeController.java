package com.example.controller.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.model.Product;
import com.example.service.ProductService;

@Controller
@RequestMapping("shop")
public class ShopHomeController {
	Logger logger = LoggerFactory.getLogger(ShopHomeController.class);

	@Autowired
	ProductService productService;

	@RequestMapping(value = "")
	public String shop(Model model) {
		model.addAttribute("title", "Shop Home");
		logger.info("shop");
		return "/user/home/home";
	}

	@ModelAttribute("listProduct")
	public List<Product> listProduct() {
		List<Product> list = productService.findByActive(true);
		logger.info("listProduct");
		return list;
	}

	@ModelAttribute("listProductFavority")
	public List<Product> listProductFavority() {
		List<Product> list = productService.findByFavorityTop10();
		logger.info("listProductFavority");
		return list;
	}

	@ModelAttribute("listProductShare")
	public List<Product> listProductShare() {
		List<Product> list = productService.findByShareTop10();
		logger.info("listProductShare");
		return list;
	}
}
