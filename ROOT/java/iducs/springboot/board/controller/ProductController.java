package iducs.springboot.board.controller;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import iducs.springboot.board.domain.Category;
import iducs.springboot.board.domain.ClothesSize;
import iducs.springboot.board.domain.Division;
import iducs.springboot.board.domain.Product;
import iducs.springboot.board.domain.ProductQuestion;
import iducs.springboot.board.domain.ProductSize;
import iducs.springboot.board.domain.ProductStock;
import iducs.springboot.board.domain.Section;
import iducs.springboot.board.entity.ProductEntity;
import iducs.springboot.board.entity.ProductQuestionEntity;
import iducs.springboot.board.service.CategoryService;
import iducs.springboot.board.service.ClothesSizeService;
import iducs.springboot.board.service.ColorService;
import iducs.springboot.board.service.DivisionService;
import iducs.springboot.board.service.ProductQuestionService;
import iducs.springboot.board.service.ProductService;
import iducs.springboot.board.service.ProductSizeService;
import iducs.springboot.board.service.ProductStockService;
import iducs.springboot.board.service.SectionService;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	ClothesSizeService clothessizeService;
	@Autowired
	ColorService colorService;
	@Autowired
	ProductService productService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	DivisionService divisionService;
	@Autowired
	SectionService sectionService;
	@Autowired
	ProductSizeService productsizeService;
	@Autowired
	ProductStockService productstockService;
	@Autowired
	ProductQuestionService productquestionService;
	
	@GetMapping("/list/category/{cno}")
	public String productCategoryList(@PathVariable("cno") long categoryno,
			@PageableDefault(size = 9, sort = "no", direction = Sort.Direction.ASC) Pageable pageable, Model model,
			HttpServletRequest request) throws Exception {
		Category category = categoryService.getCategoryByNo(categoryno);

		List<Product> product = productService.getProductByCategoryNo(categoryno, pageable);
		List<Product> related = productService.getRelatedProductByCategoryNo(categoryno);
		List<Product> related2 = productService.getRelatedProductByCategoryNo(categoryno);
		List<ProductStock> productsize = productstockService.findDistinctSizeNo(categoryno);
		List<ProductStock> productcolor = productstockService.findDistinctColorNo(categoryno);
		List<ClothesSize> size = clothessizeService.getClothesSize();
		List<Division> division = divisionService.getDivision();
		List<Section> section = sectionService.getSection();
		Page<ProductEntity> page = productService.getProductByCategoryNoPage(pageable, categoryno);

		model.addAttribute("categoryname", category);
		model.addAttribute("productsize", productsize);
		model.addAttribute("division", division);
		model.addAttribute("section", section);
		model.addAttribute("product", product);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("productcolor", productcolor);
		model.addAttribute("related", related);
		model.addAttribute("related2", related2);

		return "/home/product/list";
	}

	@GetMapping("/Ajax/list/category/{no}/size/{size}/color/{color}/price1/{price1}/price2/{price2}")
	public String filterCategoryAjax(@PathVariable(value = "no") long categoryno,
			@PathVariable(value = "size") String[] sizeArray,
			@PathVariable(value = "color") String[] colorArray,
			@PathVariable(value = "price1") long price1,
			@PathVariable(value = "price2") long price2,
			@PageableDefault(size = 9, sort = "no", direction = Sort.Direction.ASC) Pageable pageable, Model model,
			HttpServletRequest request) throws Exception { // Ajax id 중복체크
		Category category = categoryService.getCategoryByNo(categoryno);
		List<Product> product = productService.getProductByCategoryNoSize(categoryno, sizeArray, colorArray, price1, price2, pageable);
		List<Product> related = productService.getRelatedProductByCategoryNo(categoryno);
		List<Product> related2 = productService.getRelatedProductByCategoryNo(categoryno);
		List<ProductStock> productsize = productstockService.findDistinctSizeNo(categoryno);
		List<ProductStock> productcolor = productstockService.findDistinctColorNo(categoryno);
		List<ClothesSize> size = clothessizeService.getClothesSize();
		List<Division> division = divisionService.getDivision();
		List<Section> section = sectionService.getSection();
		Page<ProductEntity> page = productService.getProductByCategoryNoPageSize(pageable, categoryno, sizeArray, colorArray, price1, price2);

		model.addAttribute("categoryname", category);
		model.addAttribute("productsize", productsize);
		model.addAttribute("division", division);
		model.addAttribute("section", section);
		model.addAttribute("product", product);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("productcolor", productcolor);
		model.addAttribute("related", related);
		model.addAttribute("related2", related2);

		return "/home/product/list";
	}

	
	@ResponseBody
	@PostMapping("/categoryFilterCheck")
	public int categoryFilterCheck(@RequestParam(value = "no") long categoryno, 
			@RequestParam(value = "size") String[] sizeArray,
			@RequestParam(value = "color") String[] colorArray, 
			@RequestParam(value = "price1") long price1,
			@RequestParam(value = "price2") long price2, 
			Pageable pageable) { // Ajax product 결과값 체크
		int result = 0;
		List<Product> product = productService.getProductByCategoryNoSize(categoryno, sizeArray, colorArray, price1, price2, pageable);
		if (product != null)
			result = 1;
		return result;
	}
	
	@GetMapping("/list/division/{dno}")
	public String productDivisionList(@PathVariable("dno") long divisionno,
			@PageableDefault(size = 9, sort = "no", direction = Sort.Direction.ASC) Pageable pageable, Model model,
			HttpServletRequest request) throws Exception {
		Division divisionname = divisionService.getDivisionByNo(divisionno);
		long categoryno = divisionname.getCategory().getNo();
		List<Product> related = productService.getRelatedProductByCategoryNo(categoryno);
		List<Product> related2 = productService.getRelatedProductByCategoryNo(categoryno);
		List<Product> product = productService.getProductByDivisionNo(divisionno, pageable);
		List<ProductStock> productsize = productstockService.findDivisionDistinctSizeNo(divisionno);
		List<ProductStock> productcolor = productstockService.findDivisionDistinctColorNo(divisionno);
		List<ClothesSize> size = clothessizeService.getClothesSize();
		List<Division> division = divisionService.getDivision();
		List<Section> section = sectionService.getSection();
		Page<ProductEntity> page = productService.getProductByDivisionNoPage(pageable, divisionno);

		model.addAttribute("divisionname", divisionname);
		model.addAttribute("productsize", productsize);
		model.addAttribute("division", division);
		model.addAttribute("section", section);
		model.addAttribute("product", product);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("productcolor", productcolor);
		model.addAttribute("related", related);
		model.addAttribute("related2", related2);

		return "/home/product/list";
	}
	
	@GetMapping("/Ajax/list/division/{no}/size/{size}/color/{color}/price1/{price1}/price2/{price2}")
	public String filterDivisionAjax(@PathVariable(value = "no") long divisionno,
			@PathVariable(value = "size") String[] sizeArray,
			@PathVariable(value = "color") String[] colorArray,
			@PathVariable(value = "price1") long price1,
			@PathVariable(value = "price2") long price2,
			@PageableDefault(size = 9, sort = "no", direction = Sort.Direction.ASC) Pageable pageable, Model model,
			HttpServletRequest request) throws Exception { // Ajax id 중복체크
		Division divisionname = divisionService.getDivisionByNo(divisionno);
		long categoryno = divisionname.getCategory().getNo();
		List<Product> related = productService.getRelatedProductByCategoryNo(categoryno);
		List<Product> related2 = productService.getRelatedProductByCategoryNo(categoryno);
		List<Product> product = productService.getProductByDivisionNoSize(divisionno, sizeArray, colorArray, price1, price2, pageable);
		List<ProductStock> productsize = productstockService.findDivisionDistinctSizeNo(divisionno);
		List<ProductStock> productcolor = productstockService.findDivisionDistinctColorNo(divisionno);
		List<ClothesSize> size = clothessizeService.getClothesSize();
		List<Division> division = divisionService.getDivision();
		List<Section> section = sectionService.getSection();
		Page<ProductEntity> page = productService.getProductByDivisionNoPageSize(pageable, divisionno, sizeArray, colorArray, price1, price2);

		model.addAttribute("divisionname", divisionname);
		model.addAttribute("productsize", productsize);
		model.addAttribute("division", division);
		model.addAttribute("section", section);
		model.addAttribute("product", product);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("productcolor", productcolor);
		model.addAttribute("related", related);
		model.addAttribute("related2", related2);

		return "/home/product/list";
	}
	
	@ResponseBody
	@PostMapping("/divisionFilterCheck")
	public int divisionFilterCheck(@RequestParam(value = "no") long divisionno, 
			@RequestParam(value = "size") String[] sizeArray,
			@RequestParam(value = "color") String[] colorArray, 
			@RequestParam(value = "price1") long price1,
			@RequestParam(value = "price2") long price2, 
			Pageable pageable) { // Ajax product 결과값 체크
		int result = 0;
		List<Product> product = productService.getProductByDivisionNoSize(divisionno, sizeArray, colorArray, price1, price2, pageable);
		if (product != null)
			result = 1;
		return result;
	}
	
	@GetMapping("/list/section/{sno}")
	public String productSectionList(@PathVariable("sno") long sectionno,
			@PageableDefault(size = 9, sort = "no", direction = Sort.Direction.ASC) Pageable pageable, Model model,
			HttpServletRequest request) throws Exception {
		Section sectionname = sectionService.getSectionByNo(sectionno);
		long categoryno = sectionname.getCategory().getNo();
		List<Product> related = productService.getRelatedProductByCategoryNo(categoryno);
		List<Product> related2 = productService.getRelatedProductByCategoryNo(categoryno);
		List<Product> product = productService.getProductBySectionNo(sectionno, pageable);
		List<ProductStock> productsize = productstockService.findSectionDistinctSizeNo(sectionno);
		List<ProductStock> productcolor = productstockService.findSectionDistinctColorNo(sectionno);
		List<ClothesSize> size = clothessizeService.getClothesSize();
		List<Division> division = divisionService.getDivision();
		List<Section> section = sectionService.getSection();
		Page<ProductEntity> page = productService.getProductBySectionNoPage(pageable, sectionno);

		model.addAttribute("sectionname", sectionname);
		model.addAttribute("productsize", productsize);
		model.addAttribute("division", division);
		model.addAttribute("section", section);
		model.addAttribute("product", product);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("productcolor", productcolor);
		model.addAttribute("related", related);
		model.addAttribute("related2", related2);

		return "/home/product/list";
	}
	
	@GetMapping("/Ajax/list/section/{no}/size/{size}/color/{color}/price1/{price1}/price2/{price2}")
	public String filterSectionAjax(@PathVariable(value = "no") long sectionno,
			@PathVariable(value = "size") String[] sizeArray,
			@PathVariable(value = "color") String[] colorArray,
			@PathVariable(value = "price1") long price1,
			@PathVariable(value = "price2") long price2,
			@PageableDefault(size = 9, sort = "no", direction = Sort.Direction.ASC) Pageable pageable, Model model,
			HttpServletRequest request) throws Exception { // Ajax id 중복체크
		Section sectionname = sectionService.getSectionByNo(sectionno);
		long categoryno = sectionname.getCategory().getNo();
		List<Product> related = productService.getRelatedProductByCategoryNo(categoryno);
		List<Product> related2 = productService.getRelatedProductByCategoryNo(categoryno);
		List<Product> product = productService.getProductBySectionNoSize(sectionno, sizeArray, colorArray, price1, price2, pageable);
		List<ProductStock> productsize = productstockService.findSectionDistinctSizeNo(sectionno);
		List<ProductStock> productcolor = productstockService.findSectionDistinctColorNo(sectionno);
		List<ClothesSize> size = clothessizeService.getClothesSize();
		List<Division> division = divisionService.getDivision();
		List<Section> section = sectionService.getSection();
		Page<ProductEntity> page = productService.getProductBySectionNoPageSize(pageable, sectionno, sizeArray, colorArray, price1, price2);

		model.addAttribute("sectionname", sectionname);
		model.addAttribute("productsize", productsize);
		model.addAttribute("division", division);
		model.addAttribute("section", section);
		model.addAttribute("product", product);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("productcolor", productcolor);
		model.addAttribute("related", related);
		model.addAttribute("related2", related2);

		return "/home/product/list";
	}
	
	@ResponseBody
	@PostMapping("/sectionFilterCheck")
	public int sectionFilterCheck(@RequestParam(value = "no") long sectionno, 
			@RequestParam(value = "size") String[] sizeArray,
			@RequestParam(value = "color") String[] colorArray, 
			@RequestParam(value = "price1") long price1,
			@RequestParam(value = "price2") long price2, 
			Pageable pageable) { // Ajax product 결과값 체크
		int result = 0;
		List<Product> product = productService.getProductByDivisionNoSize(sectionno, sizeArray, colorArray, price1, price2, pageable);
		if (product != null)
			result = 1;
		return result;
	}
	
	@GetMapping("/quickview/{no}")
	public String productQuickView(
			@PathVariable(value = "no") long no,
			Model model
			) {
		Product product = productService.getProductById(no);
		ProductSize productSize = productsizeService.getProductSizeByNoNativeQuery(no);
		List<ProductStock> sizeStock = productstockService.findSizeByProductNo(no);
		List<ProductStock> colorStock = productstockService.findColorByProductNo(no);
		model.addAttribute("product", product);
		model.addAttribute("productsize", productSize);
		model.addAttribute("size", sizeStock);
		model.addAttribute("color", colorStock);
		return "home/product/quickview";
	}
	
	@GetMapping("/view/{no}")
	public String viewProduct(
			@PathVariable(value = "no") Long no, 
			@PageableDefault(size = 5, sort = "no", direction = Sort.Direction.DESC) Pageable questionPageable,
			Model model,
			HttpServletRequest request) throws Exception{
		Product product = productService.getProductById(no);
		List<ProductStock> sizeStock = productstockService.findSizeByProductNo(no);
		List<ProductStock> colorStock = productstockService.findColorByProductNo(no);
		List<ProductQuestion> productQuestion = productquestionService.getProductQuestion(no, questionPageable);
		List<ProductQuestion> productQuestionOriginal = productquestionService.getProductQuestionOriginal(no);
		List<Product> productCategory = productService.get6ProductByCategoryNo(product.getCategory().getNo());
		List<Product> productDivision = productService.get3ProductByDivisionNo(product.getDivision().getNo());
		Page<ProductQuestionEntity> questionPage = productquestionService.getProductQuestionPage(questionPageable, no);
		
		String deliveryDate = null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatDate = new SimpleDateFormat("MM월 dd일(E) 이내 발송 예정");
		cal.add(Calendar.DATE,+3);
		deliveryDate = formatDate.format(cal.getTime());
		int point = Integer.parseInt(product.getListprice()) / 1000;
		try  {
			ProductSize productSize = productsizeService.getProductSizeByNoNativeQuery(no);
			model.addAttribute("productsize", productSize);
		} catch (Exception e) {
			System.out.println("null~"); // null 체크용 try~catch문
		}
		
		int questionComplete = 0; //  상품 문의중 답변이 완료된 문의들을 count하기위한 변수
		for(int i=0; i < productQuestion.size(); i++) {
			// 불특정 다수의 고객이 문의 작성자의 풀id를 확인할 수 없도록 치환하는 작업
			String id = productQuestion.get(i).getUser_no().getId(); 
			String idSubstring = id.substring(0, 4);
			int length = id.length() - idSubstring.length();
			String replace = new String(new char[length]).replace("\0", "*");
			productQuestion.get(i).getUser_no().setId(idSubstring + replace);
		}
		for(int i=0; i < productQuestionOriginal.size(); i++) {
			// 답변 완료된 문의를 count
			if (productQuestionOriginal.get(i).getStatus() == 1) {
				questionComplete += 1;
			}
		}

		model.addAttribute("product", product);
		model.addAttribute("size", sizeStock);
		model.addAttribute("color", colorStock);
		model.addAttribute("delivery", deliveryDate);
		model.addAttribute("point", point);
		model.addAttribute("productquestionOriginal", productQuestionOriginal);
		model.addAttribute("productquestion", productQuestion);
		model.addAttribute("questionPage", questionPage);
		model.addAttribute("productquestionComplete", questionComplete);
		model.addAttribute("productCategory", productCategory);
		model.addAttribute("productDivision", productDivision);
		return "/home/product/view";
	}

}