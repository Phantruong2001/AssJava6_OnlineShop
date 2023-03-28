package com.example.controller.user;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.model.Product;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.SessionService;
import com.example.util.Item;

@Controller
@RequestMapping("shop/my-order")
public class ShopMyOrderController {
	Logger logger = LoggerFactory.getLogger(ShopMyOrderController.class);
	
	@Autowired
	ProductService productService;

	@Autowired
	CartService cartService;

	@Autowired
	SessionService session;

	@RequestMapping(value = "")
	public String myOrder(Model model) {
		model.addAttribute("title", "Shop My Order");
		Collection<Item> items = cartService.getItems();
		int quantity = cartService.getQty();
		double totail = cartService.getAmount();
		session.set("cartQuantity", quantity);
		model.addAttribute("cart", items);
		model.addAttribute("quantity", quantity);
		model.addAttribute("totail", totail);
		logger.info("myOrder");
		return "/user/cart/my-order";
	}

	@RequestMapping(value = "/add_to_cart/{id}")
	public ResponseEntity<Product> viewAddToCart(@PathVariable(name = "id") String id, ModelMap model) {
		Product product = productService.findById(id);
		logger.info("viewAddToCart");
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/add")
	public ModelAndView addItemCart(@RequestParam(name = "id") String id, ModelMap model) {
		cartService.add(id);
		logger.info("addItemCart");
		return new ModelAndView("redirect:/shop/my-order", model);
	}

	@RequestMapping(value = "/remove")
	public ModelAndView removeItemCart(@RequestParam(name = "id") String id, ModelMap model) {
		cartService.remove(id);
		logger.info("removeItemCart");
		return new ModelAndView("redirect:/shop/my-order", model);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateItemCart(@RequestParam("id") String id, @RequestParam("quantity") Integer quantity,
			ModelMap model) {
		cartService.update(id, quantity);
		logger.info("updateItemCart");
		return new ModelAndView("redirect:/shop/my-order", model);
	}
}
