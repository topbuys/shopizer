package com.salesmanager.shop.model.catalog.category;

import java.util.ArrayList;
import java.util.List;

public class ReadableCategory extends CategoryEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CategoryDescription description;//one category based on language
	private int productCount;
	private String store;
	private List<ReadableCategory> children = new ArrayList<ReadableCategory>();
	private ReadableCategoryImage image;
	private List<ReadableCategoryImage> images;
	
	
	public void setDescription(CategoryDescription description) {
		this.description = description;
	}
	public CategoryDescription getDescription() {
		return description;
	}

	public int getProductCount() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	public List<ReadableCategory> getChildren() {
		return children;
	}
	public void setChildren(List<ReadableCategory> children) {
		this.children = children;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}

	public ReadableCategoryImage getImage() {
		return image;
	}

	public void setImage(ReadableCategoryImage image) {
		this.image = image;
	}

	public List<ReadableCategoryImage> getImages() {
		return images;
	}

	public void setImages(List<ReadableCategoryImage> images) {
		this.images = images;
	}
}
