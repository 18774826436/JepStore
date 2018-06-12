/*
 *    Copyright 2016-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.mayu.jpetstore.service;

import com.mayu.jpetstore.domain.Category;
import com.mayu.jpetstore.domain.Item;
import com.mayu.jpetstore.domain.Product;
import com.mayu.jpetstore.component.exception.ResourceNotFoundException;
import com.mayu.jpetstore.mapper.CategoryMapper;
import com.mayu.jpetstore.mapper.ItemMapper;
import com.mayu.jpetstore.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 */
@Service
@RequiredArgsConstructor
public class CatalogService {

	private final CategoryMapper categoryMapper;
	private final ItemMapper itemMapper;
	private final ProductMapper productMapper;

	public Category getCategory(String categoryId) {
		return Optional.ofNullable(categoryMapper.getCategory(categoryId))
				.orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));
	}

	public Product getProduct(String productId) {
		return Optional.ofNullable(productMapper.getProduct(productId))
				.orElseThrow(() -> new ResourceNotFoundException("Product", productId));
	}

	public List<Product> getProductListByCategory(String categoryId) {
		return productMapper.getProductListByCategory(categoryId);
	}

	public List<Product> searchProductList(String keywords) {
		return Stream.of(Optional.ofNullable(keywords).orElse("").split("\\s+"))
				.distinct()
				.flatMap(x -> productMapper.selectProductList(x).stream())
				.distinct()
				.collect(Collectors.toList());
	}

	public List<Item> getItemListByProduct(String productId) {
		return itemMapper.getItemListByProduct(productId);
	}

	public Item getItem(String itemId) {
		return Optional.ofNullable(itemMapper.getItem(itemId))
				.orElseThrow(() -> new ResourceNotFoundException("Item", itemId));
	}

	public boolean isItemInStock(String itemId) {
		return itemMapper.getInventoryQuantity(itemId) > 0;
	}
}