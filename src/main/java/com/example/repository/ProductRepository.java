package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

	List<Product> findByCategoryId(String id);

	Page<Product> findAllByNameLike(String keywords, Pageable pageable);
	
	Page<Product> findAllByCategoryId(String id, Pageable pageable);

	List<Product> findByActiveOrderByCategoryIdDesc(boolean active);
	
	@Query(value = "select f.product from Favority f where f.user.id = :id")
	List<Product> findByFavorityWithUserId(@Param("id") String id);

	@Query(value = "select f.product from Favority f where f.user.id = :id and f.product.category.id = :cateId")
	List<Product> findByFavorityWithUserIdAndCateId(@Param("id") String id, @Param("cateId") String cateId);

	@Query(value = "select f.product from Favority f")
	List<Product> findByFavorityTop10();
	
	@Query(value = "select s.product from Share s")
	List<Product> findByShareTop10();

}