package com.example.controller.user;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.helper.DateHelper;
import com.example.helper.MailTypeEnum;
import com.example.model.Product;
import com.example.model.SendMail;
import com.example.model.Share;
import com.example.model.User;
import com.example.service.MailerService;
import com.example.service.ProductService;
import com.example.service.SendMailService;
import com.example.service.SessionService;
import com.example.service.ShareService;
import com.example.service.UserService;
import com.example.util.MailBody;
import com.example.util.MailInfo;

@Controller
@RequestMapping("shop/share")
public class ShopShareController {
	@Autowired
	UserService userService;

	@Autowired
	SendMailService sendMailService;

	@Autowired
	MailerService mailerService;

	@Autowired
	ProductService productService;

	@Autowired
	ShareService shareService;

	@Autowired
	SessionService session;

	@RequestMapping(value = "")
	public String share(@RequestParam(name = "id") String id, ModelMap model, Share share) {
		session.set("videoId", id);
		Product product = productService.findById(id);
		model.addAttribute("title", "Shop Share");
		model.addAttribute("product", product);
		model.addAttribute("share", share);
		return "/user/account/share";
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ModelAndView sendShare(ModelMap model, @Valid @ModelAttribute("share") Share share, BindingResult result)
			throws MessagingException {
		Product product = productService.findById(session.get("videoId"));
		User user = session.get("user");
		share.setUser(user);
		share.setProduct(product);
		shareService.save(share);

		session.remove("videoId");

		MailBody mailBody = new MailBody(user.getEmail(), share.getEmailTo(), "http://localhost:8080/shop");
		String body = mailBody.shareVideo();
		MailInfo mailInfo = new MailInfo(share.getEmailTo(), MailTypeEnum.SHARE_VIDEO.toString(), body);
		mailerService.send(mailInfo);

		SendMail sendMail = new SendMail();
		sendMail.setType(MailTypeEnum.SHARE_VIDEO.toString());
		sendMail.setEmailTo(share.getEmailTo());
		sendMail.setSubject(MailTypeEnum.SHARE_VIDEO.toString());
		sendMail.setCreateDay(DateHelper.dateNow());
		sendMailService.save(sendMail);

		model.addAttribute("message", "Has shared product to " + share.getEmailTo());
		return new ModelAndView("forward:/shop", model);
	}

}
