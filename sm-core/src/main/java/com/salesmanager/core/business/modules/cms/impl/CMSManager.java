package com.salesmanager.core.business.modules.cms.impl;

/**
 * Marker interface
 * 
 * @author carlsamson
 *
 */
public interface CMSManager {

  String getRootName();

  String getLocation();

  default String getBaseUrl() {
    return "";
  }
}
