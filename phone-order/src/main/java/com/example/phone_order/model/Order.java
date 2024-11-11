package com.example.phone_order.model;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private Long id; // Thêm khóa chính cho entity

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;
    private double totalPrice;

    // Constructor không tham số (để Spring JPA có thể sử dụng)
    public Order() {
    }

    // Constructor có tham số
    public Order(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = product.getPrice() * quantity; // Tính tổng giá ngay khi khởi tạo
    }

    // Getter and Setter cho product
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // Getter và Setter cho quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = this.product.getPrice() * quantity; // Cập nhật lại tổng giá khi số lượng thay đổi
    }

    // Getter và Setter cho totalPrice
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Getter và Setter cho id (optional, nếu cần để JPA có thể xác định khóa chính)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
