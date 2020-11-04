package com.salesmanager.core.business.modules.cms.customer;

import com.salesmanager.core.business.modules.cms.common.AssetsManager;

import java.io.Serializable;

/**
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */

public interface CustomerAssetsManager
    extends AssetsManager, CustomerImageGet, CustomerImagePut, CustomerImageRemove, Serializable {

}
