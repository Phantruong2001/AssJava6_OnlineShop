package com.example.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.example.model.Product;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.util.Item;

@SessionScope
@Service
public class CartServiceImpl implements CartService {

	Map<String, Item> map = new HashMap<>();

	@Autowired
	ProductService productService;

	@Override
	public Item add(String id) {
		if (getItem(id) == null) {
			Product product = productService.findById(id);
			Item item = new Item();
			item.setProduct_id(id);
			item.setName(product.getName());
			item.setImage(product.getImage());
			item.setQuantity(1);
			if (product.getDiscount() == 0) {
				item.setPrice(product.getPrice());
			}else {
				item.setPrice(product.getDiscount() * 0.01 * product.getPrice());
			}
			item.setTotail(item.getQuantity() * item.getPrice());
			map.put(id, item);
		} else {
			map.forEach((key, value) -> {
				if (value.getProduct_id().equals(id)) {
					value.setQuantity(value.getQuantity() + 1);
					value.setTotail(value.getQuantity() * value.getPrice());
					map.put(id, value);
					return;
				}
			});
		}
		return null;
	}

	@Override
	public void remove(String id) {
		map.remove(id);
	}

	@Override
	public Item update(String id, int qty) {
		for (Item item : map.values()) {
			if (item.getProduct_id().equals(id)) {
				item.setQuantity(qty);
				item.setTotail(item.getQuantity() * item.getPrice());
				map.put(id, item);
				return item;
			}
		}
		return null;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Collection<Item> getItems() {
		return map.values();
	}

	@Override
	public Item getItem(String id) {
		for (Item item : map.values()) {
			if (item.getProduct_id().equals(id)) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getCount() {
		return map.size();
	}

	@Override
	public double getAmount() {
		double amount = 0;
		for (Item item : map.values()) {
			amount += item.getTotail();
		}
		return amount;
	}

	@Override
	public int getQty() {
		int qty = 0;
		for (Item item : map.values()) {
			qty += item.getQuantity();
		}
		return qty;
	}

}
