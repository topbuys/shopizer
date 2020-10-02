package com.salesmanager.core.business.modules.cms.category;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.model.catalog.category.image.CategoryImage;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.content.OutputContentFile;

/**
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */

public interface CategoryImageGet {
    OutputContentFile getCategoryImage(CategoryImage categoryImage) throws ServiceException;
    OutputContentFile getCategoryImage(String categoryCode, String imageName) throws ServiceException;
}
