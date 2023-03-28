package com.example.controller.admin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.service.OrderService;

@Controller
@RequestMapping("reportpage")
public class ReportController {
	Logger logger = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	OrderService orderService;

	@ModelAttribute(name = "years")
	public List<Integer> year(ModelMap model) {
		logger.info("year");
		List<Integer> list = orderService.findByYear();
		return list;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String chartByYear(ModelMap model, @RequestParam(name = "year") Optional<Integer> year) {
		model.addAttribute("title", "Report Page Admin");
		Map<String, String> graphData = orderService.findByReportMonthObject(year.orElse(2022));
		model.addAttribute("year", year.orElse(2022));
		model.addAttribute("chartData", graphData);
		logger.info("chartByYear");
		return "/admin/report/list";
	}
}