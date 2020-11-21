package com.salesmanager.core.business.modules.cms.product;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.cms.common.ImageRemove;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.content.FileContentType;


public interface ProductImageRemove extends ImageRemove {


  public void removeProductImage(ProductImage productImage) throws ServiceException;

  public void removeProductImages(Product product) throws ServiceException;

  default void removeProductImage(ProductImage productImage, FileContentType contentType) throws ServiceException {

  }

}
