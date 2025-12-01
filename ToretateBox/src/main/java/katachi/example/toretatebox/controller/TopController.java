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
public class TopController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/top")
    public String showTop(
            @RequestParam(defaultValue = "0") int page,   // ← 今のページ番号
            Model model) {
        
        int pageSize = 8; // 1ページに表示する件数

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Product> productPage = productRepository.findAll(pageable);

        model.addAttribute("products", productPage.getContent());   // 商品リスト
        model.addAttribute("currentPage", page);                    // 現在のページ
        model.addAttribute("totalPages", productPage.getTotalPages()); // 全ページ数

        return "top/top";
    }
}
