package com.example.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	private String product_id;
	private String name;
	private String image;
	private double price;
	private int quantity;
	private double totail;
}
