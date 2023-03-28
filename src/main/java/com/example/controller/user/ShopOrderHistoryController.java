package com.example.controller.user;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.User;
import com.example.service.OrderService;
import com.example.service.SessionService;

@Controller
@RequestMapping("shop/order-history")
public class ShopOrderHistoryController {
	Logger logger = LoggerFactory.getLogger(ShopOrderHistoryController.class);
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	SessionService session;
	
	@RequestMapping(value = "")
	public String orderHistory(Model model, @RequestParam(name = "field") Optional<String> field) {
		model.addAttribute("title", "Shop Order History");
		Sort sort = Sort.by(Direction.DESC, field.orElse("order.id"));
		User user = session.get("user");
		List<Object[]> list = null;
		if (user != null)
			list = orderService.findOrderByUserId(user.getId(), sort);
		model.addAttribute("list", list);
		logger.info("orderHistory");
		return "/user/cart/order-history";
	}
}
