package com.salesmanager.core.business.services.catalog.category.image;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.cms.category.CategoryFileManager;
import com.salesmanager.core.business.repositories.catalog.category.image.CategoryImageRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.category.image.CategoryImage;
import com.salesmanager.core.model.catalog.product.file.ProductImageSize;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.content.ImageContentFile;
import com.salesmanager.core.model.content.OutputContentFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */

@Service("categoryImageService")
public class CategoryImageServiceImpl extends SalesManagerEntityServiceImpl<Long, CategoryImage> implements CategoryImageService {
    private CategoryImageRepository categoryImageRepository;

    @Autowired
    private CategoryFileManager categoryFileManager;

    @Autowired
    public CategoryImageServiceImpl(CategoryImageRepository categoryImageRepository) {
        super(categoryImageRepository);
        this.categoryImageRepository = categoryImageRepository;
    }

    @Override
    public void addCategoryImage(Category category, CategoryImage categoryImage, ImageContentFile inputImage) throws ServiceException {
        categoryImage.setCategory(category);

        try {

            Assert.notNull(inputImage.getFile(),"ImageContentFile.file cannot be null");



            categoryFileManager.addCategoryImage(categoryImage, inputImage);

            //insert ProductImage
            this.saveOrUpdate(categoryImage);



        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            try {

                if(inputImage.getFile()!=null) {
                    inputImage.getFile().close();
                }

            } catch(Exception ignore) {

            }
        }
    }

    @Override
    public OutputContentFile getCategoryImage(CategoryImage categoryImage) throws ServiceException {
        return categoryFileManager.getCategoryImage(categoryImage);
    }

    @Override
    public OutputContentFile getCategoryImage(String categoryCode, String imageName) throws ServiceException {
        OutputContentFile outputImage = categoryFileManager.getCategoryImage(categoryCode, imageName);
        return outputImage;
    }

    @Override
    public void removeCategoryImage(CategoryImage categoryImage) throws ServiceException {
        if(!StringUtils.isBlank(categoryImage.getCategoryImage())) {
            categoryFileManager.removeCategoryImage(categoryImage);
        }

        CategoryImage ci = this.getById(categoryImage.getId());


        this.delete(ci);
    }

    @Override
    public void saveOrUpdate(CategoryImage categoryImage) throws ServiceException {
        super.save(categoryImage);
    }
}
