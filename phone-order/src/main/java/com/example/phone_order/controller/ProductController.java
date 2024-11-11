package com.example.phone_order.controller;

import com.example.phone_order.model.Product;
import com.example.phone_order.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    private final List<OrderItem> orderItems = new ArrayList<>();

    @GetMapping("/")
    public String index(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "index";
    }

    @PostMapping("/orderProduct")
    public String orderProduct(@RequestParam("productId") Long productId,
                               @RequestParam("quantity") int quantity,
                               Model model) {
        Product product = productService.getProductById(productId);

        if (product != null && quantity > 0) {
            double totalPrice = product.getPrice() * quantity;
            OrderItem orderItem = new OrderItem(product, quantity, totalPrice);
            orderItems.add(orderItem);
        }

        model.addAttribute("orderItems", orderItems);
        return "order";
    }

    public static class OrderItem {
        private Product product;
        private int quantity;
        private double totalPrice;

        public OrderItem(Product product, int quantity, double totalPrice) {
            this.product = product;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getTotalPrice() {
            return totalPrice;
        }
    }
}
