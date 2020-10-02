package com.salesmanager.core.business.modules.cms.category;

import com.salesmanager.core.business.exception.ServiceException;

import com.salesmanager.core.model.catalog.category.image.CategoryImage;
import com.salesmanager.core.model.content.ImageContentFile;

/**
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */

public interface CategoryImagePut {
    void addCategoryImage(CategoryImage categoryImage, ImageContentFile contentImage)
            throws ServiceException;

}
