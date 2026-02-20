package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional // Use when working with Large objects Lob in some Entity Field
public class ProductService {

	@Autowired
	private ProductRepo repo;

	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub

		return repo.findAll();
	}

	public Product getProdById(int id) {
		return repo.findById(id).orElse(null);
	}

	public Product updateImageToProdById(int id, MultipartFile imageFile) throws IOException {
		Product product = getProdById(id);
		product.setImageName(imageFile.getOriginalFilename());
		product.setImageType(imageFile.getContentType());
		product.setImageData(imageFile.getBytes());
		return repo.save(product);
	}

	public Product addProductWithImage(Product product, MultipartFile imageFile) throws IOException {
		product.setImageName(imageFile.getOriginalFilename());
		product.setImageType(imageFile.getContentType());
		product.setImageData(imageFile.getBytes());
		return repo.save(product);

	}

	public Product addProductWithoutImage(Product product) {
		return repo.save(product);
	}

	public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
		product.setId(id);
		product.setImageName(imageFile.getOriginalFilename());
		product.setImageType(imageFile.getContentType());
		product.setImageData(imageFile.getBytes());
		return repo.save(product);
	}

	public void deleteProduct(int id) {
		repo.deleteById(id);

	}

	public List<Product> searchProducts(String keyword) {
		return repo.searchProducts(keyword);
	}

	public List<Product> searchProductsByName(String name) {
		return repo.findByName(name);
	}

	public List<Product> searchExpensiveProducts(Double price, String category) {
		return repo.findExpensive(price, category);
	}

	public List<Product> searchByPrice(Double price) {
		return repo.findByPrice(price);
	}

	public Page<Product> getProducts(String name, int page, int size) {

		Pageable pageable = PageRequest.of(page, size);

		return repo.findByNameContaining(name, pageable);
	}

}
