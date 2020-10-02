package com.salesmanager.core.business.services.catalog.category.image;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.category.image.CategoryImage;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.file.ProductImageSize;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.content.ImageContentFile;
import com.salesmanager.core.model.content.OutputContentFile;

import java.util.List;

/**
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */

public interface CategoryImageService extends SalesManagerEntityService<Long, CategoryImage> {

	/**
	 * Add a CategoryImage to the persistence and an entry to the CMS
	 * @param category
	 * @param categoryImage
	 * @param inputImage
	 * @throws ServiceException
	 */
	void addCategoryImage(Category category, CategoryImage categoryImage, ImageContentFile inputImage)
			throws ServiceException;

	/**
	 * Get the image ByteArrayOutputStream and content description from CMS
	 * @param categoryImage
	 * @return
	 * @throws ServiceException
	 */
	OutputContentFile getCategoryImage(CategoryImage categoryImage)
			throws ServiceException;

	void removeCategoryImage(CategoryImage categoryImage) throws ServiceException;

	void saveOrUpdate(CategoryImage categoryImage) throws ServiceException;
}
