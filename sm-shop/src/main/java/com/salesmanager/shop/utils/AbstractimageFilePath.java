package com.salesmanager.shop.utils;

import java.util.Properties;

import javax.annotation.Resource;

import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.customer.Customer;
import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.manufacturer.Manufacturer;
import com.salesmanager.core.model.content.FileContentType;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.shop.constants.Constants;
import org.springframework.beans.factory.annotation.Value;


public abstract class AbstractimageFilePath implements ImageFilePath {

	private final String DIGITAL_OCEAN = "digitalOcean";

	private String cmsMethod = "";

	public abstract String getBasePath();

	public abstract void setBasePath(String basePath);
	
	protected static final String CONTEXT_PATH = "CONTEXT_PATH";
	
	public @Resource(name="shopizer-properties") Properties properties = new Properties();//shopizer-properties

	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getCmsMethod() {
		return cmsMethod;
	}

	public void setCmsMethod(String cmsMethod) {
		this.cmsMethod = cmsMethod;
	}

	/**
	 * Builds a static content image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildStaticImageUtils(MerchantStore store, String imageName) {
		StringBuilder imgName = new StringBuilder().append(getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH).append(FileContentType.IMAGE.name()).append(Constants.SLASH);
				if(!StringUtils.isBlank(imageName)) {
					imgName.append(imageName);
				}
		return imgName.toString();
				
	}
	
	/**
	 * Builds a static content image file path that can be used by image servlet
	 * utility for getting the physical image by specifying the image type
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildStaticImageUtils(MerchantStore store, String type, String imageName) {
		StringBuilder imgName = new StringBuilder().append(getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH).append(type).append(Constants.SLASH);
		if(!StringUtils.isBlank(imageName)) {
				imgName.append(imageName);
		}
		return imgName.toString();

	}
	
	/**
	 * Builds a manufacturer image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param manufacturer
	 * @param imageName
	 * @return
	 */
	public String buildManufacturerImageUtils(MerchantStore store, Manufacturer manufacturer, String imageName) {
		return new StringBuilder().append(getBasePath()).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH).
				append(FileContentType.MANUFACTURER.name()).append(Constants.SLASH)
				.append(manufacturer.getId()).append(Constants.SLASH)
				.append(imageName).toString();
	}

	/**
	 * Builds a product image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param product
	 * @param imageName
	 * @return
	 */
	public String buildProductImageUtils(MerchantStore store, Product product, String imageName) {
		return new StringBuilder().append(getBasePath()).append(Constants.PRODUCTS_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH)
				.append(product.getSku()).append(Constants.SLASH).append(Constants.SMALL_IMAGE).append(Constants.SLASH).append(imageName).toString();
	}


	/**
	 * Builds a product image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param product
	 * @param imageName
	 * @return
	 */
	public String buildProductImageUtils(MerchantStore store, Product product, String imageName, String imageId) {
		if (cmsMethod.equals(DIGITAL_OCEAN)) {
			return new StringBuilder().append(getBasePath()).append(Constants.PRODUCTS_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH)
					.append(product.getSku()).append(Constants.SLASH).append(Constants.SMALL_IMAGE).append(Constants.SLASH).append(imageId).append(Constants.SLASH).append(imageName).toString();

		} else {
			return buildProductImageUtils(store, product, imageName);
		}
	}
	
	/**
	 * Builds a default product image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param sku
	 * @param imageName
	 * @return
	 */
	public String buildProductImageUtils(MerchantStore store, String sku, String imageName) {
		return new StringBuilder().append(getBasePath()).append(Constants.PRODUCTS_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH)
				.append(sku).append(Constants.SLASH).append(Constants.SMALL_IMAGE).append(Constants.SLASH).append(imageName).toString();
	}
	
	/**
	 * Builds a large product image file path that can be used by the image servlet
	 * @param store
	 * @param sku
	 * @param imageName
	 * @return
	 */
	public String buildLargeProductImageUtils(MerchantStore store, String sku, String imageName) {
		return new StringBuilder().append(getBasePath()).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH)
				.append(sku).append(Constants.SLASH).append(Constants.SMALL_IMAGE).append(Constants.SLASH).append(imageName).toString();
	}


	
	/**
	 * Builds a merchant store logo path
	 * @param store
	 * @return
	 */
	public String buildStoreLogoFilePath(MerchantStore store) {
		return new StringBuilder().append(getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH).append(FileContentType.LOGO).append(Constants.SLASH)
				.append(store.getStoreLogo()).toString();
	}
	
	/**
	 * Builds product property image url path
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildProductPropertyImageFilePath(MerchantStore store, String imageName) {
		return new StringBuilder().append(getBasePath()).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH).append(FileContentType.PROPERTY).append(Constants.SLASH)
				.append(imageName).toString();
	}
	
	public String buildProductPropertyImageUtils(MerchantStore store, String imageName) {
		return new StringBuilder().append(getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append("/").append(FileContentType.PROPERTY).append("/")
				.append(imageName).toString();
	}
	
	/**
	 * Builds static file url path
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildStaticContentFilePath(MerchantStore store, String fileName) {
		StringBuilder sb = new StringBuilder().append(getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH);
		if(!StringUtils.isBlank(fileName)) {
			sb.append(fileName);
		}
		return sb.toString();
	}


	/**
	 * Builds category image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param category
	 * @param imageName
	 * @return
	 */
	public String buildCategorymageUtils(Category category, String imageName) {
		return new StringBuilder().append(getBasePath()).append(Constants.CATEGORY_URI).append(Constants.SLASH).append(category.getCode()).append(Constants.SLASH)
				.append(imageName).toString();
	}

	/**
	 * Builds category image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param category
	 * @param imageName
	 * @return
	 */
	public String buildCategorymageUtils(Category category, String imageName, String imageId) {
		if (cmsMethod.equals(DIGITAL_OCEAN)) {
			return new StringBuilder().append(getBasePath()).append(Constants.CATEGORY_URI).append(Constants.SLASH).append(category.getCode()).append(Constants.SLASH)
					.append(imageId).append(Constants.SLASH).append(imageName).toString();
		} else {
			return buildCategorymageUtils(category, imageName);
		}
	}

	/**
	 * Builds customer image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param customer
	 * @param imageName
	 * @return
	 */
	public String buildCustomerImageUtils(Customer customer, String imageName) {
		return new StringBuilder().append(getBasePath()).append(Constants.CUSTOMER_URI).append(Constants.SLASH).append(customer.getMerchantStore().getCode())
				.append(Constants.SLASH).append(customer.getId()).append(Constants.SLASH)
				.append(imageName).toString();
	}
	


}
