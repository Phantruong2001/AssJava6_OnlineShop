package com.example.controller.admin;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Order;
import com.example.model.OrderDetail;
import com.example.service.OrderDetailService;
import com.example.service.OrderService;
import com.example.service.SessionService;

@Controller
@RequestMapping("invoicepage")
public class InvoiceController {
	Logger logger = LoggerFactory.getLogger(InvoiceController.class);

	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	SessionService session;

	@RequestMapping(value = "")
	public String invoicePage(@RequestParam(name = "field") Optional<String> field,
			@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size,
			@RequestParam(name = "invoicepagekeywords", defaultValue = "") Optional<String> invoicepagekeywords,
			Model model) {
		model.addAttribute("title", "Invoice Page Admin");

		String invoicepagekeyword = invoicepagekeywords.orElse(session.get("invoicepagekeywords"));
		session.set("invoicepagekeywords", invoicepagekeyword);

		Sort sort = Sort.by(Direction.DESC, field.orElse("order.id"));
		Pageable pageable = PageRequest.of(page.orElse(1) - 1, size.orElse(5), sort);
		Page<Object[]> resultPage = orderService.findByInvoiceObject("%" + invoicepagekeyword + "%", pageable);

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
		model.addAttribute("field", field.orElse("order.id"));
		model.addAttribute("size", size.orElse(5));
		model.addAttribute("resultPage", resultPage);
		logger.info("invoicePage");
		return "/admin/invoice/invoice";
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public ResponseEntity<Order> viewInvoice(@PathVariable(name = "id") String id, ModelMap model) {
		Order order = orderService.findById(id);
		logger.info("viewInvoice");
		return new ResponseEntity<Order>(order, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/viewDetail/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<OrderDetail>> viewInvoiceDetail(@PathVariable(name = "id") String id, ModelMap model) {
		List<OrderDetail> list = orderDetailService.findByOrderId(id);
		logger.info("viewInvoiceDetail");
		return new ResponseEntity<List<OrderDetail>>(list, HttpStatus.OK);
	}
}
