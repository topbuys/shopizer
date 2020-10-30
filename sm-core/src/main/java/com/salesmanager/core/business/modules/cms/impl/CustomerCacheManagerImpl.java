package com.salesmanager.core.business.modules.cms.impl;

/**
 * Infinispan asset manager
 * 
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 *
 */
public class CustomerCacheManagerImpl extends CacheManagerImpl {


  private final static String NAMED_CACHE = "CustomerRepository";
  private String root;


  public CustomerCacheManagerImpl(String location, String root) {
    super.init(NAMED_CACHE, location);
    this.root = root;
  }


  @Override
  public String getRootName() {
    return root;
  }


  @Override
  public String getLocation() {
    return location;
  }



}

