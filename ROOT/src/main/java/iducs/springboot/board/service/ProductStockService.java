package iducs.springboot.board.service;

import java.util.List;

import iducs.springboot.board.domain.ProductStock;


public interface ProductStockService {
	ProductStock getProductStockById(long no);
	List<ProductStock> findAll();
	List<ProductStock> getProductStockByProductNo(long no);
	
	List<ProductStock> findDistinctSizeNo(long no);
	List<ProductStock> findDistinctColorNo(long no);
	
	List<ProductStock> findDivisionDistinctSizeNo(long no);
	List<ProductStock> findDivisionDistinctColorNo(long no);
	
	List<ProductStock> findSectionDistinctSizeNo(long no);
	List<ProductStock> findSectionDistinctColorNo(long no);
	
	void saveProductStock(ProductStock productstock);
	void updateProductStock(ProductStock productstock);
	void deleteProductStock(ProductStock productstock);
}
