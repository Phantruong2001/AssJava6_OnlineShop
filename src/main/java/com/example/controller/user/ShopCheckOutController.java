package com.example.controller.user;

import java.util.Collection;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.helper.AccountTypeEnum;
import com.example.helper.DateHelper;
import com.example.helper.MailTypeEnum;
import com.example.helper.PayTypeEnum;
import com.example.helper.StatusTypeEnum;
import com.example.model.Order;
import com.example.model.OrderDetail;
import com.example.model.Product;
import com.example.model.SendMail;
import com.example.model.User;
import com.example.service.CartService;
import com.example.service.MailerService;
import com.example.service.OrderDetailService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import com.example.service.SendMailService;
import com.example.service.SessionService;
import com.example.service.UserService;
import com.example.util.CheckOut;
import com.example.util.Item;
import com.example.util.MailBody;
import com.example.util.MailInfo;

@Controller
@RequestMapping("shop/check-out")
public class ShopCheckOutController {
	Logger logger = LoggerFactory.getLogger(ShopCheckOutController.class);
	
	@Autowired
	CartService cartService;

	@Autowired
	OrderService orderService;

	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	ProductService productService;

	@Autowired
	SessionService session;

	@Autowired
	UserService userService;

	@Autowired
	SendMailService sendMailService;

	@Autowired
	MailerService mailerService;

	@RequestMapping(value = "")
	public String checkOut(Model model, CheckOut checkOut) {
		model.addAttribute("title", "Shop Check Out");

		User user = session.get("user");
		if (user != null) {
			checkOut.setFullname(user.getFullName());
			checkOut.setEmail(user.getEmail());
			checkOut.setAddress(user.getAddress());
		}
		model.addAttribute("checkOut", checkOut);

		Collection<Item> items = cartService.getItems();
		int quantity = cartService.getQty();
		double totail = cartService.getAmount();
		model.addAttribute("cart", items);
		model.addAttribute("quantity", quantity);
		model.addAttribute("totail", totail);
		logger.info("checkOut");
		return "/user/cart/check-out";
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ModelAndView checkOutSubmit(@ModelAttribute("checkOut") CheckOut checkOut, ModelMap model)
			throws MessagingException {
		Order order = new Order();
		order.setId("");
		order.setDeliveryAddress(checkOut.getAddress());
		order.setStatus(StatusTypeEnum.WAITING.toString());
		order.setPay(PayTypeEnum.UNPAID.toString());
		User user = session.get("user");
		if (user != null) {
			order.setUser(user);
		} else {
			User userOdl = new User();
			userOdl.setId("");
			userOdl.setFullName(checkOut.getFullname());
			userOdl.setEmail(checkOut.getEmail());
			userOdl.setAddress(checkOut.getAddress());
			userOdl.setBirthDay(DateHelper.toDate("2000-12-12"));
			userOdl.setAccountType(AccountTypeEnum.ACCOUNT_OFTEN.toString());
			userOdl.setPassword("OnlineShop123");
			userOdl.setRole(false);
			User userOrder = userService.saveOrUpdate(userOdl);
			MailBody mailBody = new MailBody(checkOut.getEmail(), "OnlineShop123",
					"http://localhost:8080/shop/account/login");
			String body = mailBody.register();
			MailInfo mailInfo = new MailInfo(checkOut.getEmail(), MailTypeEnum.REGISTER.toString(), body);
			mailerService.send(mailInfo);

			SendMail sendMail = new SendMail();
			sendMail.setType(MailTypeEnum.REGISTER.toString());
			sendMail.setEmailTo(checkOut.getEmail());
			sendMail.setSubject(MailTypeEnum.REGISTER.toString());
			sendMail.setCreateDay(DateHelper.dateNow());
			sendMailService.save(sendMail);

			order.setUser(userOrder);
		}
		Order orderOdl = orderService.saveOrUpdate(order);
		Collection<Item> items = cartService.getItems();
		for (Item item : items) {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setPrice(item.getPrice());
			orderDetail.setQuantity(item.getQuantity());
			orderDetail.setTotail(item.getPrice() * item.getQuantity());
			Product product = productService.findById(item.getProduct_id());
			orderDetail.setProduct(product);
			orderDetail.setOrder(orderOdl);
			orderDetailService.saveOrUpdate(orderDetail);
		}
		cartService.clear();
		model.addAttribute("message", "Thank you for your order, please check your email for login information !");
		logger.info("checkOutSubmit");
		return new ModelAndView("forward:/shop", model);
	}
}
