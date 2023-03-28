package com.example.controller.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.helper.StringHelper;
import com.example.model.Category;
import com.example.model.Product;
import com.example.service.CategoryService;
import com.example.service.ProductService;

@Controller
@RequestMapping("shop")
public class ShopController {
	Logger logger = LoggerFactory.getLogger(ShopController.class);
	
	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;

	@RequestMapping(value = "/shop-now")
	public String shopNow(@RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "category", defaultValue = "") String category, Model model) {

		model.addAttribute("title", "Shop");
		Sort sort = Sort.by(Direction.DESC, "id");
		Pageable pageable = PageRequest.of(page.orElse(1) - 1, 18, sort);
		Page<Product> resultPage;
		if (StringHelper.equals(category, ""))
			resultPage = productService.findAll(pageable);
		else
			resultPage = productService.findAllByCategoryId(category, pageable);
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
		model.addAttribute("resultPage", resultPage);
		logger.info("shopNow");
		return "/user/shop/shop-now";
	}

	@RequestMapping(value = "/deltail")
	public String deltail(@RequestParam(name = "id") String id, ModelMap model) {
		model.addAttribute("title", "Shop Deltail");
		Product product = productService.findById(id);
		model.addAttribute("product", product);
		logger.info("deltail");
		return "/user/shop/deltail";
	}

	@ModelAttribute("categories")
	public List<Category> listCategory() {
		List<Category> list = categoryService.findAll();
		return list;
	}
}
