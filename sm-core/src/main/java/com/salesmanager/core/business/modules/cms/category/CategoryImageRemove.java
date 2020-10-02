package com.salesmanager.core.business.modules.cms.category;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.model.catalog.category.image.CategoryImage;

/**
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */

public interface CategoryImageRemove {
    void removeCategoryImage(CategoryImage categoryImage) throws ServiceException;
}
