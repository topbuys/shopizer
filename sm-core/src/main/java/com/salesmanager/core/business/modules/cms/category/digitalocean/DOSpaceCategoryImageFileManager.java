package com.salesmanager.core.business.modules.cms.category.digitalocean;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.salesmanager.core.business.constants.Constants;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.cms.category.CategoryAssetsManager;
import com.salesmanager.core.business.modules.cms.impl.CMSManager;
import com.salesmanager.core.business.modules.cms.product.ProductAssetsManager;
import com.salesmanager.core.model.catalog.category.image.CategoryImage;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.file.ProductImageSize;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.content.FileContentType;
import com.salesmanager.core.model.content.ImageContentFile;
import com.salesmanager.core.model.content.OutputContentFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Product content file manager with Digital Ocean Space
 * 
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 *
 */
public class DOSpaceCategoryImageFileManager
    implements CategoryAssetsManager {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;



  private static final Logger LOGGER = LoggerFactory.getLogger(DOSpaceCategoryImageFileManager.class);



  private static DOSpaceCategoryImageFileManager fileManager = null;

  private static String DEFAULT_BUCKET_NAME = "topbuys";
  private static String DEFAULT_REGION_NAME = "fra1";
  private static final String ROOT_NAME = "category";

  private static final char UNIX_SEPARATOR = '/';
  private static final char WINDOWS_SEPARATOR = '\\';


  private final static String SMALL = "SMALL";
  private final static String LARGE = "LARGE";

  private CMSManager cmsManager;

  public static DOSpaceCategoryImageFileManager getInstance() {

    if (fileManager == null) {
      fileManager = new DOSpaceCategoryImageFileManager();
    }

    return fileManager;

  }


  @Override
  public OutputContentFile getCategoryImage(CategoryImage categoryImage) throws ServiceException {
    return null;
  }

  @Override
  public OutputContentFile getCategoryImage(String categoryCode, String imageName) throws ServiceException {
    return null;
  }


  @Override
  public void removeCategoryImage(CategoryImage categoryImage) throws ServiceException {
    try {
      // get buckets
      String bucketName = bucketName();

      final AmazonS3 s3 = s3Client();

      String nodePath = nodePath(categoryImage.getCategory().getCode(), String.valueOf(categoryImage.getId()));

      s3.deleteObject(bucketName, nodePath + categoryImage.getCategoryImage());
      LOGGER.info("Remove file");
    } catch (final Exception e) {
      LOGGER.error("Error while removing file", e);
      throw new ServiceException(e);

    }
  }


  @Override
  public void addCategoryImage(CategoryImage categoryImage, ImageContentFile contentImage) throws ServiceException {
    try {
      // get buckets
      String bucketName = bucketName();
      final AmazonS3 s3 = s3Client();

      String nodePath = nodePath(categoryImage.getCategory().getCode(), String.valueOf(categoryImage.getId()));

      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(contentImage.getMimeType());

      PutObjectRequest request = new PutObjectRequest(bucketName,
              nodePath + categoryImage.getCategoryImage(), contentImage.getFile(), metadata);
      request.setCannedAcl(CannedAccessControlList.PublicRead);


      s3.putObject(request);


      LOGGER.info("Category add image");

    } catch (final Exception e) {
      LOGGER.error("Error while adding image to category", e);
      throw new ServiceException(e);

    }
  }

  private Bucket getBucket(String bucket_name) {
    final AmazonS3 s3 = s3Client();
    Bucket named_bucket = null;
    List<Bucket> buckets = s3.listBuckets();
    for (Bucket b : buckets) {
      if (b.getName().equals(bucket_name)) {
        named_bucket = b;
      }
    }

    if (named_bucket == null) {
      named_bucket = createBucket(bucket_name);
    }

    return named_bucket;
  }

  private Bucket createBucket(String bucket_name) {
    final AmazonS3 s3 = s3Client();
    Bucket b = null;
    if (s3.doesBucketExistV2(bucket_name)) {
      System.out.format("Bucket %s already exists.\n", bucket_name);
      b = getBucket(bucket_name);
    } else {
      try {
        b = s3.createBucket(bucket_name);
      } catch (AmazonS3Exception e) {
        System.err.println(e.getErrorMessage());
      }
    }
    return b;
  }

  /**
   * Builds an amazon S3 client
   * 
   * @return
   */
  private AmazonS3 s3Client() {
    AmazonS3 s3 = AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration(
                            this.cmsManager.getBaseUrl(),
                            regionName()))
            .withCredentials(new EnvironmentVariableCredentialsProvider())
            .build();

    return s3;
  }

  private String bucketName() {
    String bucketName = getCmsManager().getRootName();
    if (StringUtils.isBlank(bucketName)) {
      bucketName = DEFAULT_BUCKET_NAME;
    }
    return bucketName;
  }

  private String regionName() {
    String regionName = getCmsManager().getLocation();
    if (StringUtils.isBlank(regionName)) {
      regionName = DEFAULT_REGION_NAME;
    }
    return regionName;
  }

  private String nodePath(String categoryCode) {
    return new StringBuilder().append(ROOT_NAME).append(Constants.SLASH).append(categoryCode)
        .append(Constants.SLASH).toString();
  }

  private String nodePath(String categoryCode, String categoryImageId) {

    StringBuilder sb = new StringBuilder();

    String nodePath = nodePath(categoryCode);
    sb.append(nodePath).append(categoryImageId).append(Constants.SLASH);

    return sb.toString();


  }

  public static String getName(String filename) {
    if (filename == null) {
      return null;
    }
    int index = indexOfLastSeparator(filename);
    return filename.substring(index + 1);
  }

  public static int indexOfLastSeparator(String filename) {
    if (filename == null) {
      return -1;
    }
    int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
    int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
    return Math.max(lastUnixPos, lastWindowsPos);
  }



  public CMSManager getCmsManager() {
    return cmsManager;
  }

  public void setCmsManager(CMSManager cmsManager) {
    this.cmsManager = cmsManager;
  }
}
