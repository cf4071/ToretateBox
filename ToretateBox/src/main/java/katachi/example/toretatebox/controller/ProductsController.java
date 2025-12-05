package katachi.example.toretatebox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import katachi.example.toretatebox.domain.model.Product;
import katachi.example.toretatebox.repository.ProductRepository;

@Controller
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public String showList(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        int pageSize = 8; // 1ページに表示する件数

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> productPage = productRepository.findAll(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "products/list"; // list.html を表示
    }
}
