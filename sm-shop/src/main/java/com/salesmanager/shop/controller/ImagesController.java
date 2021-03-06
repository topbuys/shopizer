package com.salesmanager.shop.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.salesmanager.core.business.modules.cms.customer.CustomerFileManager;
import com.salesmanager.core.business.services.catalog.category.image.CategoryImageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.catalog.product.image.ProductImageService;
import com.salesmanager.core.business.services.content.ContentService;
import com.salesmanager.core.model.catalog.product.file.ProductImageSize;
import com.salesmanager.core.model.content.FileContentType;
import com.salesmanager.core.model.content.OutputContentFile;

/**
 * When handling images and files from the application server
 * @author c.samson
 *
 */
@Controller
public class ImagesController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImagesController.class);
	

	
	@Inject
	private ContentService contentService;
	
	@Inject
	private ProductImageService productImageService;

	@Autowired
	private CategoryImageService categoryImageService;

	@Autowired
	private CustomerFileManager customerFileManager;

	private byte[] tempImage = null;
	
	@PostConstruct
	public void init() {
		try {
			File file = ResourceUtils.getFile("classpath:static/not-found.png");
			if(file != null) {
				byte[] bFile = Files.readAllBytes(file.toPath());
				this.tempImage = bFile;
			}

			
		} catch (Exception e) {
			LOGGER.error("Can't load temporary default image", e);
		}
	}
	
	/**
	 * Logo, content image
	 * @param storeCode
	 * @param imageType (LOGO, CONTENT, IMAGE)
	 * @param imageName
	 * @return
	 * @throws IOException
	 * @throws ServiceException 
	 */
	@RequestMapping("/static/files/{storeCode}/{imageType}/{imageName}.{extension}")
	public @ResponseBody byte[] printImage(@PathVariable final String storeCode, @PathVariable final String imageType, @PathVariable final String imageName, @PathVariable final String extension) throws IOException, ServiceException {

		// example -> /static/files/DEFAULT/CONTENT/myImage.png
		
		FileContentType imgType = null;
		
		if(FileContentType.LOGO.name().equals(imageType)) {
			imgType = FileContentType.LOGO;
		}
		
		if(FileContentType.IMAGE.name().equals(imageType)) {
			imgType = FileContentType.IMAGE;
		}
		
		if(FileContentType.PROPERTY.name().equals(imageType)) {
			imgType = FileContentType.PROPERTY;
		}
		
		OutputContentFile image =contentService.getContentFile(storeCode, imgType, new StringBuilder().append(imageName).append(".").append(extension).toString());
		
		
		if(image!=null) {
			return image.getFile().toByteArray();
		} else {
			return tempImage;
		}

	}
	

	/**
	 * For product images
	 * @Deprecated
	 * @param storeCode
	 * @param productCode
	 * @param imageType
	 * @param imageName
	 * @param extension
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/static/{storeCode}/{imageType}/{productCode}/{imageName}.{extension}")
	public @ResponseBody byte[] printImage(@PathVariable final String storeCode, @PathVariable final String productCode, @PathVariable final String imageType, @PathVariable final String imageName, @PathVariable final String extension) throws IOException {

		// product image
		// example small product image -> /static/DEFAULT/products/TB12345/product1.jpg
		
		// example large product image -> /static/DEFAULT/products/TB12345/product1.jpg

		
		/**
		 * List of possible imageType
		 * 
		 */
		

		ProductImageSize size = ProductImageSize.SMALL;
		
		if(imageType.equals(FileContentType.PRODUCTLG.name())) {
			size = ProductImageSize.LARGE;
		} 
		

		
		OutputContentFile image = null;
		try {
			image = productImageService.getProductImage(storeCode, productCode, new StringBuilder().append(imageName).append(".").append(extension).toString(), size);
		} catch (ServiceException e) {
			LOGGER.error("Cannot retrieve image " + imageName, e);
		}
		if(image!=null) {
			return image.getFile().toByteArray();
		} else {
			//empty image placeholder
			return tempImage;
		}

	}
	
	/**
	 * Exclusive method for dealing with product images
	 * @param storeCode
	 * @param productCode
	 * @param imageName
	 * @param extension
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/static/products/{storeCode}/{productCode}/{imageSize}/{imageName}.{extension}")
	public @ResponseBody byte[] printImage(@PathVariable final String storeCode, @PathVariable final String productCode, @PathVariable final String imageSize, @PathVariable final String imageName, @PathVariable final String extension, HttpServletRequest request) throws IOException {

		// product image small
		// example small product image -> /static/products/DEFAULT/TB12345/SMALL/product1.jpg
		
		// example large product image -> /static/products/DEFAULT/TB12345/LARGE/product1.jpg


		/**
		 * List of possible imageType
		 * 
		 */
		
		
		ProductImageSize size = ProductImageSize.SMALL;
		
		if(FileContentType.PRODUCTLG.name().equals(imageSize)) {
			size = ProductImageSize.LARGE;
		} 
		
	

		
		OutputContentFile image = null;
		try {
			image = productImageService.getProductImage(storeCode, productCode, new StringBuilder().append(imageName).append(".").append(extension).toString(), size);
		} catch (ServiceException e) {
			LOGGER.error("Cannot retrieve image " + imageName, e);
		}
		if(image!=null) {
			return image.getFile().toByteArray();
		} else {
			//empty image placeholder
			return tempImage;
		}

	}
	
	/**
	 * Exclusive method for dealing with product images
	 * @param storeCode
	 * @param productCode
	 * @param imageName
	 * @param extension
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/static/products/{storeCode}/{productCode}/{imageName}.{extension}")
	public @ResponseBody byte[] printImage(@PathVariable final String storeCode, @PathVariable final String productCode, @PathVariable final String imageName, @PathVariable final String extension, HttpServletRequest request) throws IOException {

		// product image
		// example small product image -> /static/products/DEFAULT/TB12345/product1.jpg?size=small
		
		// example large product image -> /static/products/DEFAULT/TB12345/product1.jpg
		// or
		//example large product image -> /static/products/DEFAULT/TB12345/product1.jpg?size=large
		

		/**
		 * List of possible imageType
		 * 
		 */
		

		ProductImageSize size = ProductImageSize.LARGE;
		
				
		if(StringUtils.isNotBlank(request.getParameter("size"))) {
			String requestSize = request.getParameter("size");
			if(requestSize.equals(ProductImageSize.SMALL.name())) {
				size = ProductImageSize.SMALL;
			} 
		}
		

		
		OutputContentFile image = null;
		try {
			image = productImageService.getProductImage(storeCode, productCode, new StringBuilder().append(imageName).append(".").append(extension).toString(), size);
		} catch (ServiceException e) {
			LOGGER.error("Cannot retrieve image " + imageName, e);
		}
		if(image!=null) {
			return image.getFile().toByteArray();
		} else {
			//empty image placeholder
			return tempImage;
		}

	}

	/**
	 * Exclusive method for dealing with category images
	 * @param categoryCode
	 * @param imageName
	 * @param extension
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/static/category/{categoryCode}/{imageName}.{extension}")
	public @ResponseBody byte[] printImage(@PathVariable final String categoryCode, @PathVariable final String imageName, @PathVariable final String extension, HttpServletRequest request) throws IOException {

		// category image
		// example category image -> /static/category/TB12345/category1.jpg

		OutputContentFile image = null;
		try {
			image = categoryImageService.getCategoryImage(categoryCode, new StringBuilder().append(imageName).append(".").append(extension).toString());
		} catch (ServiceException e) {
			LOGGER.error("Cannot retrieve image " + imageName, e);
		}
		if(image!=null) {
			return image.getFile().toByteArray();
		} else {
			//empty image placeholder
			return tempImage;
		}

	}

	/**
	 * Exclusive method for dealing with customer photo
	 * @param storeCode
	 * @param customerId
	 * @param imageName
	 * @param extension
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/static/customer/{storeCode}/{customerId}/{imageName}.{extension}")
	public @ResponseBody byte[] printImage(@PathVariable final String storeCode, @PathVariable final Long customerId, @PathVariable final String imageName, @PathVariable final String extension, HttpServletRequest request) throws IOException {

		// customer image
		// example customer image -> /static/customer/TB12345/1/customer1.jpg

		OutputContentFile image = null;
		try {
			image = customerFileManager.getCustomerImage(storeCode, customerId, new StringBuilder().append(imageName).append(".").append(extension).toString());
		} catch (ServiceException e) {
			LOGGER.error("Cannot retrieve image " + imageName, e);
		}
		if(image!=null) {
			return image.getFile().toByteArray();
		} else {
			//empty image placeholder
			return tempImage;
		}

	}


}
