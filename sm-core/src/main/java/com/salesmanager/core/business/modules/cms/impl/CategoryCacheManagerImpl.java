package com.salesmanager.core.business.modules.cms.impl;

/**
 * Infinispan asset manager
 * 
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 *
 */
public class CategoryCacheManagerImpl extends CacheManagerImpl {


  private final static String NAMED_CACHE = "CategoryRepository";
  private String root;


  public CategoryCacheManagerImpl(String location, String root) {
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

