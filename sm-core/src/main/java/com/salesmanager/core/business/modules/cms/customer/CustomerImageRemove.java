package com.salesmanager.core.business.modules.cms.customer;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.model.customer.Customer;

/**
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */

public interface CustomerImageRemove {
    void removeCustomerImage(Customer customer) throws ServiceException;
}
