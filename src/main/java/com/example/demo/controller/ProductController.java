package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.ProductService;

import tools.jackson.databind.ObjectMapper;

import com.example.demo.model.Product;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

	@Autowired
	private ProductService service;

	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts() {

		return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
	}

	@GetMapping("/product/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable("id") int id) {

		Product product = service.getProdById(id);
		if (product != null)
			return new ResponseEntity<>(product, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/product/{id}/image")
	public ResponseEntity<?> getImageByProdId(@PathVariable("id") int id) {
		Product prod = service.getProdById(id);
		if(prod != null && prod.getImageData() != null) {
		byte[] imageFile = prod.getImageData();
		return ResponseEntity.ok().contentType(MediaType.valueOf(prod.getImageType())).body(imageFile);
		}
		else {
			return new ResponseEntity<>("Product not valid ", HttpStatus.NOT_FOUND);			
		}
	}

	@PutMapping("/product/{id}/image")
	public ResponseEntity<?> updateImageToProduct(@PathVariable("id") int id,
			@RequestParam("image") MultipartFile imageFile) {
		Product prod = service.getProdById(id);
		if(prod != null) {
		try {
			Product Outprod = service.updateImageToProdById(id, imageFile);

			return ResponseEntity.ok().contentType(MediaType.valueOf(Outprod.getImageType())).body(Outprod.getImageData());
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		}
		else {
			return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
		}

	}

	@PutMapping(value = "/product/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateProduct(@PathVariable("id") int id, @RequestPart("product") String productJSON,
			@RequestPart("image") MultipartFile imageFile) {

		ObjectMapper mapper = new ObjectMapper();
		Product inProd = mapper.readValue(productJSON, Product.class);
		Product destProd = service.getProdById(id);
		if (destProd != null) {
			try {
				
				Product outProd = service.updateProduct(id, inProd, imageFile);
				if (outProd != null)
					return new ResponseEntity<>("updated", HttpStatus.OK);
				else
					return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);

			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> addProductWithImage(@RequestPart("product") String productJSON,
			@RequestPart("image") MultipartFile imageFile) {
		try {

			ObjectMapper mapper = new ObjectMapper();

			Product inProd = mapper.readValue(productJSON, Product.class);
			Product outProd = service.addProductWithImage(inProd, imageFile);
			return new ResponseEntity<>(outProd, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/product")
	public ResponseEntity<?> addProductWithoutImage(@RequestBody Product product) {
		try {
			System.out.println(product);

			Product outProd = service.addProductWithoutImage(product);
			return new ResponseEntity<>(outProd, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") int id) {
		Product product = service.getProdById(id);
		if (product != null) {
			service.deleteProduct(id);
			return new ResponseEntity<>("Deleted", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
		}

	}
	
	@GetMapping("/product/search/{keyword}")
	public ResponseEntity<List<Product>> searchProducts(@PathVariable("keyword") String keyword){
		System.out.println("Searching with "+keyword	 );
		List<Product> products = service.searchProducts(keyword);
		return new ResponseEntity<>(products,HttpStatus.OK);
	}
	@GetMapping("/product/name/{name}")
	public ResponseEntity<List<Product>> searchProductsByName(@PathVariable("name") String name){
		System.out.println("Searching with "+name	 );
		List<Product> products = service.searchProductsByName(name);
		return new ResponseEntity<>(products,HttpStatus.OK);
	}
	@GetMapping("/product/expensive/price/{price}/cat/{cat}")
	public ResponseEntity<List<Product>> searchExpensiveProducts(@PathVariable("price") Double price,@PathVariable("cat") String cat){
		System.out.println("Searching with "+price +" "+cat	 );
		List<Product> products = service.searchExpensiveProducts(price, cat);
		return new ResponseEntity<>(products,HttpStatus.OK);
	}
	
	@GetMapping("/product/price/{price}")
	public ResponseEntity<List<Product>> searchByPrice(@PathVariable("price") Double price){
		List<Product> products = service.searchByPrice(price);
		return new ResponseEntity<>(products,HttpStatus.OK);
	}
	@GetMapping("/products/paging")
	public Page<Product> getAllByName(
			@RequestParam String name,
	        @RequestParam int page,
	        @RequestParam int size) {

	    return service.getProducts(name,page, size);
	}
}
