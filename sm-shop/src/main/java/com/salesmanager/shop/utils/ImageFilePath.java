package com.salesmanager.shop.utils;

import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.manufacturer.Manufacturer;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;

public interface ImageFilePath {
	
	/**
	 * Context path configured in shopizer-properties.xml
	 * @return
	 */
	public String getContextPath();
	
	
	public String getBasePath();

	/**
	 * Builds a static content image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildStaticImageUtils(MerchantStore store, String imageName);
	
	/**
	 * Builds a static content image file path that can be used by image servlet
	 * utility for getting the physical image by specifying the image type
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildStaticImageUtils(MerchantStore store, String type, String imageName);
	
	/**
	 * Builds a manufacturer image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param manufacturer
	 * @param imageName
	 * @return
	 */
	public String buildManufacturerImageUtils(MerchantStore store, Manufacturer manufacturer, String imageName);
	
	/**
	 * Builds a product image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param product
	 * @param imageName
	 * @return
	 */
	String buildProductImageUtils(MerchantStore store, Product product, String imageName);


	/**
	 * Builds a product image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param product
	 * @param imageName
	 * @return
	 */
	String buildProductImageUtils(MerchantStore store, Product product, String imageName, String imageId);

	/**
	 * Builds a default product image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param sku
	 * @param imageName
	 * @return
	 */
	public String buildProductImageUtils(MerchantStore store, String sku, String imageName);
	
	/**
	 * Builds a large product image file path that can be used by the image servlet
	 * @param store
	 * @param sku
	 * @param imageName
	 * @return
	 */
	public String buildLargeProductImageUtils(MerchantStore store, String sku, String imageName);


	
	/**
	 * Builds a merchant store logo path
	 * @param store
	 * @return
	 */
	public String buildStoreLogoFilePath(MerchantStore store);
	
	/**
	 * Builds product property image url path
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildProductPropertyImageUtils(MerchantStore store, String imageName);
	
	
	/**
	 * Builds static file path
	 * @param store
	 * @param fileName
	 * @return
	 */
	public String buildStaticContentFilePath(MerchantStore store, String fileName);

	/**
	 * Builds category image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param category
	 * @param imageName
	 * @return
	 */
	String buildCategorymageUtils(Category category, String imageName);

	/**
	 * Builds category image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param category
	 * @param imageName
	 * @return
	 */
	String buildCategorymageUtils(Category category, String imageName, String imageId);

	/**
	 * Builds customer image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param customer
	 * @param imageName
	 * @return
	 */
	String buildCustomerImageUtils(Customer customer, String imageName);
}
