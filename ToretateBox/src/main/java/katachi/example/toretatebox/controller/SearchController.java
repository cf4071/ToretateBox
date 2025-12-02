package katachi.example.toretatebox.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import katachi.example.toretatebox.domain.model.Product;
import katachi.example.toretatebox.service.ProductService;

@Controller
public class SearchController {

    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword, Model model) {

    	List<Product> results = productService.searchProducts(keyword);


        model.addAttribute("keyword", keyword);
        model.addAttribute("products", results);

        return "search/search"; // search.html を表示
    }
}
