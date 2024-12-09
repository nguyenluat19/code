package com.example.KTHP.controllers;

import com.example.KTHP.models.Product;
import com.example.KTHP.reponsitory.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String getProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "main";
    }
    @GetMapping("/products")
    public String getShops(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "product";
    }
    @GetMapping("/main")
    public String getMain(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "main";
    }
    @PostMapping("/order/add")
    public String addToCart(@RequestParam("productId") Long productId, HttpSession session) {
        // Lấy sản phẩm từ cơ sở dữ liệu
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            List<Product> cart = (List<Product>) session.getAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
                session.setAttribute("cart", cart); // Tạo mới giỏ hàng nếu chưa có
            }
            cart.add(product); // Thêm sản phẩm vào giỏ hàng
        }
        return "redirect:/order"; // Chuyển hướng đến trang giỏ hàng
    }

    // Hiển thị giỏ hàng
    @GetMapping("/order")
    public String showCart(HttpSession session, Model model) {
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>(); // Nếu giỏ hàng rỗng, tạo mới
        }
        model.addAttribute("cart", cart); // Thêm giỏ hàng vào model để hiển thị trên trang
        return "order"; // Trả về trang giỏ hàng
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @PostMapping("/order/remove")
    public String removeFromCart(@RequestParam("productId") Long productId, HttpSession session) {
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart != null) {
            cart.removeIf(product -> product.getId().equals(productId)); // Xóa sản phẩm theo ID
        }
        return "redirect:/order"; // Cập nhật lại trang giỏ hàng sau khi xóa
    }
    @GetMapping("/admin")
    public String getAdmin(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "admin";
    }
    @GetMapping("/admin/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "addProduct"; // Trả về form thêm sản phẩm
    }

    @PostMapping("/admin/add")
    public String addProduct(@ModelAttribute Product product) {
        productRepository.save(product); // Lưu sản phẩm vào cơ sở dữ liệu
        return "redirect:/admin"; // Quay lại trang admin
    }

    // Sửa sản phẩm (admin)
    @GetMapping("/admin/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return "error";
        }
        model.addAttribute("product", product.get());
        return "editProduct"; // Trả về form sửa sản phẩm
    }

    @PostMapping("/admin/edit/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product product) {
        product.setId(id);
        productRepository.save(product); // Cập nhật sản phẩm trong cơ sở dữ liệu
        return "redirect:/admin"; // Quay lại trang admin
    }

    // Xóa sản phẩm (admin)
    @PostMapping("/admin/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id); // Xóa sản phẩm khỏi cơ sở dữ liệu
        return "redirect:/admin"; // Quay lại trang admin
    }
    @GetMapping("/products/search")
    public String getSearch(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Product> products;

        if (query != null && !query.isEmpty()) {
            // Tìm sản phẩm theo tên chứa từ khóa (không phân biệt chữ hoa/thường)
            products = productRepository.findByNameContainingIgnoreCase(query);
        } else {
            // Nếu không có từ khóa tìm kiếm, lấy tất cả sản phẩm
            products = productRepository.findAll();
        }

        model.addAttribute("products", products);
        return "search"; // Trả về trang kết quả tìm kiếm
    }

    @GetMapping("/products/detail")
    public String getProductDetail(@RequestParam("id") Long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            // Trả về trang lỗi nếu sản phẩm không tồn tại
            return "error";
        }
        model.addAttribute("product", product);
        return "detail"; // Trả về trang chi tiết sản phẩm
    }
}