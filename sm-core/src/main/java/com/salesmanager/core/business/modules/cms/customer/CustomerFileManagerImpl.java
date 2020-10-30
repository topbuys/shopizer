package com.salesmanager.core.business.modules.cms.customer;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.model.content.FileContentType;
import com.salesmanager.core.model.content.ImageContentFile;
import com.salesmanager.core.model.content.OutputContentFile;
import com.salesmanager.core.model.customer.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */

public class CustomerFileManagerImpl extends CustomerFileManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerFileManagerImpl.class);


  private CustomerImagePut uploadImage;
  private CustomerImageGet getImage;
  private CustomerImageRemove removeImage;

  public CustomerImagePut getUploadImage() {
    return uploadImage;
  }

  public void setUploadImage(CustomerImagePut uploadImage) {
    this.uploadImage = uploadImage;
  }

  public CustomerImageGet getGetImage() {
    return getImage;
  }

  public void setGetImage(CustomerImageGet getImage) {
    this.getImage = getImage;
  }

  public CustomerImageRemove getRemoveImage() {
    return removeImage;
  }

  public void setRemoveImage(CustomerImageRemove removeImage) {
    this.removeImage = removeImage;
  }

  @Override
  public OutputContentFile getCustomerImage(Customer customer) throws ServiceException {
    return getImage.getCustomerImage(customer);
  }

  @Override
  public void addCustomerImage(Customer customer, ImageContentFile contentImage) throws ServiceException {
    try {

      /** copy to input stream **/
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      // Fake code simulating the copy
      // You can generally do better with nio if you need...
      // And please, unlike me, do something about the Exceptions :D
      byte[] buffer = new byte[1024];
      int len;
      while ((len = contentImage.getFile().read(buffer)) > -1) {
        baos.write(buffer, 0, len);
      }
      baos.flush();

      // Open new InputStreams using the recorded bytes
      // Can be repeated as many times as you wish
      InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
      InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

      BufferedImage bufferedImage = ImageIO.read(is2);


      if (bufferedImage == null) {
        LOGGER.error("Cannot read image format for " + customer.getCustomerImage());
        throw new Exception("Cannot read image format " + customer.getCustomerImage());
      }

      // contentImage.setBufferedImage(bufferedImage);
      contentImage.setFile(is1);


      // upload original -- L
      contentImage.setFileContentType(FileContentType.CUSTOMER);
      uploadImage.addCustomerImage(customer, contentImage);


    } catch (Exception e) {
      LOGGER.error("Error while saving Customer image:  " + customer.getCustomerImage(), e);
      throw new ServiceException(e);
    } finally {
      try {
        customer.getImage().getInputStream().close();
      } catch (Exception ignore) {
      }
    }
  }

  @Override
  public void removeCustomerImage(Customer customer) throws ServiceException {
    removeImage.removeCustomerImage(customer);
  }
}
