package com.example.phone_order.service;

import com.example.phone_order.model.Order;
import com.example.phone_order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final Map<Long, Order> orderItems = new HashMap<>(); // Giả sử đây là nơi lưu trữ giỏ hàng tạm thời

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Thêm sản phẩm vào giỏ hàng
    public void addProductToOrder(Order orderItem) {
        if (orderItems.containsKey(orderItem.getProduct().getId())) {
            Order existingOrder = orderItems.get(orderItem.getProduct().getId());
            existingOrder.setQuantity(existingOrder.getQuantity() + orderItem.getQuantity());
            existingOrder.setTotalPrice(existingOrder.getProduct().getPrice() * existingOrder.getQuantity());
        } else {
            orderItems.put(orderItem.getProduct().getId(), orderItem);
        }
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public void updateProductQuantity(Long productId, int quantity) {
        if (orderItems.containsKey(productId)) {
            Order orderItem = orderItems.get(productId);
            orderItem.setQuantity(quantity);
            orderItem.setTotalPrice(orderItem.getProduct().getPrice() * quantity);
        }
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeProductFromOrder(Long productId) {
        orderItems.remove(productId);
    }

    // Tính toán tổng tiền và số lượng trong giỏ hàng
    public Map<String, Object> calculateOrder() {
        double totalAmount = 0.0;
        int totalQuantity = 0;

        // Duyệt qua tất cả các sản phẩm trong giỏ hàng để tính tổng tiền và số lượng
        for (Order orderItem : orderItems.values()) {
            totalAmount += orderItem.getTotalPrice();
            totalQuantity += orderItem.getQuantity();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalAmount", totalAmount);
        result.put("totalQuantity", totalQuantity);

        return result;
    }

    // Lấy danh sách các sản phẩm trong giỏ hàng
    public List<Order> getOrderItems() {
        return new ArrayList<>(orderItems.values());
    }
}
