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

import com.example.helper.StringHelper;
import com.example.model.Category;
import com.example.service.CategoryService;
import com.example.service.SessionService;

@Controller
@RequestMapping("categorypage")
public class CategoryController {
	Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	CategoryService categoryService;

	@Autowired
	SessionService session;

	@RequestMapping(value = "/form")
	public String categoryForm(Model model, Category category) {
		model.addAttribute("title", "Category Form Page Admin");
		model.addAttribute("category", category);
		logger.info("categoryForm");
		return "/admin/category/form";
	}

	@RequestMapping(value = "/form/create", method = RequestMethod.POST)
	public ModelAndView createCategory(@Valid @ModelAttribute(name = "category") Category category,
			BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("error", "Data entry error !");
			logger.warn("Data entry error !");
			return new ModelAndView("/admin/category/form");
		}
		if (!StringHelper.equals(category.getId(), "")) {
			model.addAttribute("error", "Category already exists, please select update !");
			logger.warn("Category already exists, please select update !");
			return new ModelAndView("/admin/category/form");
		}
		categoryService.saveOrUpdate(category);
		model.addAttribute("message", "Create new category to public !");
		logger.info("Create new category to public !");
		return new ModelAndView("forward:/categorypage/list", model);
	}

	@RequestMapping(value = "/form/update", method = RequestMethod.POST)
	public ModelAndView updateCategory(@Valid @ModelAttribute(name = "category") Category category,
			BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("error", "Data entry error !");
			logger.warn("Data entry error !");
			return new ModelAndView("/admin/category/form");
		}
		if (StringHelper.equals(category.getId(), "")) {
			model.addAttribute("error", "Category already exists, please select create !");
			logger.warn("Category already exists, please select create !");
			return new ModelAndView("/admin/category/form");
		}
		categoryService.saveOrUpdate(category);
		model.addAttribute("message", "Update new category to public !");
		logger.info("Update new category to public !");
		return new ModelAndView("forward:/categorypage/list", model);
	}

	@RequestMapping(value = "/edit")
	public String editCategory(@RequestParam(name = "id") String id, ModelMap model) {
		model.addAttribute("title", "Category Form Page Admin");
		Category category = categoryService.findById(id);
		model.addAttribute("category", category);
		logger.info("editCategory");
		return "/admin/category/form";
	}

	@RequestMapping(value = "/delete")
	public ModelAndView deleteCategory(@RequestParam(name = "id") String id, ModelMap model) {
		categoryService.deleteById(id);
		model.addAttribute("message", "Deleted category successfully !");
		logger.info("Deleted category successfully !");
		return new ModelAndView("forward:/categorypage/list", model);
	}

	@RequestMapping(value = "/list")
	public String sortAndPage(Model model, @RequestParam(name = "field") Optional<String> field,
			@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size,
			@RequestParam(name = "namekeyword", defaultValue = "") Optional<String> namekeywords) {
		model.addAttribute("title", "Category List Page Admin");

		String namekeyword = namekeywords.orElse(session.get("namekeywords"));
		session.set("namekeywords", namekeyword);

		Sort sort = Sort.by(Direction.DESC, field.orElse("id"));
		Pageable pageable = PageRequest.of(page.orElse(1) - 1, size.orElse(5), sort);
		Page<Category> resultPage = categoryService.findAllByNameLike("%" + namekeyword + "%", pageable);

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
		return "/admin/category/list";
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public ResponseEntity<Category> viewCategory(@PathVariable(name = "id") String id, ModelMap model) {
		Category category = categoryService.findById(id);
		logger.info("viewCategory");
		return new ResponseEntity<Category>(category, HttpStatus.OK);
	}
}
