package com.salesmanager.core.business.modules.cms.category;

import com.salesmanager.core.business.constants.Constants;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.cms.product.ProductFileManager;
import com.salesmanager.core.business.modules.cms.product.ProductImageGet;
import com.salesmanager.core.business.modules.cms.product.ProductImagePut;
import com.salesmanager.core.business.modules.cms.product.ProductImageRemove;
import com.salesmanager.core.business.utils.CoreConfiguration;
import com.salesmanager.core.business.utils.ProductImageCropUtils;
import com.salesmanager.core.business.utils.ProductImageSizeUtils;
import com.salesmanager.core.model.catalog.category.image.CategoryImage;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.file.ProductImageSize;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.content.FileContentType;
import com.salesmanager.core.model.content.ImageContentFile;
import com.salesmanager.core.model.content.OutputContentFile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;

/**
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */

public class CategoryFileManagerImpl extends CategoryFileManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(CategoryFileManagerImpl.class);


  private CategoryImagePut uploadImage;
  private CategoryImageGet getImage;
  private CategoryImageRemove removeImage;

  public CategoryImageRemove getRemoveImage() {
    return removeImage;
  }

  public CategoryImagePut getUploadImage() {
    return uploadImage;
  }

  public void setUploadImage(CategoryImagePut uploadImage) {
    this.uploadImage = uploadImage;
  }

  public CategoryImageGet getGetImage() {
    return getImage;
  }

  public void setGetImage(CategoryImageGet getImage) {
    this.getImage = getImage;
  }

  public void setRemoveImage(CategoryImageRemove removeImage) {
    this.removeImage = removeImage;
  }


  public void addCategoryImage(CategoryImage categoryImage, ImageContentFile contentImage)
      throws ServiceException {


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
        LOGGER.error("Cannot read image format for " + categoryImage.getCategoryImage());
        throw new Exception("Cannot read image format " + categoryImage.getCategoryImage());
      }

      // contentImage.setBufferedImage(bufferedImage);
      contentImage.setFile(is1);


      // upload original -- L
      contentImage.setFileContentType(FileContentType.CATEGORY);
      uploadImage.addCategoryImage(categoryImage, contentImage);


    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      try {
        categoryImage.getImage().close();
      } catch (Exception ignore) {
      }
    }

  }


  public OutputContentFile getCategoryImage(CategoryImage categoryImage) throws ServiceException {
    // will return original
    return getImage.getCategoryImage(categoryImage);
  }

  @Override
  public OutputContentFile getCategoryImage(String categoryCode, String imageName) throws ServiceException {
    return getImage.getCategoryImage(categoryCode, imageName);
  }

  @Override
  public void removeCategoryImage(CategoryImage categoryImage) throws ServiceException {
    this.removeImage.removeCategoryImage(categoryImage);
  }

}
