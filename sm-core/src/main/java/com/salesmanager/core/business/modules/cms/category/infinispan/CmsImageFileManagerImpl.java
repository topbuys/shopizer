package com.salesmanager.core.business.modules.cms.category.infinispan;

import com.salesmanager.core.business.constants.Constants;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.cms.category.CategoryAssetsManager;
import com.salesmanager.core.business.modules.cms.impl.CMSManager;
import com.salesmanager.core.business.modules.cms.impl.CacheManager;
import com.salesmanager.core.business.modules.cms.product.ProductAssetsManager;
import com.salesmanager.core.model.catalog.category.image.CategoryImage;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.file.ProductImageSize;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.content.FileContentType;
import com.salesmanager.core.model.content.ImageContentFile;
import com.salesmanager.core.model.content.OutputContentFile;
import com.salesmanager.core.model.merchant.MerchantStore;
import org.apache.commons.io.IOUtils;
import org.infinispan.tree.Fqn;
import org.infinispan.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Manager for storing in retrieving image files from the CMS This is a layer on top of Infinispan
 * https://docs.jboss.org/author/display/ISPN/Tree+API+Module
 * 
 * Manages - Category images
 * 
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */
public class CmsImageFileManagerImpl implements CategoryAssetsManager {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(CmsImageFileManagerImpl.class);

  private static CmsImageFileManagerImpl fileManager = null;

  private final static String ROOT_NAME = "category-merchant";

  private String rootName = ROOT_NAME;

  private CacheManager cacheManager;


  /**
   * Requires to stop the engine when image servlet un-deploys
   */
  public void stopFileManager() {

    try {
      LOGGER.info("Stopping CMS");
      cacheManager.getManager().stop();
    } catch (Exception e) {
      LOGGER.error("Error while stopping CmsImageFileManager", e);
    }
  }

  @PostConstruct
  void init() {

    this.rootName = ((CMSManager) cacheManager).getRootName();
    LOGGER.info("init " + getClass().getName() + " setting root" + this.rootName);

  }

  public static CmsImageFileManagerImpl getInstance() {

    if (fileManager == null) {
      fileManager = new CmsImageFileManagerImpl();
    }


    return fileManager;

  }

  private CmsImageFileManagerImpl() {

  }

  @Override
  public void addCategoryImage(CategoryImage categoryImage, ImageContentFile contentImage) throws ServiceException {
    if (cacheManager.getTreeCache() == null) {
      throw new ServiceException(
              "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()");
    }

    try {

      // node
      StringBuilder nodePath = new StringBuilder();
      nodePath.append(categoryImage.getCategory().getCode())
              .append(Constants.SLASH);

      Node<String, Object> categoryNode = this.getNode(nodePath.toString());


      InputStream isFile = contentImage.getFile();

      ByteArrayOutputStream output = new ByteArrayOutputStream();
      IOUtils.copy(isFile, output);


      // object for a given product containing all images
      categoryNode.put(contentImage.getFileName(), output.toByteArray());



    } catch (Exception e) {

      throw new ServiceException(e);

    }
  }

  @Override
  public OutputContentFile getCategoryImage(CategoryImage categoryImage) throws ServiceException {
    return getCategoryImage(categoryImage.getCategory().getCode(), categoryImage.getCategoryImage());
  }

//  @Override
//  public void removeProductImage(ProductImage productImage) throws ServiceException {
//
//    if (cacheManager.getTreeCache() == null) {
//      throw new ServiceException(
//          "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()");
//    }
//
//    try {
//
//
//      StringBuilder nodePath = new StringBuilder();
//      nodePath.append(productImage.getProduct().getMerchantStore().getCode())
//          .append(Constants.SLASH).append(productImage.getProduct().getSku());
//
//
//      Node<String, Object> productNode = this.getNode(nodePath.toString());
//      productNode.remove(productImage.getProductImage());
//
//
//
//    } catch (Exception e) {
//      throw new ServiceException(e);
//    } finally {
//
//    }
//
//  }


  private OutputContentFile getCategoryImage(String categoryCode,
    String imageName) throws ServiceException {

    if (cacheManager.getTreeCache() == null) {
      throw new ServiceException(
          "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()");
    }
    InputStream input = null;
    OutputContentFile contentImage = new OutputContentFile();
    try {

      FileNameMap fileNameMap = URLConnection.getFileNameMap();

      // SMALL by default
      StringBuilder nodePath = new StringBuilder();
      nodePath.append(categoryCode)
          .append(Constants.SLASH);

      Node<String, Object> categoryNode = this.getNode(nodePath.toString());


      byte[] imageBytes = (byte[]) categoryNode.get(imageName);

      if (imageBytes == null) {
        LOGGER.warn("Image " + imageName + " does not exist");
        return null;// no post processing will occur
      }

      input = new ByteArrayInputStream(imageBytes);
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      IOUtils.copy(input, output);

      String contentType = fileNameMap.getContentTypeFor(imageName);

      contentImage.setFile(output);
      contentImage.setMimeType(contentType);
      contentImage.setFileName(imageName);



    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (Exception ignore) {
        }
      }
    }

      return contentImage;

  }


  @SuppressWarnings("unchecked")
  private Node<String, Object> getNode(final String node) {
    LOGGER.debug("Fetching node for store {} from Infinispan", node);
    final StringBuilder merchantPath = new StringBuilder();
    merchantPath.append(getRootName()).append(node);

    Fqn contentFilesFqn = Fqn.fromString(merchantPath.toString());

    Node<String, Object> nd = cacheManager.getTreeCache().getRoot().getChild(contentFilesFqn);

    if (nd == null) {

      cacheManager.getTreeCache().getRoot().addChild(contentFilesFqn);
      nd = cacheManager.getTreeCache().getRoot().getChild(contentFilesFqn);

    }

    return nd;

  }

  public CacheManager getCacheManager() {
    return cacheManager;
  }

  public void setCacheManager(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  public void setRootName(String rootName) {
    this.rootName = rootName;
  }

  public String getRootName() {
    return rootName;
  }
}
