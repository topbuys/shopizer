package com.salesmanager.core.business.services.customer;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.salesmanager.core.business.modules.cms.category.CategoryFileManager;
import com.salesmanager.core.business.modules.cms.customer.CustomerFileManager;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.category.image.CategoryImage;
import com.salesmanager.core.model.content.FileContentType;
import com.salesmanager.core.model.content.ImageContentFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.customer.CustomerRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.services.customer.attribute.CustomerAttributeService;
import com.salesmanager.core.model.common.Address;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.CustomerCriteria;
import com.salesmanager.core.model.customer.CustomerList;
import com.salesmanager.core.model.customer.attribute.CustomerAttribute;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.modules.utils.GeoLocation;



@Service("customerService")
public class CustomerServiceImpl extends SalesManagerEntityServiceImpl<Long, Customer> implements CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	private CustomerRepository customerRepository;
	
	@Inject
	private CustomerAttributeService customerAttributeService;
	
	@Inject
	private GeoLocation geoLocation;

	@Autowired
	private CustomerFileManager customerFileManager;
	
	@Inject
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		super(customerRepository);
		this.customerRepository = customerRepository;
	}

	@Override
	public List<Customer> getByName(String firstName) {
		return customerRepository.findByName(firstName);
	}
	
	@Override
	public Customer getById(Long id) {
			return customerRepository.findOne(id);		
	}
	
	@Override
	public Customer getByNick(String nick) {
		return customerRepository.findByNick(nick);	
	}
	
	@Override
	public Customer getByNick(String nick, int storeId) {
		return customerRepository.findByNick(nick, storeId);	
	}
	
	@Override
	public List<Customer> getListByStore(MerchantStore store) {
		return customerRepository.findByStore(store.getId());
	}
	
	@Override
	public CustomerList getListByStore(MerchantStore store, CustomerCriteria criteria) {
		return customerRepository.listByStore(store,criteria);
	}
	
	@Override
	public Address getCustomerAddress(MerchantStore store, String ipAddress) throws ServiceException {
		
		try {
			return geoLocation.getAddress(ipAddress);
		} catch(Exception e) {
			throw new ServiceException(e);
		}
		
	}

	@Override	
	public void saveOrUpdate(Customer customer) throws ServiceException {

		LOGGER.debug("Creating Customer");
		
		if(customer.getId()!=null && customer.getId()>0) {
			super.update(customer);
		} else {
			super.create(customer);
		}
		saveCustomerImage(customer);
	}

	public void delete(Customer customer) throws ServiceException {
		customer = getById(customer.getId());
		
		//delete attributes
		List<CustomerAttribute> attributes =customerAttributeService.getByCustomer(customer.getMerchantStore(), customer);
		if(attributes!=null) {
			for(CustomerAttribute attribute : attributes) {
				customerAttributeService.delete(attribute);
			}
		}
		customerRepository.delete(customer);

	}


	/**
	 * Image creation needs extra service to save the file in the CMS
	 */
	private void saveCustomerImage(Customer customer) throws ServiceException {
		try {
			if(customerFileManager.getCustomerImage(customer) == null) {
				if (customer.getImage() != null) {
					InputStream inputStream = customer.getImage().getInputStream();
					ImageContentFile cmsContentImage = new ImageContentFile();
					cmsContentImage.setFileName(customer.getCustomerImage());

					cmsContentImage.setFile(inputStream);
					cmsContentImage.setFileContentType(FileContentType.IMAGE);

					customerFileManager.addCustomerImage(customer, cmsContentImage);
				}
			}
		} catch(Exception e) {
			throw new ServiceException(e);
		}
	}
}
