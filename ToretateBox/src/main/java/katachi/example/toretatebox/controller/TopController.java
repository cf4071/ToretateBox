package katachi.example.toretatebox.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import katachi.example.toretatebox.domain.model.Product; // ← これを忘れずに追加
import katachi.example.toretatebox.repository.ProductRepository;

@Controller
public class TopController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/top")
    public String showTop(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "top/top"; 
    }
}
