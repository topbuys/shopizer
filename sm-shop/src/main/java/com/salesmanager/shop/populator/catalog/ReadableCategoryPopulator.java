package com.salesmanager.shop.populator.catalog;

import com.salesmanager.core.model.catalog.category.image.CategoryImage;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.shop.model.catalog.category.ReadableCategoryImage;
import com.salesmanager.shop.model.catalog.product.ReadableImage;
import com.salesmanager.shop.utils.ImageFilePath;
import org.apache.commons.lang.Validate;

import com.salesmanager.core.business.exception.ConversionException;
import com.salesmanager.core.business.utils.AbstractDataPopulator;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.category.CategoryDescription;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.catalog.category.ReadableCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ReadableCategoryPopulator extends
        AbstractDataPopulator<Category, ReadableCategory> {

    private ImageFilePath imageUtils;

    public ImageFilePath getimageUtils() {
        return imageUtils;
    }

    public void setimageUtils(ImageFilePath imageUtils) {
        this.imageUtils = imageUtils;
    }

    @Override
    public ReadableCategory populate(final Category source,
            final ReadableCategory target,
            final MerchantStore store,
            final Language language) throws ConversionException {

        Validate.notNull(source, "Category must not be null");

        target.setLineage(source.getLineage());
        if (source.getDescriptions() != null && source.getDescriptions().size() > 0) {

            CategoryDescription description = source.getDescription();
            if (source.getDescriptions().size() > 1) {
                for (final CategoryDescription desc : source.getDescriptions()) {
                    if (desc.getLanguage().getCode().equals(language.getCode())) {
                        description = desc;
                        break;
                    }
                }
            }

            if (description != null) {
                final com.salesmanager.shop.model.catalog.category.CategoryDescription desc = new com.salesmanager.shop.model.catalog.category.CategoryDescription();
                desc.setFriendlyUrl(description.getSeUrl());
                desc.setName(description.getName());
                desc.setId(source.getId());
                desc.setDescription(description.getDescription());
                desc.setKeyWords(description.getMetatagKeywords());
                desc.setHighlights(description.getCategoryHighlight());
                desc.setTitle(description.getMetatagTitle());
                desc.setMetaDescription(description.getMetatagDescription());

                target.setDescription(desc);
            }

        }

        if (source.getParent() != null) {
            final com.salesmanager.shop.model.catalog.category.Category parent = new com.salesmanager.shop.model.catalog.category.Category();
            parent.setCode(source.getParent().getCode());
            parent.setId(source.getParent().getId());
            target.setParent(parent);
        }

        target.setCode(source.getCode());
        target.setId(source.getId());
        if (source.getDepth() != null) {
            target.setDepth(source.getDepth());
        }
        target.setSortOrder(source.getSortOrder());
        target.setVisible(source.isVisible());
        target.setFeatured(source.isFeatured());

        Set<CategoryImage> images = source.getImages();
        if(images!=null && images.size()>0) {
            List<ReadableCategoryImage> imageList = new ArrayList<ReadableCategoryImage>();

            String contextPath = imageUtils.getContextPath();

            for(CategoryImage img : images) {
                ReadableCategoryImage catImage = new ReadableCategoryImage();
                catImage.setImageName(img.getCategoryImage());
                catImage.setDefaultImage(img.isDefaultImage());

                StringBuilder imgPath = new StringBuilder();
                imgPath.append(contextPath).append(imageUtils.buildCategorymageUtils(source, img.getCategoryImage()));

                catImage.setImageUrl(imgPath.toString());

                if(catImage.isDefaultImage()) {
                    target.setImage(catImage);
                }

                imageList.add(catImage);
            }
            target.setImages(imageList);
        }

        return target;

    }

    @Override
    protected ReadableCategory createTarget() {
        return null;
    }

}
