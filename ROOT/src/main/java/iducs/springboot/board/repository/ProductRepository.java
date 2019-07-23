package iducs.springboot.board.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import iducs.springboot.board.entity.ProductEntity;
import iducs.springboot.board.entity.ProductStockEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>{
	ProductEntity findByNo(long no);
	List<ProductEntity> findByCategoryNo(Pageable pageable, long no);
	Page<ProductEntity> findByCategoryNo(long no, Pageable pageable);
	
	@Query(value="SELECT * FROM product as p LEFT JOIN product_stock ON p.no = product_stock.product_no WHERE product_stock.size_no IN (:size) AND p.category_no = :no GROUP BY p.name", nativeQuery=true)
	List<ProductEntity> findByCategoryNo(@Param("no") long categoryno, @Param("size") String[] size, Pageable pageable);
	
	@Query(value="select * from product as p left join product_stock on p.no = product_stock.product_no where product_stock.size_no in (:size) and p.category_no = :no group by p.name", nativeQuery=true)
	Page<ProductEntity> findByCategoryNoPage(@Param("no") long categoryno, Pageable pageable, @Param("size") String[] size);
	
	List<ProductEntity> findAll(Sort sort);
}