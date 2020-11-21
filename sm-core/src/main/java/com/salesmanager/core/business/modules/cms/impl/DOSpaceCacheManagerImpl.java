package com.salesmanager.core.business.modules.cms.impl;

/**
 * Interaction with DO Spaces same as AWS S3
 * https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/examples-s3-transfermanager.html
 * 
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 *
 */
public class DOSpaceCacheManagerImpl implements CMSManager {

  private String baseUrl;
  private String bucketName;
  private String regionName;

  public DOSpaceCacheManagerImpl(String baseUrl, String bucketName, String regionName) {
    this.baseUrl = baseUrl;
    this.bucketName = bucketName;
    this.regionName = regionName;
  }


  @Override
  public String getRootName() {
    return bucketName;
  }

  @Override
  public String getLocation() {
    return regionName;
  }

  @Override
  public String getBaseUrl() {
    return baseUrl;
  }

  public String getBucketName() {
    return bucketName;
  }

  public String getRegionName() {
    return regionName;
  }


}
