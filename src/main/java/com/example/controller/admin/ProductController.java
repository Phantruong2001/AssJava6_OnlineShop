package com.example.controller.admin;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.exception.StorageFileNotFoundException;
import com.example.helper.StringHelper;
import com.example.model.Category;
import com.example.model.Product;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import com.example.service.SessionService;
import com.example.service.StorageService;

@Controller
@RequestMapping("productpage")
public class ProductController {
	Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	ProductService productService;

	@Autowired
	SessionService session;

	@Autowired
	StorageService storageService;

	@Autowired
	CategoryService categoryService;

	@ModelAttribute(name = "categories")
	public List<Category> getCategories() {
		List<Category> list = categoryService.findAll();
		logger.info("categories");
		return list;
	}

	@RequestMapping(value = "/form")
	public String productForm(Model model, Product product) {
		model.addAttribute("title", "Product Form Page Admin");
		model.addAttribute("product", product);
		logger.info("productForm");
		return "/admin/product/form";
	}

	@RequestMapping(value = "/form/create", method = RequestMethod.POST)
	public ModelAndView createProduct(@Valid @ModelAttribute(name = "product") Product product,
			@RequestParam(name = "file") MultipartFile file, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("error", "Data entry error !");
			logger.warn("Data entry error !");
			return new ModelAndView("/admin/product/form");
		}
		if (!StringHelper.equals(product.getId(), "")) {
			model.addAttribute("error", "Product already exists, please select update !");
			logger.warn("Product already exists, please select update !");
			return new ModelAndView("/admin/product/form");
		}
		if (!file.isEmpty()) {
			UUID uuid = UUID.randomUUID();
			String uuidString = uuid.toString();
			product.setImage(storageService.getStorageFilename(file, uuidString));
			storageService.store(file, product.getImage());
		}
		productService.saveOrUpdate(product);
		model.addAttribute("message", "Create new product to public !");
		logger.info("Create new product to public !");
		return new ModelAndView("forward:/productpage/list", model);
	}

	@RequestMapping(value = "/form/update", method = RequestMethod.POST)
	public ModelAndView updateProduct(@Valid @ModelAttribute(name = "product") Product product,
			@RequestParam(name = "file") MultipartFile file, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("error", "Data entry error !");
			logger.warn("Data entry error !");
			return new ModelAndView("/admin/product/form");
		}
		if (StringHelper.equals(product.getId(), "")) {
			model.addAttribute("error", "Product already exists, please select create !");
			logger.warn("Product already exists, please select create !");
			return new ModelAndView("/admin/product/form");
		}
		if (!file.isEmpty()) {
			UUID uuid = UUID.randomUUID();
			String uuidString = uuid.toString();
			product.setImage(storageService.getStorageFilename(file, uuidString));
			storageService.store(file, product.getImage());
		}
		productService.saveOrUpdate(product);
		model.addAttribute("message", "Update new product to public !");
		logger.info("Update new product to public !");
		return new ModelAndView("forward:/productpage/list", model);
	}

	@RequestMapping(value = "/edit")
	public String editProduct(@RequestParam(name = "id") String id, ModelMap model) {
		model.addAttribute("title", "Product Form Page Admin");
		Product product = productService.findById(id);
		model.addAttribute("product", product);
		logger.info("editProduct");
		return "/admin/product/form";
	}

	@RequestMapping(value = "/delete")
	public ModelAndView deleteUser(@RequestParam(name = "id") String id, ModelMap model) throws IOException {
		Product product = productService.findById(id);
		if (product.getImage() != null) {
			storageService.delete(product.getImage());
		}
		productService.deleteById(id);
		model.addAttribute("message", "Deleted product successfully !");
		logger.info("Deleted product successfully !");
		return new ModelAndView("forward:/productpage/list", model);
	}

	@RequestMapping(value = "/list")
	public String sortAndPage(Model model, @RequestParam(name = "field") Optional<String> field,
			@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size,
			@RequestParam(name = "productpagekeywords", defaultValue = "") Optional<String> productpagekeywords) {
		model.addAttribute("title", "Product List Page Admin");

		String productpagekeyword = productpagekeywords.orElse(session.get("productpagekeywords"));
		session.set("productpagekeywords", productpagekeyword);

		Sort sort = Sort.by(Direction.DESC, field.orElse("id"));
		Pageable pageable = PageRequest.of(page.orElse(1) - 1, size.orElse(5), sort);
		Page<Product> resultPage = productService.findAllByNameLike("%" + productpagekeyword + "%", pageable);

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
		return "/admin/product/list";
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public ResponseEntity<Product> viewProduct(@PathVariable(name = "id") String id, ModelMap model) {
		Product product = productService.findById(id);
		logger.info("viewProduct");
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/images/{filename:.+}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
}
