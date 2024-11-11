package com.example.phone_order.service;

import com.example.phone_order.model.Product;
import com.example.phone_order.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository; // Không phải là static final

    // Sử dụng constructor để tiêm phụ thuộc
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Lấy tất cả sản phẩm từ cơ sở dữ liệu
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Tìm sản phẩm theo ID
    public Product getProductById(Long productId) {
        // Sử dụng Optional để kiểm tra nếu sản phẩm không tồn tại
        Optional<Product> productOptional = productRepository.findById(productId);
        return productOptional.orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
    }
}
