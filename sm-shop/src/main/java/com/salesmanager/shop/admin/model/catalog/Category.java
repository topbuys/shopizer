package com.salesmanager.shop.admin.model.catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import com.salesmanager.core.model.catalog.category.CategoryDescription;
import com.salesmanager.core.model.catalog.category.image.CategoryImage;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import org.springframework.web.multipart.MultipartFile;

/**
 * Wrapper to ease admin jstl
 * @author carlsamson
 *
 */
public class Category implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private com.salesmanager.core.model.catalog.category.Category category;
  
  @Valid
  private List<CategoryDescription> descriptions = new ArrayList<CategoryDescription>();

  private MultipartFile image = null;

  private CategoryImage categoryImage = null;

  public com.salesmanager.core.model.catalog.category.Category getCategory() {
    return category;
  }

  public void setCategory(com.salesmanager.core.model.catalog.category.Category category) {
    this.category = category;
  }

  public List<CategoryDescription> getDescriptions() {
    return descriptions;
  }

  public void setDescriptions(List<CategoryDescription> descriptions) {
    this.descriptions = descriptions;
  }

  public MultipartFile getImage() {
    return image;
  }

  public void setImage(MultipartFile image) {
    this.image = image;
  }

  public CategoryImage getCategoryImage() {
    return categoryImage;
  }

  public void setCategoryImage(CategoryImage categoryImage) {
    this.categoryImage = categoryImage;
  }

}
