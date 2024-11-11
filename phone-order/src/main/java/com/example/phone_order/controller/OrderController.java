package com.example.phone_order.controller;

import com.example.phone_order.model.Order;
import com.example.phone_order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/add")
    public void addProductToOrder(@RequestBody Order orderItem) {
        orderService.addProductToOrder(orderItem);
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    @PutMapping("/update/{productId}")
    public void updateProductQuantity(@PathVariable Long productId, @RequestParam int quantity) {
        orderService.updateProductQuantity(productId, quantity);
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @DeleteMapping("/remove/{productId}")
    public void removeProductFromOrder(@PathVariable Long productId) {
        orderService.removeProductFromOrder(productId);
    }

    // Tính toán tổng tiền và số lượng trong giỏ hàng
    @GetMapping("/summary")
    public Map<String, Object> calculateOrder() {
        return orderService.calculateOrder();
    }
}
