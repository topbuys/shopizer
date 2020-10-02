package com.salesmanager.core.business.repositories.catalog.category.image;

import com.salesmanager.core.model.catalog.category.image.CategoryImage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */

public interface CategoryImageRepository extends JpaRepository<CategoryImage, Long> {


//	@Query("select p from ProductImage p left join fetch p.descriptions pd inner join fetch p.product pp inner join fetch pp.merchantStore ppm where p.id = ?1")
//	ProductImage findOne(Long id);
	
	
}
