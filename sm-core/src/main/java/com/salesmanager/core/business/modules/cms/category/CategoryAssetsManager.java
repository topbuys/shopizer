package com.salesmanager.core.business.modules.cms.category;

import com.salesmanager.core.business.modules.cms.common.AssetsManager;
import com.salesmanager.core.business.modules.cms.product.ProductImageGet;
import com.salesmanager.core.business.modules.cms.product.ProductImagePut;
import com.salesmanager.core.business.modules.cms.product.ProductImageRemove;

import java.io.Serializable;

public interface CategoryAssetsManager
    extends AssetsManager, CategoryImageGet, CategoryImagePut, CategoryImageRemove, Serializable {

}
