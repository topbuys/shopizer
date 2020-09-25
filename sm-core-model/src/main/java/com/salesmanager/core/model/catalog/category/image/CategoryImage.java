package com.salesmanager.core.model.catalog.category.image;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.generic.SalesManagerEntity;

import javax.persistence.*;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */

@Entity
@Table(name = "CATEGORY_IMAGE", schema= SchemaConstant.SALESMANAGER_SCHEMA)
public class CategoryImage extends SalesManagerEntity<Long, CategoryImage> {

    @Id
    @Column(name = "CATEGORY_IMAGE_ID")
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_IMG_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    @Column(name = "CATEGORY_IMAGE")
    private String categoryImage;

    @Column(name = "DEFAULT_IMAGE")
    private boolean defaultImage = true;

    @Transient
    private InputStream image = null;

    public CategoryImage(){
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }

    public boolean isDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(boolean defaultImage) {
        this.defaultImage = defaultImage;
    }
}
