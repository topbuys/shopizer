package com.salesmanager.core.business.modules.cms.customer.infinispan;

import com.salesmanager.core.business.constants.Constants;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.cms.customer.CustomerAssetsManager;
import com.salesmanager.core.business.modules.cms.impl.CMSManager;
import com.salesmanager.core.business.modules.cms.impl.CacheManager;
import com.salesmanager.core.model.content.ImageContentFile;
import com.salesmanager.core.model.content.OutputContentFile;
import com.salesmanager.core.model.customer.Customer;
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

/**
 * Manager for storing in retrieving image files from the CMS This is a layer on top of Infinispan
 * https://docs.jboss.org/author/display/ISPN/Tree+API+Module
 * 
 * Manages - Customer images
 * 
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */
public class CmsImageFileManagerImpl implements CustomerAssetsManager {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(CmsImageFileManagerImpl.class);

  private static CmsImageFileManagerImpl fileManager = null;

  private final static String ROOT_NAME = "customer-merchant";

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
  public OutputContentFile getCustomerImage(Customer customer) throws ServiceException {
    return getCustomerImageContent(customer.getMerchantStore().getCode(), customer.getId(), customer.getCustomerImage());
  }

  @Override
  public void addCustomerImage(Customer customer, ImageContentFile contentImage) throws ServiceException {
    if (cacheManager.getTreeCache() == null) {
      throw new ServiceException(
              "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()");
    }

    try {

      // node
      StringBuilder nodePath = new StringBuilder();
      nodePath.append(customer.getMerchantStore().getCode())
              .append(Constants.SLASH)
              .append(customer.getId())
              .append(Constants.SLASH);;

      Node<String, Object> customerNode = getNode(nodePath.toString());


      InputStream isFile = contentImage.getFile();

      ByteArrayOutputStream output = new ByteArrayOutputStream();
      IOUtils.copy(isFile, output);


      // object for a given product containing all images
      customerNode.put(contentImage.getFileName(), output.toByteArray());

    } catch (Exception e) {

      throw new ServiceException(e);

    }
  }

  @Override
  public void removeCustomerImage(Customer customer) throws ServiceException {
    if (cacheManager.getTreeCache() == null) {
      throw new ServiceException(
              "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()");
    }

    try {
      StringBuilder nodePath = new StringBuilder();
      nodePath.append(customer.getMerchantStore().getCode()).append(Constants.SLASH).append(customer.getId()).append(Constants.SLASH);;

      Node<String, Object> customerNode = getNode(nodePath.toString());
      customerNode.remove(customer.getCustomerImage());
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }


  private OutputContentFile getCustomerImageContent(String merchantCode, Long customerId, String imageName) throws ServiceException {

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
      nodePath.append(merchantCode)
          .append(Constants.SLASH)
          .append(customerId)
          .append(Constants.SLASH);

      Node<String, Object> customerNode = this.getNode(nodePath.toString());


      byte[] imageBytes = (byte[]) customerNode.get(imageName);

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
    LOGGER.debug("Fetching node for customer {} from Infinispan", node);
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
