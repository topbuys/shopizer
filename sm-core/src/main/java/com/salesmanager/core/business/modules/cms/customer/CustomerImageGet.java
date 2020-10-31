package com.salesmanager.core.business.modules.cms.customer;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.model.content.OutputContentFile;
import com.salesmanager.core.model.customer.Customer;

/**
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */

public interface CustomerImageGet {
    OutputContentFile getCustomerImage(String storeCode, Long customerId, String imageName) throws ServiceException;
}
